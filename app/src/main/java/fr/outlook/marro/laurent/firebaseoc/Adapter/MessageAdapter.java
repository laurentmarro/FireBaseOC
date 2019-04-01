package fr.outlook.marro.laurent.firebaseoc.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.RequestManager;
import java.util.List;
import fr.outlook.marro.laurent.firebaseoc.Models.Message;
import fr.outlook.marro.laurent.firebaseoc.R;
import fr.outlook.marro.laurent.firebaseoc.ViewHolder.MessageViewHolder;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    //FOR DATA
    private List<Message> messages;
    private final RequestManager glide;
    public Context context;

    // CONSTRUCTOR
    public MessageAdapter(List<Message> messages, RequestManager glide) {
        this.messages = messages;
        this.glide = glide;
    }

    // VIEW HOLDER
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    // UPDATE VIEW HOLDER
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder viewHolder, int position) {
        viewHolder.updateWithMessages(this.messages.get(position), this.glide);
    }

    // RETURN THE TOTAL COUNT OF ITEMS
    @Override
    public int getItemCount() {
        return this.messages.size();
    }
}