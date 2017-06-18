package com.shalom.itai.theservantexperience.chatBot;

import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.shalom.itai.theservantexperience.activities.MainActivity;
import com.shalom.itai.theservantexperience.activities.ToolBarActivity;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.services.BuggerService;
import com.shalom.itai.theservantexperience.utils.Functions;

import java.util.List;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Fulfillment;
import ai.api.model.ResponseMessage;
import ai.api.model.Result;

import static com.shalom.itai.theservantexperience.utils.Constants.CHAT_QUICK_REPLY;
import static com.shalom.itai.theservantexperience.utils.Constants.CHAT_START_MESSAGE;


public class ChatActivity extends ToolBarActivity implements AIListener {
    private static final String TAG = "ChatActivity";
    //private static ChatActivity instance;
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private AIDataService aiDataService;
    private boolean side = false;
    private boolean LEFT = false;
    private boolean RIGHT = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.activity_chat);

        //  setContentView(R.layout.activity_chat);

        Button buttonSend = (Button) findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right_msg);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        Intent startIntent = getIntent();
        String startCov = startIntent.getStringExtra(CHAT_START_MESSAGE);
        if(startCov !=null && !startCov.isEmpty()){
            sendChatMessage(false,false,startCov);
        }
        final AIConfiguration config = new AIConfiguration("7f164d5c270e4014aa878dd674c6bccf",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiDataService = new AIDataService(config);

        String chatReply = startIntent.getStringExtra(CHAT_QUICK_REPLY);
        if(chatReply !=null && !chatReply.isEmpty()){
            sendChatMessage(true,true,chatReply);
        }

        chatText.setText("go to sleep");
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return (event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && sendChatMessage(true, true, "");
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage(true,true,"");// isLeft= true
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

   //     instance = this;
    }
   /*
    private static ChatActivity getInstance()
    {
        return instance;
    }
*/
    private boolean sendChatMessage(boolean isLeft,boolean waitForRespond, String text) {
        String tempText;
        if(!text.isEmpty())
        {
            tempText = text;
        }else {
            tempText =chatText.getText().toString();
            chatText.setText("");
        }
        final String query = tempText;
        if(query.isEmpty())
            return false;
        chatArrayAdapter.add(new ChatMessage(isLeft,query ));
        if(!waitForRespond)
            return true;

        final AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(query);
        new AsyncTask<AIRequest, Void, AIResponse>() {
            @Override
            protected AIResponse doInBackground(AIRequest... requests) {
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
                            if (result.getMetadata().getIntentName() == null)
                            {
                                Uri uri = Uri.parse("http://www.google.com/#q="+query.replace(" ","+"));
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                sendChatMessage(false,false,"I hope it helped you");
                                return;
                            }
                            int point = 0;
                            if (result.getAction().equals("lowerPoints")) {
                                BuggerService.getInstance().saveInsults(query);
                                point = -1;
                            } else if (result.getAction().equals("incPoints")) {
                                BuggerService.getInstance().saveBless(query);
                                point = 1;
                            }


                            if((Functions.allowToChangeFromChat()) && (point != 0)){
                                BuggerService.setSYSTEM_GlobalPoints(point);
                            }
                            Fulfillment answer =result.getFulfillment();

                            List<ResponseMessage> resultList = answer.getMessages();
                            String finalAnswer;
                            if(resultList.size()==1)
                            {
                                finalAnswer = ((ResponseMessage.ResponseSpeech) resultList.get(0)).getSpeech().get(0);
                            }
                            else{
                                String status  = BuggerService.getInstance().getRelationsStatus().getRelationStatus();
                                Log.d(TAG, "run: " + status);
                                finalAnswer =
                                        ((ResponseMessage.ResponseSpeech) resultList.get(BuggerService.getInstance().getRelationsStatus().getResponseNumber())).getSpeech().get(0);
                            }
                            if (result.getMetadata().getIntentName().equals("smalltalk.greetings.goodnight")){
                                sendChatMessage(false,false,finalAnswer);
                                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                                //intent.putExtra("sleep",true); //TODO change sleep call to bugger
                                BuggerService.getInstance().sendJonToSleep();
                                startActivity(intent);
                                finish();
                                return;
                            }
                            sendChatMessage(false,false,finalAnswer);


                            // Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
                            /*Toast.makeText(getApplicationContext(),"Query:" + result.getResolvedQuery() +
                                    "\nAction: " + result.getAction() +
                                    "\nParameters: " + finalParameterString,Toast.LENGTH_LONG).show();
                        */}
                    });


                    return response;
                } catch (AIServiceException e) {
                    Log.d(TAG, "doInBackground: Error!");
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_top_in, R.anim.slide_bottom_out);
    }
}