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
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

public class MessageActivity extends AppCompatActivity implements MessageAdapter.Listener {

    // ---------------------
    // FOR DESIGN
    // ---------------------

    @BindView(R.id.message_recycler_view) RecyclerView recyclerView; // Declare RecyclerView
    @BindView(R.id.message_text) EditText message_text;
    @BindView(R.id.image_preview) ImageView imageViewPreview;

    // ---------------------
    // FOR DATA
    // ---------------------

    private MessageAdapter adapter;
    private String image_to_send = "";
    String userId, receiverID, currentTime, image_receiver_url, message;
    private SharedPreferences preferences;
    Toolbar toolbar;
    Date date;
    private Uri uriImageSelected;

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
        this.configureRecyclerView();
        this.configureToolBar();
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

    // Configure RecyclerView with a Query
    private void configureRecyclerView(){
        //Configure Adapter & RecyclerView
        this.adapter = new MessageAdapter(generateOptionsForAdapter(MessageHelper.getAllMessage()),
                Glide.with(this), this);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.smoothScrollToPosition(adapter.getItemCount()); // Scroll to bottom on new messages
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.adapter);
    }

    // Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();
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

    // --------------------
    // CALLBACK
    // --------------------

    @Override
    public void onDataChanged() {
        Log.i("TAG", "onDataChanged: " + this.adapter.getItemCount());
    }
}