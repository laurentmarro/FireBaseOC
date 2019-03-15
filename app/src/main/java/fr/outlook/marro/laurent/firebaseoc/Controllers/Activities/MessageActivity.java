package fr.outlook.marro.laurent.firebaseoc.Controllers.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Objects;
import fr.outlook.marro.laurent.firebaseoc.R;

public class MessageActivity extends AppCompatActivity {

    // UX
    private ImageView imageView;
    private TextView username;
    Toolbar toolbar;
    FirebaseFirestore database;
    Intent intent;
    private String userID, name, url, message, sender, receiver;
    private DocumentReference reference;
    private ImageButton button_send,add_send;
    EditText text_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        this.configureToolBar();
        this.configureViews();
        this.display();
        this.actions();
    }

    private void configureToolBar() {
        toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void configureViews() {
        database = FirebaseFirestore.getInstance();
        intent = getIntent();
        userID = intent.getStringExtra("userId");
        username = findViewById(R.id.username_message);
        imageView = findViewById(R.id.profile_image_message);
        button_send = findViewById(R.id.button_send);
        add_send = findViewById(R.id.button_picture_add);
        text_send = findViewById(R.id.text_send);
    }

    private void display() {
        reference = database.collection("users").document(userID);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    name = documentSnapshot.getString("username");
                    url = documentSnapshot.getString("photoURL");

                    username.setText(name);

                    Uri photo = Uri.parse(url);
                    url = String.valueOf(photo);
                    Glide.with(MessageActivity.this)
                            .load(url)
                            .apply(RequestOptions.circleCropTransform())
                            .into(imageView);
                }
            }
        });
    }

    private void actions () {
        sender = this.getCurrentUser().getUid();
        receiver = userID;

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = text_send.getText().toString();
                if(!message.equals("")) {
                    sendMessage(sender, receiver, message);
                    text_send.setText("");
                } else {
                    Toast.makeText(MessageActivity.this, "No Text", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {

        HashMap <String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.collection("chats").add(hashMap);
    }

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
}