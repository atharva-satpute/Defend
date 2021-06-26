package com.app.defend.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.defend.R;
import com.app.defend.Utils;
import com.app.defend.model.Message;
import com.app.defend.model.User;
import com.app.defend.rsa.RSAUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class UserChats extends AppCompatActivity {

	User receiver;
	FirebaseFirestore db;
	RecyclerView ll;
	ArrayList<Message> messages;
	ArrayList<String> uids;
	Adapter adapter;
	EditText et;
	ImageButton send;

	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_chats);
		uids = new ArrayList<>();

		Intent intent = getIntent();
		String receiverName = intent.getStringExtra("receiver_name");
		String phoneNumber = intent.getStringExtra("phone_number");
		db = FirebaseFirestore.getInstance();
		ll = findViewById(R.id.rvChat);
		messages = new ArrayList<>();
		adapter = new Adapter();
		et = findViewById(R.id.etMessage);
		send = findViewById(R.id.btnSend);

		ll.setLayoutManager(new LinearLayoutManager(this));
		ll.setAdapter(adapter);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.custom_chat_toolbar);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);
		View view = actionBar.getCustomView();
		TextView rName = view.findViewById(R.id.receiverUserName);
		rName.setText(phoneNumber);

		retriveUser(phoneNumber);
		send.setOnClickListener(v -> {

			Message msg = new Message();
			msg.setUID(Utils.getAlphaNumericString(20));
			String typedMsg = et.getText().toString();

			Utils.putMessage(msg.getUID(), typedMsg, this);

			try {
				typedMsg = Base64.getEncoder().encodeToString(RSAUtils.encrypt(typedMsg, receiver.getPublicKey()));
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

			msg.setDate(new Date());
			msg.setEncryptedText(typedMsg);

			msg.setFrom(Utils.getUID(this));
			msg.setTo(receiver.getUID());

			db.collection(uids.get(0) + uids.get(1)).document(msg.getUID())
					.set(msg)
					.addOnSuccessListener(new OnSuccessListener<Void>() {
						@Override
						public void onSuccess(Void aVoid) {
						}
					})
					.addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							Toast.makeText(UserChats.this, "Failed to post msg", Toast.LENGTH_LONG).show();
						}
					});

			et.setText("");
		});
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	public void retriveUser(String phoneNumber) {
		db.collection("Users").whereEqualTo("phoneNo", phoneNumber)
				.get()
				.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
					@Override
					public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
						receiver = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
						retrieveMessages();
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {

					}
				});
	}

	private void retrieveMessages() {
		uids.add(receiver.getUID());
		uids.add(Utils.getUID(this));
		Collections.sort(uids);

		db.collection(uids.get(0) + uids.get(1)).get()
				.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
					@Override
					public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
						for (DocumentSnapshot doc : queryDocumentSnapshots) {
							Message msg = doc.toObject(Message.class);
							messages.add(msg);
						}
						adapter.notifyDataSetChanged();
						setUpListeners();
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {

			}
		});
	}

	private void setUpListeners() {

		db.collection(uids.get(0) + uids.get(1)).addSnapshotListener(new EventListener<QuerySnapshot>() {
			@Override
			public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
				List<DocumentSnapshot> val = value.getDocuments();
				if (val.size() != 0) {
					Message msg = value.getDocuments().get(0).toObject(Message.class);
					messages.add(msg);
					adapter.notifyDataSetChanged();
				}
			}
		});

	}

	public class TextViewHolder extends RecyclerView.ViewHolder {

		TextView tv;

		public TextViewHolder(@NonNull View itemView) {
			super(itemView);
			tv = itemView.findViewById(R.id.chattv);
		}
	}

	public class Adapter extends RecyclerView.Adapter<TextViewHolder> {

		@NonNull
		@Override
		public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View v = LayoutInflater.from(UserChats.this).inflate(R.layout.chattvitem, parent, false);
			return new TextViewHolder(v);
		}

		@RequiresApi(api = Build.VERSION_CODES.O)
		@Override
		public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
			if (messages.get(position).getTo().equals(Utils.getUID(UserChats.this))) {
				try {
					holder.tv.setText(RSAUtils.decrypt(messages.get(position).getEncryptedText(), Utils.getPrivateKey(UserChats.this)));
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				}
			} else {
				holder.tv.setText(Utils.getMessage(messages.get(position).getUID(), UserChats.this));
			}
		}

		@Override
		public int getItemCount() {
			return messages.size();
		}
	}

}
