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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
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

        // Update all views alignment depending is current user or not
        this.updateDesignDependingUser(isCurrentUser);
    }

    private void updateDesignDependingUser(Boolean isSender){

        // Image Receiver Position
        RelativeLayout.LayoutParams paramsLayoutHeader = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsLayoutHeader.addRule(isSender ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT);
        this.image_receiver.setLayoutParams(paramsLayoutHeader);

        // Message
        RelativeLayout.LayoutParams paramsLayoutContent = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsLayoutContent.addRule(isSender ? RelativeLayout.LEFT_OF : RelativeLayout.RIGHT_OF, R.id.message_text);
        this.messageTextView.setLayoutParams(paramsLayoutContent);

        // Image Sent Position
        RelativeLayout.LayoutParams paramsImageView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsImageView.addRule(isSender ? RelativeLayout.ALIGN_LEFT : RelativeLayout.ALIGN_RIGHT, R.id.image_receiver);
        this.image_sent.setLayoutParams(paramsImageView);

        this.rootView.requestLayout();
    }
}