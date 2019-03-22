package fr.outlook.marro.laurent.firebaseoc.Controllers.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.outlook.marro.laurent.firebaseoc.Adapter.MessageAdapter;
import fr.outlook.marro.laurent.firebaseoc.Api.MessageHelper;
import fr.outlook.marro.laurent.firebaseoc.Models.Message;
import fr.outlook.marro.laurent.firebaseoc.R;

public class MessageActivity extends AppCompatActivity {

    // ---------------------
    // DECLARATION
    // ---------------------

    List<Message> messages;
    MessageAdapter adapter;
    Toolbar toolbar;
    String userId, receiverID, currentTime, image_receiver_url, image_to_send, message;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this); //Configure ButterKnife
        this.getData();
        this.configureToolBar();
        // Display messages.
        this.configureRecyclerView();
    }

    // ---------------------
    // BINDING
    // ---------------------

    @BindView(R.id.message_recycler_view)
    RecyclerView recyclerView; // Declare RecyclerView
    @BindView(R.id.button_add)
    Button addButton;
    @BindView(R.id.button_send)
    Button sendButton;
    @BindView(R.id.message_text)
    EditText message_text;

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // Configure Toolbar
    private void configureToolBar() {
        this.toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.messages);
    }

    // Configure RecyclerView
    private void configureRecyclerView(){
        // Reset lists
        this.messages = new ArrayList<>();
        // Create adapter passing the list of articles
        this.adapter = new MessageAdapter(this.messages, Glide.with(this));
        // Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getData() {

        // Receiver User from UserAdapter (getIntent, getStringExtra)
        intent = getIntent();
        receiverID = intent.getStringExtra("receiver");

        // Get userId from ChatActivity(getString)
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = preferences.getString("UserID",null);

        // Get image url of receiver from ChatActivity (getString)
        image_receiver_url = intent.getStringExtra("receiver_photo_url");

        // Get Hour (create here)
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        currentTime = simpleDateFormat.format(new Date());

        // Listeners
        addButton.setOnClickListener(view -> {
            // User clicked the button
            Log.i("TAG", "Click on add Button ");
            // method add document to implement
            image_to_send = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDJ6kb1pxXXXOwwgCQkZCVKTGF1rr6PbkGBFKt8rBR9QgSd_FM";
        });

        sendButton.setOnClickListener(view -> {
            // User clicked the button
            message = message_text.getText().toString();

            // Check if text field is not empty
            if (!message.equals("")) {
                // Create a new Message to Firestore
                MessageHelper.createMessage(userId, receiverID, currentTime, image_receiver_url, image_to_send, message)
                        .addOnFailureListener(this.onFailureListener());
                // Reset text field
                message_text.setText("");
                image_to_send = "";
            }
        });
    }

    // --------------------
    // ERROR HANDLER
    // --------------------

    protected OnFailureListener onFailureListener(){
        return e -> Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
    }
}