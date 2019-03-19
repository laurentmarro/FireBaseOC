package fr.outlook.marro.laurent.firebaseoc.Controllers.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
        this.readUsersWithoutCurrant();
    }

    // ---------------------
    // DECLARATION
    // ---------------------

    @BindView(R.id.user_recycler_view)
    RecyclerView recyclerView; // Declare RecyclerView
    List<User> users;
    UserAdapter adapter;
    FirebaseFirestore database;
    CollectionReference collectionReference;
    Toolbar toolbar;

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    private void configureToolBar() {
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.chat);
    }

    // 1 - Configure RecyclerView
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

    private void readUsersWithoutCurrant() {

        Intent intent = getIntent();
        final String userid = intent.getStringExtra("userid");
        database = FirebaseFirestore.getInstance() ;
        collectionReference = database.collection("users");
        users.clear();
        collectionReference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        User user = documentSnapshot.toObject(User.class);
                        if (!user.getUid().equals(userid)) {
                            users.add(user);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}