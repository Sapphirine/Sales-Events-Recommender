package twitter;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashSet;




public class TweetServer extends WebSocketServer {

    HashSet<WebSocket> clients = new HashSet<>();

    public TweetServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(new InetSocketAddress(0).getAddress(), port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println(webSocket.getRemoteSocketAddress());
        clients.add(webSocket);
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        clients.remove(webSocket);
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {

    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    public void publish(String s) {
        //System.out.println(clients.size());
        for (WebSocket webSocket : clients)
            webSocket.send(s);
    }

    public static void main(String[] args) throws IOException {
        TweetServer tweetServer = new TweetServer(11083);
        tweetServer.start();
        System.out.println( "TweetServer started on port: " + tweetServer.getPort());
    }
}