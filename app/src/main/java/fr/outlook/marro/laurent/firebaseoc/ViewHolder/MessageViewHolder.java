package fr.outlook.marro.laurent.firebaseoc.ViewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.outlook.marro.laurent.firebaseoc.Models.Message;
import fr.outlook.marro.laurent.firebaseoc.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    //ROOT VIEW
    @BindView(R.id.rootView) RelativeLayout rootView;
    @BindView(R.id.image_sender) ImageView image_receiver;
    @BindView(R.id.message) TextView messageTextView;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.image_sent) ImageView image_sent;

    // FOR DATA
    public Context context;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @SuppressLint("ResourceType")
    public void updateWithMessages(Message message, RequestManager glide) {

        // Var
        String currentTime, image_sender_url, image_to_send, message_text;

        // Get in Firestore
        currentTime = message.getCurrentTime();
        image_sender_url = message.getImage_sender_url();

        image_to_send = message.getImage_to_send();
        message_text = message.getMessage();

        // Update message TextView
        this.messageTextView.setText(message_text);

        // Transform and Update MessageDate
        this.date.setText(currentTime);

        // Update Image of the Receiver
        glide.load(image_sender_url).apply(RequestOptions.circleCropTransform()).into(image_receiver);

        // Update Image sent
        if (image_to_send != null) {
            glide.load(image_to_send).into(image_sent);
            this.image_sent.setVisibility(View.VISIBLE);
        } else {
            this.image_sent.setVisibility(View.GONE);
        }
    }
}