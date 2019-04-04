package fr.outlook.marro.laurent.firebaseoc.Models;

public class Message {
    private String userId, receiverID, currentTime, time, image_sender_url, image_to_send, message;

    public Message() { }

    public Message(String userId, String receiverID, String currentTime, String time, String image_sender_url, String image_to_send, String message) {
        this.userId = userId;
        this.receiverID = receiverID;
        this.currentTime = currentTime;
        this.time = time;
        this.image_sender_url = image_sender_url;
        this.image_to_send = image_to_send;
        this.message = message;
    }

    // --- GETTERS ---

    public String getUserId() { return userId; }

    public String getReceiverID() { return receiverID; }

    public String getCurrentTime() { return currentTime; }

    public String getTime() { return time; }

    public String getImage_sender_url() { return image_sender_url; }

    public String getImage_to_send() { return image_to_send; }

    public String getMessage() { return message; }
}