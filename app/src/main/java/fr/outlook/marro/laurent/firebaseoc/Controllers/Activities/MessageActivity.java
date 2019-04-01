package fr.outlook.marro.laurent.firebaseoc.Controllers.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.outlook.marro.laurent.firebaseoc.Adapter.MessageAdapter;
import fr.outlook.marro.laurent.firebaseoc.Api.MessageHelper;
import fr.outlook.marro.laurent.firebaseoc.Models.Message;
import fr.outlook.marro.laurent.firebaseoc.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MessageActivity extends AppCompatActivity {

    // ---------------------
    // FOR DESIGN
    // ---------------------

    @BindView(R.id.message_recycler_view) RecyclerView recyclerView; // Declare RecyclerView
    @BindView(R.id.message_text) EditText message_text;
    @BindView(R.id.image_preview) ImageView imageViewPreview;

    // ---------------------
    // FOR DATA
    // ---------------------

    MessageAdapter adapter;
    private String image_to_send = "";
    String userId, receiverID, currentTime, image_receiver_url, message;
    private SharedPreferences preferences;
    Toolbar toolbar;
    Date date;
    private Uri uriImageSelected;
    List<Message> messages;
    FirebaseFirestore database;
    CollectionReference collectionReference;

    // ---------------------
    // STATIC DATA FOR PICTURE
    // ---------------------

    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ButterKnife.bind(this); //Configure ButterKnife

        this.configureToolBar();
        this.configureRecyclerView();
        this.readMessages();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
    }

    // --------------------
    // ACTIONS
    // --------------------

    @OnClick(R.id.send_button)
    public void onClickSendMessage() {
        Intent intent = getIntent();
        date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        DateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");

        currentTime = formatter.format(date);
        userId = preferences.getString("UserID",null);
        receiverID = intent.getStringExtra("receiver");
        image_receiver_url = intent.getStringExtra("receiver_photo_url");
        message = message_text.getText().toString();

        // Check if the ImageView is set
        if (this.imageViewPreview.getDrawable() == null) {
            // SEND A TEXT MESSAGE
            MessageHelper.createMessage(userId, receiverID, currentTime, image_receiver_url, image_to_send, message).addOnFailureListener(this.onFailureListener());
            this.message_text.setText("");
        } else {
            // SEND A IMAGE + TEXT IMAGE
            this.uploadPhotoInFirebaseAndSendMessage();
            this.message_text.setText("");
            image_to_send="";
            this.imageViewPreview.setImageDrawable(null);
        }
    }

    @OnClick(R.id.add_button)
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void onClickAddFile() {
        this.chooseImageFromPhone();
    }

    // ---------------------
    // UI
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

    // --------------------
    // REST REQUESTS
    // --------------------

    private void uploadPhotoInFirebaseAndSendMessage() {
        String uuid = UUID.randomUUID().toString(); // GENERATE UNIQUE STRING
        // A - UPLOAD TO GCS
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);
        mImageRef.putFile(this.uriImageSelected)
                .addOnSuccessListener(this, taskSnapshot -> {
                    image_to_send = Objects
                            .requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).
                                    getDownloadUrl()).toString();
                    // B - SAVE MESSAGE IN FIRESTORE
                    MessageHelper.createMessage(userId, receiverID, currentTime, image_receiver_url, image_to_send, message).addOnFailureListener(onFailureListener());
                })
                .addOnFailureListener(this.onFailureListener());
    }

    // --------------------
    // FILE MANAGEMENT
    // --------------------

    private void chooseImageFromPhone(){
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.this_app_need_permission_to_function), RC_IMAGE_PERMS, PERMS);
            return;
        }
        Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(imageIntent, RC_CHOOSE_PHOTO);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data){
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS
                this.uriImageSelected = data.getData();
                Glide.with(this).load(this.uriImageSelected).into(this.imageViewPreview);
            } else {
                Toast.makeText(this, getString(R.string.no_image_chosen), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // --------------------
    // ERROR HANDLER
    // --------------------

    protected OnFailureListener onFailureListener(){
        return e -> Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
    }

    private void readMessages() {
        Intent intent = getIntent();
        receiverID = intent.getStringExtra("receiver");
        userId = preferences.getString("UserID",null);

        // updating list of messages
        database = FirebaseFirestore.getInstance() ;
        collectionReference = database.collection("chats");
        messages.clear();
        collectionReference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        Message message = documentSnapshot.toObject(Message.class);
                            if ((message.getReceiverID().equals(receiverID))&&(message.getUserId().equals(userId))
                            || (message.getUserId().equals(receiverID))&&(message.getReceiverID().equals(userId))) {
                                messages.add(message);
                            }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}