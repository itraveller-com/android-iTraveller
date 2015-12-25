package com.itraveller.chat;

/**
 * Created by rohan bundelkhandi on 17/12/2015.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itraveller.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private TextView chatText;
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    private LinearLayout singleMessageContainer;
    TextView dateText,timeText,senderNameText;
    SharedPreferences prefs;

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
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.activity_chat_singlemessage, parent, false);
        }

        singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
        ChatMessage chatMessageObj = getItem(position);
    //    senderNameText = (TextView) row.findViewById(R.id.singleSenderName);
        chatText = (TextView) row.findViewById(R.id.singleMessage);
        dateText = (TextView) row.findViewById(R.id.singleMessageDate);
        timeText = (TextView) row.findViewById(R.id.singleMessageTime);

        String messageText=chatMessageObj.message;
        //senderNameText.setText(chatMessageObj.sender_name);
        chatText.setText(chatMessageObj.sender_name + " \n" + chatMessageObj.message);
        chatText.setBackgroundResource(chatMessageObj.left ? R.drawable.bubble_b : R.drawable.bubble_a);

        String date_arr[]=(chatMessageObj.date).split("-");
        dateText.setText(""+date_arr[2]+"-"+date_arr[1]+"-"+date_arr[0]);
        String time_arr[]=(chatMessageObj.time).split(":");
        String time_string="";
        int time_val=Integer.parseInt(time_arr[0]);
        if(time_val>12)
        {
            time_val=time_val%12;
            time_string=""+time_val+":"+time_arr[1]+" PM";
        }
        else
        {
            time_string=""+time_arr[0]+":"+time_arr[1]+" AM";
        }
        timeText.setText(""+time_string);

        //    singleMessageContainer.setBackgroundResource(chatMessageObj.left ? R.drawable.bubble_b : R.drawable.bubble_a);
        singleMessageContainer.setGravity(chatMessageObj.left ? Gravity.LEFT : Gravity.RIGHT);

        return row;
    }

    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
