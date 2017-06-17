package com.shalom.itai.theservantexperience.ChatBot;

/**
 * Created by Itai on 02/05/2017.
 */


        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

        import com.shalom.itai.theservantexperience.R;

        import java.util.ArrayList;
        import java.util.List;

class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessageObj = getItem(position);
        View row;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (chatMessageObj.left) {
            row = inflater.inflate(R.layout.right_msg, parent, false);
        }else{
            row = inflater.inflate(R.layout.left_msg, parent, false);
        }
        TextView chatText = (TextView) row.findViewById(R.id.msgr);
        chatText.setText(chatMessageObj.message);
        return row;
    }

}