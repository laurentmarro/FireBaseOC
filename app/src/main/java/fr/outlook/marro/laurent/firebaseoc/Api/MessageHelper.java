package fr.outlook.marro.laurent.firebaseoc.Api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import fr.outlook.marro.laurent.firebaseoc.Models.Message;

public class MessageHelper {

    // --- CREATE ---
    public static Task<DocumentReference> createMessage(String userId, String receiverID, String currentTime, String image_receiver_url, String image_to_send, String message){

        // Create the Message object
        Message messageToCreate = new Message(userId, receiverID, currentTime, image_receiver_url, image_to_send, message);

        // Store Message to Firestore
        return ChatHelper.getChatCollection()
                .add(messageToCreate);
    }
}