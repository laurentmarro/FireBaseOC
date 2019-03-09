package fr.outlook.marro.laurent.firebaseoc.Controllers.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import fr.outlook.marro.laurent.firebaseoc.Adapter.UserAdapter;
import fr.outlook.marro.laurent.firebaseoc.Models.User;
import fr.outlook.marro.laurent.firebaseoc.R;

public class UserFragment extends Fragment {

    private static final String COLLECTION_NAME = "users";
    private RecyclerView recyclerView;
    UserAdapter userAdapter;
    private List<User> userList;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference documentReference = database.collection(COLLECTION_NAME).document();
    String user_name, photoURL;
    User user = null;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        readUsers();
        return view;
    }

    private void readUsers() {
        userList.clear();

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    user_name = documentSnapshot.getString("username");
                    photoURL = documentSnapshot.getString("photoURL");
                    user.setPhotoURL(photoURL);
                    user.setUsername(user_name);
                    userList.add(user);
                } else {
                    Log.e("TAG", getString(R.string.no_document));
                }
            }
        });

        userAdapter = new UserAdapter(getContext(), userList);
        recyclerView.setAdapter(userAdapter);
    }
}