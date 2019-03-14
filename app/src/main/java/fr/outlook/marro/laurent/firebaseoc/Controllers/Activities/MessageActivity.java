package fr.outlook.marro.laurent.firebaseoc.Controllers.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;
import fr.outlook.marro.laurent.firebaseoc.R;

public class MessageActivity extends AppCompatActivity {

    // UX
    private ImageView imageView;
    private TextView username;
    Toolbar toolbar;

    // UI - DATA
    FirebaseFirestore database;
    Intent intent;
    String userID, name, url;
    DocumentReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        this.configureToolBar();
        this.configureViews();
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

        reference = database.collection("users").document(userID);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    name = documentSnapshot.getString("username");
                    url = documentSnapshot.getString("photoURL");
                    username = findViewById(R.id.username_message);
                    imageView = findViewById(R.id.profile_image_message);
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
}