package fr.outlook.marro.laurent.firebaseoc.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.outlook.marro.laurent.firebaseoc.Models.Message;
import fr.outlook.marro.laurent.firebaseoc.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_receiver)
    ImageView image_receiver;
    @BindView(R.id.message)
    TextView message_text;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.image_send)
    ImageView image_send;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithMessages(Message message, RequestManager glide) {

        // Var
        String currentTime, image_receiver_url, image_to_send, message_text, userId, receiverID;

        // Get in Firestore
        userId = message.getUserId();
        receiverID = message.getReceiverID();
        currentTime = message.getCurrentTime();
        image_receiver_url = message.getImage_receiver_url();
        image_to_send = message.getImage_to_send();
        message_text = message.getMessage();

        // Display
        this.date.setText(currentTime);
        this.message_text.setText(message_text);
        glide.load(image_receiver_url).apply(RequestOptions.circleCropTransform()).into(image_receiver);
        glide.load(image_to_send).into(image_send);
    }
}