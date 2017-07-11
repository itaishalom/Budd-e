package com.shalom.itai.theservantexperience.chatBot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import com.shalom.itai.theservantexperience.activities.Main2Activity;
import com.shalom.itai.theservantexperience.activities.MainActivity;
import com.shalom.itai.theservantexperience.activities.ToolBarActivity;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.activities.ToolBarActivityNew;
import com.shalom.itai.theservantexperience.services.BuggerService;
import com.shalom.itai.theservantexperience.utils.FontEditView;
import com.shalom.itai.theservantexperience.utils.FontTextView;
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
import static com.shalom.itai.theservantexperience.utils.Constants.PREFS_NAME;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_INITIAL_TIRED_POINTS;
import static com.shalom.itai.theservantexperience.utils.Constants.SETTINGS_TIRED_POINTS;


public class ChatActivity extends ToolBarActivityNew implements AIListener {
    private static final String TAG = "ChatActivity";
    //private static ChatActivity instance;
    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private FontEditView chatText;
    private AIDataService aiDataService;
    private boolean side = false;
    private boolean LEFT = false;
    private boolean RIGHT = true;
    private ConstraintLayout hidingLayout;
    private String[] denyGoodNight = new String[]{"I am not tired", "It's too early", "Not now", "I don't feel like"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_chat, R.menu.tool_bar_options, true,R.id.msgview);
        getSupportActionBar().setIcon(R.drawable.title);
        //  setContentView(R.layout.activity_chat);

        Button buttonSend = (Button) findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right_msg);
        listView.setAdapter(chatArrayAdapter);

        chatText = (FontEditView) findViewById(R.id.msg);
        Intent startIntent = getIntent();
        String startCov = startIntent.getStringExtra(CHAT_START_MESSAGE);
        if (startCov != null && !startCov.isEmpty()) {
            sendChatMessage(false, false, startCov);
        }
        final AIConfiguration config = new AIConfiguration("7f164d5c270e4014aa878dd674c6bccf",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiDataService = new AIDataService(config);

        String chatReply = startIntent.getStringExtra(CHAT_QUICK_REPLY);
        if (chatReply != null && !chatReply.isEmpty()) {
            sendChatMessage(true, true, chatReply);
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
                sendChatMessage(true, true, "");// isLeft= true
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
  /*  @Override
    protected void hideBeneath(ConstraintLayout layoutAppeared) {
        int val = 0;
        if (hidingLayout != null) {
            val = hidingLayout.getHeight();
        }
        ConstraintSet set = new ConstraintSet();
        ListView list = (ListView) findViewById(R.id.msgview);
        //   LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(list.getWidth(), list.getHeight() - layoutAppeared.getHeight());
        //   layout.setLayoutParams(params);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) list.getLayoutParams();
        ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                list.getHeight() + val - layoutAppeared.getHeight());

        newParams.setMargins(16, 16, 16, 16);
        newParams.setMarginStart(20);
        newParams.setMarginEnd(20);

        layout.removeView(list);
        layout.addView(list, -1, newParams);
        set.clone(layout);
        set.connect(list.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 8);
        set.connect(list.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT, 8);
        set.connect(list.getId(), ConstraintSet.TOP, layoutAppeared.getId(), ConstraintSet.BOTTOM, 8);
        set.applyTo(layout);
        hidingLayout = layoutAppeared;
    }

    @Override
    protected void showBeneath(ConstraintLayout layoutAppeared) {
        ConstraintSet set = new ConstraintSet();
        ListView list = (ListView) findViewById(R.id.msgview);
        layout.removeView(list);
        layout.addView(list, -1, originalParams);
        set.clone(layout);
        set.connect(list.getId(), ConstraintSet.TOP, R.id.my_toolbar, ConstraintSet.BOTTOM, 8);
        set.connect(list.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 8);
        set.connect(list.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT, 8);
        set.applyTo(layout);
        hidingLayout = null;
    }*/

    private boolean sendChatMessage(boolean isLeft, boolean waitForRespond, String text) {
        String tempText;
        if (!text.isEmpty()) {
            tempText = text;
        } else {
            tempText = chatText.getText().toString();
            chatText.setText("");
        }
        final String query = tempText;
        if (query.isEmpty())
            return false;
        chatArrayAdapter.add(new ChatMessage(isLeft, query));
        if (!waitForRespond)
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

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (result.getMetadata().getIntentName() == null) {
                                Uri uri = Uri.parse("http://www.google.com/#q=" + query.replace(" ", "+"));
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                sendChatMessage(false, false, "I hope it helped you");
                                return;
                            }
                            String whatHappend = "";
                            int point = 0;
                            if (result.getAction().equals("lowerPoints")) {
                                BuggerService.getInstance().saveInsults(query);
                                whatHappend = "You said mean things";
                                point = -1;
                            } else if (result.getAction().equals("incPoints")) {
                                BuggerService.getInstance().saveBless(query);
                                whatHappend = "You said nice things";
                                point = 1;
                            }


                            if ((Functions.allowToChangeFromChat()) && (point != 0)) {
                                BuggerService.setSYSTEM_GlobalPoints(point, whatHappend);
                            }
                            Fulfillment answer = result.getFulfillment();

                            List<ResponseMessage> resultList = answer.getMessages();
                            String finalAnswer;
                            if (resultList.size() == 1) {
                                finalAnswer = ((ResponseMessage.ResponseSpeech) resultList.get(0)).getSpeech().get(0);
                            } else {
                                String status = BuggerService.getInstance().getRelationsStatus().getRelationStatus();
                                Log.d(TAG, "run: " + status);
                                finalAnswer =
                                        ((ResponseMessage.ResponseSpeech) resultList.get(BuggerService.getInstance().getRelationsStatus().getResponseNumber())).getSpeech().get(0);
                            }
                            if (result.getMetadata().getIntentName().equals("smalltalk.greetings.goodnight")) {
                                SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                                int tired = settings.getInt(SETTINGS_TIRED_POINTS, SETTINGS_INITIAL_TIRED_POINTS);
                                if (tired < SETTINGS_INITIAL_TIRED_POINTS) {
                                    sendChatMessage(false, false, finalAnswer);
                                    Intent intent = new Intent(ChatActivity.this, Main2Activity.class);
                                    //intent.putExtra("sleep",true); //TODO change sleep call to bugger
                                    BuggerService.getInstance().sendJonToSleep();
                                    startActivity(intent);
                                    finish();
                                    return;
                                } else {
                                    int index = Functions.throwRandom(denyGoodNight.length, 0);
                                    finalAnswer = denyGoodNight[index];
                                }
                            }
                            sendChatMessage(false, false, finalAnswer);


                            // Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
                            /*Toast.makeText(getApplicationContext(),"Query:" + result.getResolvedQuery() +
                                    "\nAction: " + result.getAction() +
                                    "\nParameters: " + finalParameterString,Toast.LENGTH_LONG).show();
                        */
                        }
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