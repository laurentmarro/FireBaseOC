package fr.outlook.marro.laurent.firebaseoc.Controllers.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.outlook.marro.laurent.firebaseoc.Adapter.UserAdapter;
import fr.outlook.marro.laurent.firebaseoc.Models.User;
import fr.outlook.marro.laurent.firebaseoc.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this); //Configure Butterknife
        this.configureToolBar();
        this.configureRecyclerView();
        this.retrieveAndSave();
        this.readUsersWithoutCurrent();
    }

    // ---------------------
    // DECLARATION
    // ---------------------

    @BindView(R.id.user_recycler_view)
    RecyclerView recyclerView; // Declare RecyclerView
    List<User> users;
    Intent intent;
    UserAdapter adapter;
    FirebaseFirestore database;
    CollectionReference collectionReference;
    Toolbar toolbar;
    SharedPreferences.Editor editor;
    String userId;

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // Configure Toolbar
    private void configureToolBar() {
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.users);
    }

    // Configure RecyclerView
    private void configureRecyclerView(){
        // Reset lists
        this.users = new ArrayList<>();
        // Create adapter passing the list of articles
        this.adapter = new UserAdapter(this.users, Glide.with(this));
        // Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void retrieveAndSave() {
        // userId
        intent = getIntent();
        userId = intent.getStringExtra("userid");

        // Save userId for message Activity
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        editor.putString("UserID",userId);
        editor.apply();

    }

    private void readUsersWithoutCurrent() {
        // updating list of users
        database = FirebaseFirestore.getInstance() ;
        collectionReference = database.collection("users");
        users.clear();
        collectionReference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        User user = documentSnapshot.toObject(User.class);
                        if (!user.getUid().equals(userId)) {
                            users.add(user);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}