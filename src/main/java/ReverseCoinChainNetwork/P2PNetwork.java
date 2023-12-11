package ReverseCoinChainNetwork;

import BlockModel.CommandCode;
import static BlockModel.CommandCode.CheckLastestBlock;
import static BlockModel.CommandCode.CheckWholeChain;
import static BlockModel.CommandCode.ReturnLastestBlock;
import static BlockModel.CommandCode.ReturnLastestBlockChain;
import BlockModel.PeerMessages;
import BlockModel.ReverseCoinBlock;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.java_websocket.WebSocket;
import ConnectionAPI.NewReverseCoinBlockInterface;
import ConnectionAPI.ReverseCoinChainConfigInterface;
import ConnectionAPI.ReverseCoinBlockMessagesInterface;

public class P2PNetwork {

    private static final Gson SimpleGson = new GsonBuilder().create();
    private static final Gson PrettyGson = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, String> PetaJsonCache = new HashMap<>();

    public void handleMessage(WebSocket websocket, NewReverseCoinBlockInterface newblock, ReverseCoinBlockMessagesInterface peerMessages, CopyOnWriteArrayList<WebSocket> ListSocket, String receiveMessage, ReverseCoinChainConfigInterface config) {

        try {
            PeerMessages messages = SimpleGson.fromJson(receiveMessage, new TypeToken<PeerMessages>() {
            }.getType());

            CommandCode commandCode = messages.getCommandCode();

            if (commandCode == null) {
                return;
            }
            switch (commandCode) {
                case CheckLastestBlock ->
                    PostRequest(websocket, responseNewBlockMessage(config));
                case CheckWholeChain ->
                    PostRequest(websocket, responseBlockChainMessage(config));
                case ReturnLastestBlock ->
                    handleReceiveBlockResponse(messages.getMessage(), newblock, ListSocket, config);
                case ReturnLastestBlockChain ->
                    handleBlockChainResponse(messages.getMessage(), newblock, ListSocket, config);
            }
        } catch (JsonSyntaxException ex) {
            ex.getMessage();
        }
    }

    //同步区块
    public synchronized void handleReceiveBlockResponse(String blockData, NewReverseCoinBlockInterface newblock, CopyOnWriteArrayList<WebSocket> sockets, ReverseCoinChainConfigInterface config) {

        ReverseCoinBlock newBlockReceived = SimpleGson.fromJson(blockData, ReverseCoinBlock.class);
        ReverseCoinBlock BlockonChain = config.getLatestBlock();

        if (newBlockReceived != null) {
            if (BlockonChain != null) {
                if (newBlockReceived.getIndex() > BlockonChain.getIndex() + 1) {
                    BroadCastBlockMessage(FormatBlockChainMessage(), config);
                } else if (newBlockReceived.getIndex() > BlockonChain.getIndex()
                        && BlockonChain.getThisBlockHash().equals(newBlockReceived.getPreviousHash())) {
                    if (newblock.addNewBlocks(newBlockReceived, config)) {
                        BroadCastBlockMessage(FormatBlockMessages(), config);
                    }
                    System.out.println("Add Block to Local PetaChain");
                    System.out.println();
                }
            } else if (BlockonChain == null) {
                BroadCastBlockMessage(FormatBlockChainMessage(), config);
            }
        }
    }

    //同步区块链
    public synchronized void handleBlockChainResponse(String blockData, NewReverseCoinBlockInterface newblock, CopyOnWriteArrayList<WebSocket> sockets, ReverseCoinChainConfigInterface config) {

        CopyOnWriteArrayList<ReverseCoinBlock> receiveBlockchain = SimpleGson.fromJson(blockData, new TypeToken<CopyOnWriteArrayList<ReverseCoinBlock>>() {
        }.getType());

        if (receiveBlockchain != null && !receiveBlockchain.isEmpty() && newblock.isUpdateBlockChain(receiveBlockchain, config)) {
            if (!newblock.isSortedByIndex(config)) {
                Collections.sort(receiveBlockchain, (ReverseCoinBlock block1, ReverseCoinBlock block2) -> block1.getIndex() - block2.getIndex());
            }

            ReverseCoinBlock latestBlockReceived = receiveBlockchain.get(receiveBlockchain.size() - 1);
            ReverseCoinBlock latestBlockonChain = config.getLatestBlock();

            if (latestBlockonChain == null) {
                newblock.updatePetaChain(receiveBlockchain, config);
            } else {
                if (latestBlockReceived.getIndex() > latestBlockonChain.getIndex()) {
                    if (latestBlockonChain.getThisBlockHash().equals(latestBlockReceived.getPreviousHash())) {
                        if (newblock.addNewBlocks(latestBlockReceived, config)) {
                            BroadCastBlockMessage(responseNewBlockMessage(config), config);
                        }
                        System.out.println("Add PetaBlock to PetaChain Network!");
                    } else {
                        newblock.updatePetaChain(receiveBlockchain, config);
                    }
                }
            }
        }
    }

    public String responseNewBlockMessage(ReverseCoinChainConfigInterface config) {
        String cachedMessage = PetaJsonCache.get("responseNewBlockMessage");
        if (cachedMessage != null) {
            return cachedMessage;
        } else {
            PeerMessages peerMessages = new PeerMessages();

            peerMessages.setCommandCode(CommandCode.ReturnLastestBlock);
            peerMessages.setMessage(PrettyGson.toJson(config.getLatestBlock()));

            String JsonMessage = PrettyGson.toJson(peerMessages);

            PetaJsonCache.put("responseNewBlockMessage", JsonMessage);
            return JsonMessage;
        }
    }

    public String responseBlockChainMessage(ReverseCoinChainConfigInterface config) {
        String cachedMessage = PetaJsonCache.get("responseBlockChainMessage");
        if (cachedMessage != null) {
            return cachedMessage;
        } else {
            PeerMessages peerMessages = new PeerMessages();

            peerMessages.setCommandCode(CommandCode.ReturnLastestBlockChain);
            peerMessages.setMessage(PrettyGson.toJson(config.getBlockList()));

            String JsonMessage = PrettyGson.toJson(peerMessages);

            PetaJsonCache.put("responseBlockChainMessage", JsonMessage);
            return JsonMessage;
        }
    }

    public String FormatBlockMessages() {
        String cachedMessage = PetaJsonCache.get("FormatBlockMessages");
        if (cachedMessage != null) {
            return cachedMessage;
        } else {
            String JsonMessage = PrettyGson.toJson(new PeerMessages(CommandCode.CheckLastestBlock));
            PetaJsonCache.put("FormatBlockMessages", JsonMessage);
            return JsonMessage;
        }
    }

    public String FormatBlockChainMessage() {
        String cachedMessage = PetaJsonCache.get("FormatBlockChainMessage");
        if (cachedMessage != null) {
            return cachedMessage;
        } else {
            String JsonMessage = PrettyGson.toJson(new PeerMessages(CommandCode.CheckWholeChain));
            PetaJsonCache.put("FormatBlockChainMessage", JsonMessage);
            return JsonMessage;
        }
    }

    public void BroadCastBlockMessage(String message, ReverseCoinChainConfigInterface config) {
        CopyOnWriteArrayList<WebSocket> socketsList = this.getSockets(config);
        if (socketsList.isEmpty()) {
            return;
        }
        System.out.println("<--------BroadCast Start: -------->");
        for (WebSocket socket : socketsList) {
            System.out.println("Send This Messages to: " + socket.getRemoteSocketAddress().getAddress().toString()
                    + ":" + socket.getRemoteSocketAddress().getPort() + ". The Block Message: " + message);
            this.PostRequest(socket, message);

        }
        System.out.println("<--------BroadCast End: -------->");
    }

    public void PostRequest(WebSocket websocket, String message) {
        websocket.send(message);
    }

    public CopyOnWriteArrayList<WebSocket> getSockets(ReverseCoinChainConfigInterface config) {
        return config.getSocketsList();
    }
}
