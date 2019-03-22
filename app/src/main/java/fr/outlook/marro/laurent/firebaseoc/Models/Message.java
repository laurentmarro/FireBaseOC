package fr.outlook.marro.laurent.firebaseoc.Models;

public class Message {
    private String userId, receiverID, currentTime, image_receiver_url, image_to_send, message;

    public Message() { }

    public Message(String userId, String receiverID, String currentTime, String image_receiver_url, String image_to_send, String message) {
        this.userId = userId;
        this.receiverID = receiverID;
        this.currentTime = currentTime;
        this.image_receiver_url = image_receiver_url;
        this.image_to_send = image_to_send;
        this.message = message;
    }

    // --- GETTERS ---

    public String getUserId() { return userId; }

    public String getReceiverID() { return receiverID; }

    public String getCurrentTime() { return currentTime; }

    public String getImage_receiver_url() { return image_receiver_url; }

    public String getImage_to_send() { return image_to_send; }

    public String getMessage() { return message; }
}