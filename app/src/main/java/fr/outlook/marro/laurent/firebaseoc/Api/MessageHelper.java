package fr.outlook.marro.laurent.firebaseoc.Api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import fr.outlook.marro.laurent.firebaseoc.Models.Message;

public class MessageHelper {

    private static final String COLLECTION_NAME = "chats";
    private static final String DOCUMENT_NAME = "messages";

    // --- COLLECTION REFERENCE ---
    private static CollectionReference getMessagesCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<DocumentReference> createMessage(String userId, String receiverID, String currentTime, String image_receiver_url, String image_to_send, String message){

        // 1 - Create the Message object
        Message messageToCreate = new Message(userId, receiverID, currentTime, image_receiver_url, image_to_send, message);

        // 2 - Store Message to Firestore
        return MessageHelper.getMessagesCollection()
                .document(DOCUMENT_NAME)
                .collection(COLLECTION_NAME)
                .add(messageToCreate);
    }

}