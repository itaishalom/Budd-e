package com.shalom.itai.theservantexperience.ChatBot;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.shalom.itai.theservantexperience.Activities.MainActivity;
import com.shalom.itai.theservantexperience.R;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Fulfillment;
import ai.api.model.Result;


public class ChatActivity extends AppCompatActivity implements AIListener {
    private static final String TAG = "ChatActivity";
    private static ChatActivity instance;
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private AIDataService aiDataService;
    private boolean side = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        buttonSend = (Button) findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right_msg);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        chatText.setText("who are you?");
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage(true,true);
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage(true,true);// isLeft= true
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
        final AIConfiguration config = new AIConfiguration("d83c66cd6fa046ae8539d9e566004fe0",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiDataService = new AIDataService(config);
        instance = this;
    }
    public static ChatActivity getInstance()
    {
        return instance;
    }

    private boolean sendChatMessage(boolean isLeft,boolean waitForRespond) {
        final String Query = chatText.getText().toString();

        chatArrayAdapter.add(new ChatMessage(isLeft,Query ));
        chatText.setText("");
        if(!waitForRespond)
            return true;
        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(Query);
        new AsyncTask<AIRequest, Void, AIResponse>() {
            @Override
            protected AIResponse doInBackground(AIRequest... requests) {
                final AIRequest request = requests[0];
                try {
                    final AIResponse response = aiDataService.request(aiRequest);
                    final Result result = response.getResult();

                    // Get parameters
                /*    Looper.prepare();
                    String parameterString = "";
                    if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                        for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                            parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                        }
                    }
                    final String finalParameterString = parameterString;
                    String res = result.getFulfillment().getSpeech();*/

                    runOnUiThread(new Runnable(){

                        @Override
                        public void run(){

                            Fulfillment answer =result.getFulfillment();
                            String res = answer.getSpeech();
                            if(Query.contains("night") && res.toLowerCase().contains("bye")){
                                Intent intent = new Intent(ChatActivity.getInstance(), MainActivity.class);
                                intent.putExtra("sleep",true);
                                startActivity(intent);
                                finish();
                            }
                                    chatText.setText(res);
                            sendChatMessage(false,false);
                           // Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
                            /*Toast.makeText(getApplicationContext(),"Query:" + result.getResolvedQuery() +
                                    "\nAction: " + result.getAction() +
                                    "\nParameters: " + finalParameterString,Toast.LENGTH_LONG).show();
                        */}
                    });


                    return response;
                } catch (AIServiceException e) {
                }
                return null;
            }
            @Override
            protected void onPostExecute(AIResponse aiResponse) {
                if (aiResponse != null) {
                    // process aiResponse here
                }
            }
        }.execute(aiRequest);


        return true;
    }

    @Override
    public void onResult(AIResponse result) {

    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}