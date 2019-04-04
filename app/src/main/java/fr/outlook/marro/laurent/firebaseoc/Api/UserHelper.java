package fr.outlook.marro.laurent.firebaseoc.Api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import fr.outlook.marro.laurent.firebaseoc.Models.User;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---
    private static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---
    public static Task<Void> createUser(String uid, String username, String email, String photoURL) {
        User userToCreate = new User(uid, username, email, photoURL);
        return UserHelper.getUsersCollection()
                .document(uid)
                .set(userToCreate);
    }
}