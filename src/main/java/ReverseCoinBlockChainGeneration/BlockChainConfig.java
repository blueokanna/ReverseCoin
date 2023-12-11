package ReverseCoinBlockChainGeneration;

import BlockModel.ReverseCoinBlock;
import BlockModel.ReverseCoinTransaction;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.util.encoders.Hex;
import org.java_websocket.WebSocket;
import java.util.concurrent.CopyOnWriteArrayList;
import ConnectionAPI.ReverseCoinChainConfigInterface;

public class BlockChainConfig implements ReverseCoinChainConfigInterface {

    private volatile CopyOnWriteArrayList<ReverseCoinBlock> BlockList = new CopyOnWriteArrayList<>();
    private volatile CopyOnWriteArrayList<ReverseCoinTransaction> TransactionsList = new CopyOnWriteArrayList<>();
    private volatile CopyOnWriteArrayList<WebSocket> socketsList = new CopyOnWriteArrayList<>();

    private int difficulty;
    private int ports;
    private String addressPort;

    @Override
    public int getDifficulty() {
        return difficulty;
    }

    @Override
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public int getPorts() {
        return ports;
    }

    @Override
    public void setPorts(int ports) {
        this.ports = ports;
    }

    @Override
    public String getAddressPort() {
        return addressPort;
    }

    @Override
    public void setAddressPort(String addressPort) {
        this.addressPort = addressPort;
    }

    @Override
    public CopyOnWriteArrayList<ReverseCoinBlock> getBlockList() {
        return BlockList;
    }

    @Override
    public void setBlockList(CopyOnWriteArrayList<ReverseCoinBlock> BlockList) {
        this.BlockList = BlockList;
    }

    @Override
    public CopyOnWriteArrayList<ReverseCoinTransaction> getTransactionsList() {
        return TransactionsList;
    }

    @Override
    public void setTransactionsList(CopyOnWriteArrayList<ReverseCoinTransaction> TransactionsList) {
        this.TransactionsList = TransactionsList;
    }

    @Override
    public CopyOnWriteArrayList<WebSocket> getSocketsList() {
        return socketsList;
    }

    @Override
    public void setSocketsList(CopyOnWriteArrayList<WebSocket> socketsList) {
        this.socketsList = socketsList;
    }

    @Override
    public ReverseCoinBlock getLatestBlock() {
        return !BlockList.isEmpty() ? BlockList.get(BlockList.size() - 1) : null;
    }

    @Override
    public String SHA3with256Hash(String input) {
        SHA3Digest digest = new SHA3Digest(256);
        digest.update(input.getBytes(), 0, input.getBytes().length);
        byte[] result = new byte[digest.getDigestSize()];
        digest.doFinal(result, 0);
        return Hex.toHexString(result);
    }

}
