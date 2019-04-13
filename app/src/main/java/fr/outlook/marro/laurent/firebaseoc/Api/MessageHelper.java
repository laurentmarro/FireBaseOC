package fr.outlook.marro.laurent.firebaseoc.Api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import fr.outlook.marro.laurent.firebaseoc.Models.Message;

public class MessageHelper {

    private static final String COLLECTION_NAME = "messages";

    // --- GET ---
    public static Query getAllMessageForChat(String chat) {
        return ChatHelper.getChatCollection().document(chat).collection(COLLECTION_NAME).orderBy("dateCreated").limit(50);
    }

    // --- CREATE ---
    public static Task<DocumentReference> createMessage(String chat, String userId, String receiverID, String currentTime, String time, String image_sender_url, String image_to_send, String message) {

        // Create the Message object
        Message messageToCreate = new Message(userId, receiverID, currentTime, time, image_sender_url, image_to_send, message);

        // Store Message to Firestore
        return ChatHelper.getChatCollection().document(chat).collection(COLLECTION_NAME).add(messageToCreate);
    }

}