package com.itraveller.dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.itraveller.R;
import com.itraveller.chat.ChatMain;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {


    Button chat_btn;
    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.chat_view, container, false);

        chat_btn=(Button) view.findViewById(R.id.chat_btn);

        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(), ChatMain.class);
                startActivity(i);
            }
        });

        return view;

    }


}
