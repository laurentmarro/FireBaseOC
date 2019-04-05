package fr.outlook.marro.laurent.firebaseoc.Controllers.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.outlook.marro.laurent.firebaseoc.Adapter.WorkmateAdapter;
import fr.outlook.marro.laurent.firebaseoc.Models.User;
import fr.outlook.marro.laurent.firebaseoc.R;

public class WorkmateFragment extends Fragment {

    public WorkmateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workmate, container, false);
        ButterKnife.bind(this, view);
        this.configureRecyclerView();
        this.readUsersWithoutCurrent();
        return view;
    }

    // ---------------------
    // DECLARATION
    // ---------------------

    @BindView(R.id.fragment_workmate_recycler_view) RecyclerView recyclerView; // Declare RecyclerView
    List<User> users;
    WorkmateAdapter adapter;
    FirebaseFirestore database;
    CollectionReference collectionReference;
    String userId;

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // Configure RecyclerView
    private void configureRecyclerView(){
        // Reset lists
        this.users = new ArrayList<>();
        // Create adapter passing the list of articles
        this.adapter = new WorkmateAdapter(this.users, Glide.with(this));
        // Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void readUsersWithoutCurrent() {
        userId = getCurrentUser().getUid();
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

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
}