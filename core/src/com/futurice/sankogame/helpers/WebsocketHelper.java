package com.futurice.sankogame.helpers;

import com.badlogic.gdx.Gdx;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Mostly from
 * http://www.elabs.se/blog/66-using-websockets-in-native-ios-and-android-apps
 */
public class WebsocketHelper {

    private WebSocketClient mWebSocketClient;
    private static final String LOG_TAG = "WebsocketHelper";
    private String lastMessage = "";

    public void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://secret-peak-3818.herokuapp.com/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Gdx.app.log(LOG_TAG, "Opened");
            }

            @Override
            public void onMessage(String s) {
                Gdx.app.log(LOG_TAG, "Message: " + s);
                lastMessage = s;
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Gdx.app.log(LOG_TAG, "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Gdx.app.log(LOG_TAG, "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void disconnectWebSocket() {
        Gdx.app.log(LOG_TAG, "Close");
        mWebSocketClient.close();
    }
}
