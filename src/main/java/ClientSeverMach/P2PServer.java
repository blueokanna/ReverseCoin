package ClientSeverMach;


import ReverseCoinChainNetwork.P2PNetwork;
import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import ConnectionAPI.NewReverseCoinBlockInterface;
import ConnectionAPI.ReverseCoinChainConfigInterface;
import ConnectionAPI.ReverseCoinBlockMessagesInterface;

public class P2PServer {


    public void initP2PServer(int port, ReverseCoinChainConfigInterface config, NewReverseCoinBlockInterface newblock, ReverseCoinBlockMessagesInterface peerMessages,P2PNetwork network) {
        WebSocketServer socketServer = new WebSocketServer(new InetSocketAddress(port)) {

            /**
             * 连接建立后触发
             */
            @Override
            public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
                network.getSockets(config).add(webSocket);
            }

            /**
             * 连接关闭后触发
             */
            @Override
            public void onClose(WebSocket webSocket, int i, String s, boolean b) {
                network.getSockets(config).remove(webSocket);
                System.out.println("connection closed to address:" + webSocket.getRemoteSocketAddress());
            }

            /**
             * 接收到客户端消息时触发
             */
            @Override
            public void onMessage(WebSocket webSocket, String message) {
                //作为服务端，业务逻辑处理
                network.handleMessage(webSocket, newblock, peerMessages, network.getSockets(config), message, config);
            }

            /**
             * 发生错误时触发
             */
            @Override
            public void onError(WebSocket webSocket, Exception e) {
                network.getSockets(config).remove(webSocket);
                System.out.println("connection failed to address:" + webSocket.getRemoteSocketAddress());
            }

            @Override
            public void onStart() {

            }

        };
        socketServer.start();
        System.out.println("listening websocket p2p port on: " + port);
    }

}
