package com.shalom.itai.theservantexperience.utils;

/**
 * Proudly written by Itai on 11/07/2017.
 */

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.shalom.itai.theservantexperience.services.BuggerService;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class Client {
    static final String URL_WEBSOCKET = "ws://192.168.1.5:8080/WebMobileGroupChatServer/chat?name=";
    // LogCat tag
    private static final String TAG = ClientActivity.class.getSimpleName();

    private WebSocketClient mWebSocketClient;
    // Client name
    private String name = null;

    // JSON flags to identify the kind of JSON response
    private static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_MESSAGE = "message", TAG_EXIT = "exit";
    private static String uName ="";
    private static Client instance = null;

    public static Client getInstance(String username) {
        if (instance == null ) {
            instance = new Client(username);
        }
        return instance;
    }

    public void close() {
        if (mWebSocketClient != null) {
            mWebSocketClient.close();
        }
    }

    public void sendMessage(String msg) {
        try {

            sendMessageToServer(getSendMessageJSON(msg));
        } catch (Exception e) {
            instance.close();
            instance = new Client(uName);
        }
    }

    private Client(String username) {
        /**
         * Creating web socket client. This will have callback methods
         * */
        URI uri;
        try {
            uName= username;
            uri = new URI(URL_WEBSOCKET + username);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri, new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                //    mWebSocketClient.send("hello");
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseMessage(message);

                    }
                });*/
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
                BuggerService.sessionId = null;
                instance = null;
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();

    }

    /**
     * Method to send message to web socket server
     */
    private void sendMessageToServer(String message) {
        if (mWebSocketClient != null) {
            mWebSocketClient.send(message);
            //   client.send(message);
        }
    }

    /**
     * Parsing the JSON message received from server The intent of message will
     * be identified by JSON node 'flag'. flag = self, message belongs to the
     * person. flag = new, a new person joined the conversation. flag = message,
     * a new message received from server. flag = exit, somebody left the
     * conversation.
     */
    private void parseMessage(final String msg) {

        try {
            JSONObject jObj = new JSONObject(msg);

            // JSON node 'flag'
            String flag = jObj.getString("flag");

            // if flag is 'self', this JSON contains session id
            if (flag.equalsIgnoreCase(TAG_SELF)) {

                // Save the session id in shared preferences
                BuggerService.sessionId = jObj.getString("sessionId");

                Log.e(TAG, "Your session id: " + BuggerService.sessionId);

            } else if (flag.equalsIgnoreCase(TAG_NEW)) {
                // If the flag is 'new', new person joined the room
                String name = jObj.getString("name");
                String message = jObj.getString("message");

                // number of people online
                String onlineCount = jObj.getString("onlineCount");


            } else if (flag.equalsIgnoreCase(TAG_MESSAGE)) {
                // if the flag is 'message', new message received
                String fromName = name;
                String message = jObj.getString("message");
                String sessionId = jObj.getString("sessionId");
                boolean isSelf = true;

                // Checking if the message was sent by you
                if (!sessionId.equals(BuggerService.sessionId)) {
                    fromName = jObj.getString("name");
                    isSelf = false;
                }

                ClientActivity.Message m = new ClientActivity.Message(fromName, message, isSelf);

                // Appending the message to chat list

            } else if (flag.equalsIgnoreCase(TAG_EXIT)) {
                // If the flag is 'exit', somebody left the conversation
                String name = jObj.getString("name");
                String message = jObj.getString("message");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public class Message {
        private String fromName, message;
        private boolean isSelf;

        public Message() {
        }

        public Message(String fromName, String message, boolean isSelf) {
            this.fromName = fromName;
            this.message = message;
            this.isSelf = isSelf;
        }

        public String getFromName() {
            return fromName;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSelf() {
            return isSelf;
        }

        public void setSelf(boolean isSelf) {
            this.isSelf = isSelf;
        }

    }

    public String getSendMessageJSON(String message) {
        String json = null;

        try {
            JSONObject jObj = new JSONObject();
            jObj.put("flag", "message");
            jObj.put("sessionId", BuggerService.sessionId);
            jObj.put("message", message);

            json = jObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
