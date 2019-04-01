package fr.outlook.marro.laurent.firebaseoc.ViewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    //ROOT VIEW
    @BindView(R.id.rootView) RelativeLayout rootView;
    @BindView(R.id.image_receiver) ImageView image_receiver;
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

        // Check if current user is the sender

            // Get userId from ChatActivity(getString)
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String currentUserId = preferences.getString("UserID", null);

        boolean isCurrentUser = message.getUserId().equals(currentUserId);
        // True : CurrentUser is the sender.
        // False : CurrentUser is the receiver.

        // Var
        String currentTime, image_receiver_url, image_to_send, message_text;

        // Get in Firestore
        currentTime = message.getCurrentTime();
        image_receiver_url = message.getImage_receiver_url();

        image_to_send = message.getImage_to_send();
        message_text = message.getMessage();

        // Update message TextView
        this.messageTextView.setText(message_text);
        this.messageTextView.setTextAlignment(isCurrentUser ? View.TEXT_ALIGNMENT_TEXT_END : View.TEXT_ALIGNMENT_TEXT_START);

        // Update Date
        this.date.setText(currentTime);

        // Update Image of the Receiver
        glide.load(image_receiver_url).apply(RequestOptions.circleCropTransform()).into(image_receiver);

        // Update Image sent
        if (image_to_send != null) {
            glide.load(image_to_send).into(image_sent);
            this.image_sent.setVisibility(View.VISIBLE);
        } else {
            this.image_sent.setVisibility(View.GONE);
        }

        // Update Message Background Color Background
        if (isCurrentUser) {
            messageTextView.setBackgroundColor(R.color.colorSand);
        } else {
            messageTextView.setBackgroundColor(R.color.colorBlue);
        }
    }
}