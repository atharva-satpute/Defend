package com.app.defend.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.defend.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChatsDashboardActivity extends AppCompatActivity {

	FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chats_dashboard);
		fab = findViewById(R.id.newconversation);
		fab.setOnClickListener(v -> {
			Intent i = new Intent(ChatsDashboardActivity.this, ContactsActivity.class);
			startActivity(i);
		});

	}
}