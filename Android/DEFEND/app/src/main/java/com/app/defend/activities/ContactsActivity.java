package com.app.defend.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.defend.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

	ArrayList<String> userContacts, _phones, _names;
	RecyclerView rv;
	ContactsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		rv = findViewById(R.id.recyclerView);
		getPhoneNumbers();
		retriveUsers();

	}

	private void retriveUsers() {

		FirebaseFirestore db = FirebaseFirestore.getInstance();
		db.collection("Users")
				.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
			@Override
			public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
				userContacts = new ArrayList<>();
				for (DocumentSnapshot doc : queryDocumentSnapshots) {
					if (_phones.contains("+91" + doc.getString("phoneNo")))
						userContacts.add(doc.getString("phoneNo"));
				}
				updateUI();
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {

			}
		});

	}

	private void updateUI() {
		adapter = new ContactsAdapter();
		rv.setLayoutManager(new LinearLayoutManager(this));
		rv.setAdapter(adapter);
	}

	private void getPhoneNumbers() {

		_phones = new ArrayList<>();
		_names = new ArrayList<>();
		Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		while (phones.moveToNext()) {

			String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			phoneNumber = phoneNumber.replaceAll("[()\\s-]+", "");

			_phones.add(phoneNumber);
			_names.add(name);

		}
		phones.close();

	}

	public class ContactViewHolder extends RecyclerView.ViewHolder {
		TextView name, phone;

		public ContactViewHolder(@NonNull View itemView) {
			super(itemView);
			name = itemView.findViewById(R.id.name);
			phone = itemView.findViewById(R.id.phone);
		}
	}

	public class ContactsAdapter extends RecyclerView.Adapter<ContactViewHolder> {

		@NonNull
		@Override
		public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View v = LayoutInflater.from(ContactsActivity.this).inflate(R.layout.contacts_list_item, parent, false);
			return new ContactViewHolder(v);
		}

		@Override
		public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
			holder.phone.setText(userContacts.get(position));
			int idx = _phones.indexOf("+91"+userContacts.get(position));
			holder.name.setText(_names.get(idx));
		}

		@Override
		public int getItemCount() {
			return userContacts.size();
		}
	}

}