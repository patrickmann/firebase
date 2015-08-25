package com.example.patrick.nanochat;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class MainActivity extends ListActivity {
    private Firebase mFirebaseRef;
    private EditText mMessageEditText;
    private FirebaseListAdapter<ChatMessage> mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase("https://radiant-fire-4794.firebaseio.com");
        mMessageEditText = (EditText) this.findViewById(R.id.message_text);

        mListAdapter = new FirebaseListAdapter<ChatMessage>(
                mFirebaseRef, ChatMessage.class, R.layout.message_layout, this) {
            @Override
            protected void populateView(View v, ChatMessage model) {
                ((TextView) v.findViewById(R.id.username_text_view)).setText(model.getName());
                ((TextView) v.findViewById(R.id.message_text_view)).setText(model.getMessage());
            }
        };
        setListAdapter(mListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSendButtonClick(View v) {
        String message = mMessageEditText.getText().toString();
        mFirebaseRef.push().setValue(new ChatMessage("puf", message));
        mMessageEditText.setText("");
    }


    public void onLoginButtonClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.login_email)
                .setTitle(R.string.login_title);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_signin, null));
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AlertDialog dlg = (AlertDialog) dialog;
                final String email =
                        ((TextView) dlg.findViewById(R.id.email)).getText().toString();
                final String password
                        = ((TextView) dlg.findViewById(R.id.password)).getText().toString();

                mFirebaseRef.createUser(email, password, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        mFirebaseRef.authWithPassword(email, password, null);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        mFirebaseRef.authWithPassword(email, password, null);
                    }
                });
            }
        });

        builder.setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
