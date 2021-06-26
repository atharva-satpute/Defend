package com.app.defend.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.defend.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatsDashboardActivity extends AppCompatActivity {

	private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
	FloatingActionButton fab;
	RecyclerView rvchats;
	private FirebaseAuth mAuth;
	private boolean storagePermissionGranted;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chats_dashboard);

		mAuth = FirebaseAuth.getInstance();
		fab = findViewById(R.id.newconversation);
		rvchats = findViewById(R.id.rvChat);

		retrieveMessages();

		fab.setOnClickListener(v -> {

//			getExternalStoragePermission();


				// Check the SDK version and whether the permission is already granted or not.
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
					requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
					//After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
				} else {
					// Android version is lesser than 6.0 or the permission is already granted.
					Intent i = new Intent(ChatsDashboardActivity.this, ContactsActivity.class);
					startActivity(i);
				}

//			if (storagePermissionGranted) {
//				Intent i = new Intent(ChatsDashboardActivity.this, ContactsActivity.class);
//				startActivity(i);
//			}
		});
	}


	private void retrieveMessages() {
		FirebaseFirestore.getInstance().collection("Users").document("Messages").collection("Messages").get()
				.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
					@Override
					public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
						for (DocumentSnapshot doc : queryDocumentSnapshots) {

						}
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {

			}
		});
	}

	private void getExternalStoragePermission() {
		if (ContextCompat.checkSelfPermission(getApplicationContext(),
				Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
			// Permission not granted
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.READ_CONTACTS)) {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
						PERMISSIONS_REQUEST_READ_CONTACTS);
			} else {
				if (!storagePermissionGranted) {
					// If asked for permission before but denied
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

					alertDialogBuilder.setTitle("Permission Needed");
					alertDialogBuilder.setMessage("Contacts permission needed");
					alertDialogBuilder.setPositiveButton("Open Setting", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							Intent intent = new Intent();
							intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
							Uri uri = Uri.fromParts("package", ChatsDashboardActivity.this.getPackageName(), null);
							intent.setData(uri);
							startActivity(intent);
						}
					});
					alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {

						}
					});

					AlertDialog dialog = alertDialogBuilder.create();
					dialog.show();
				} else {
					// If asked permission for the first time
					ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
							PERMISSIONS_REQUEST_READ_CONTACTS);
				}
			}
		} else {
			storagePermissionGranted = true;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				storagePermissionGranted = true;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_chat_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.logout) {
			mAuth.signOut();
			startActivity(new Intent(ChatsDashboardActivity.this, LoginActivity.class));
			finish();
			return true;
		}
		return false;
	}
}