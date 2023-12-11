package ClientSeverMach;

import ReverseCoinChainNetwork.P2PNetwork;
import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import ConnectionAPI.NewReverseCoinBlockInterface;
import ConnectionAPI.ReverseCoinChainConfigInterface;
import ConnectionAPI.ReverseCoinBlockMessagesInterface;

public class P2PClient {

    public void connectToPeer(String address, ReverseCoinChainConfigInterface config, NewReverseCoinBlockInterface newblock, ReverseCoinBlockMessagesInterface peerMessages,P2PNetwork network) {
        try {
            WebSocketClient socketClient = new WebSocketClient(new URI(address)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    network.PostRequest(this, network.FormatBlockMessages());
                    network.getSockets(config).add(this);
                }

                @Override
                public void onMessage(String message) {
                    network.handleMessage(this, newblock, peerMessages, network.getSockets(config), message, config);
                }

                @Override
                public void onClose(int i, String msg, boolean b) {
                    network.getSockets(config).remove(this);
                    System.out.println("connection closed");
                }

                @Override
                public void onError(Exception e) {
                    network.getSockets(config).remove(this);
                    System.out.println("connection failed");
                }
            };
            socketClient.connect();
        } catch (URISyntaxException e) {
            System.out.println("P2P connect is error:" + e.getMessage());
        }
    }

}
