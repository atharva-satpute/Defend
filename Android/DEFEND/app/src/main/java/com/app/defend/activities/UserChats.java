package com.app.defend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.app.defend.R;

public class UserChats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_chats);

        Intent intent = getIntent();
        String receiverName = intent.getStringExtra("receiver_name");
        //TODO: phone Number sent as putExtra
        String phoneNumber = intent.getStringExtra("phone_number");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_chat_toolbar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        View view = actionBar.getCustomView();
        TextView rName = view.findViewById(R.id.receiverUserName);
        rName.setText(receiverName);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
