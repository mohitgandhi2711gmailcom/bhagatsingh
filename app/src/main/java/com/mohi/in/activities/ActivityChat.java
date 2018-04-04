package com.mohi.in.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mohi.in.R;
import com.mohi.in.model.ChatMessageModel;
import com.mohi.in.ui.adapter.ChatAdapter;

import java.util.ArrayList;

public class ActivityChat extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<ChatMessageModel> messageList;
    private EditText send_chat_et;
    private RecyclerView mChatRecyclerView;
    private ChatAdapter mChatAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initData();
        setChatTemporarayData();
        mChatAdapter = new ChatAdapter(messageList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        mChatRecyclerView.setLayoutManager(manager);
        mChatRecyclerView.setAdapter(mChatAdapter);
    }


    private void initData() {
        messageList = new ArrayList<>();
        ImageView send_btn_iv = findViewById(R.id.send_btn_iv);
        send_chat_et = findViewById(R.id.send_chat_et);
        mChatRecyclerView = findViewById(R.id.reyclerview_message_list);
        send_btn_iv.setOnClickListener(this);
        ImageView back_iv = findViewById(R.id.back_iv);
        back_iv.setOnClickListener(this);
    }

    private void setChatTemporarayData() {
        ArrayList<String> fromWhere = new ArrayList<>();
        ArrayList<String> message = new ArrayList<>();
        fromWhere.add("send");
        fromWhere.add("send");
        fromWhere.add("receive");
        fromWhere.add("send");
        fromWhere.add("receive");
        message.add("Hi");
        message.add("How are you?");
        message.add("Good, How are you");
        message.add("I mm good");
        message.add("Happy Bday");
        for (int i = 0; i <= 4; i++) {
            ChatMessageModel obj = new ChatMessageModel();
            obj.setFromwhere(fromWhere.get(i));
            obj.setChatMessage(message.get(i));
            messageList.add(obj);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.send_btn_iv: {
                String message = send_chat_et.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    send_chat_et.setText(null);
                    ChatMessageModel obj = new ChatMessageModel();
                    obj.setChatMessage(message);
                    obj.setFromwhere("send");
                    messageList.add(0, obj);
                    mChatAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(this, "First Enter Data", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.back_iv:
            {
                onBackPressed();
                break;
            }
        }
    }
}
