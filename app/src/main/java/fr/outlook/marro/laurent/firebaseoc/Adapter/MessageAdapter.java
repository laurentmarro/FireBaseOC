package fr.outlook.marro.laurent.firebaseoc.Adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import fr.outlook.marro.laurent.firebaseoc.Models.Message;
import fr.outlook.marro.laurent.firebaseoc.R;
import fr.outlook.marro.laurent.firebaseoc.ViewHolder.MessageViewHolder;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageViewHolder> {

    public interface Listener {
        void OnDataChanged();
    }

    //FOR DATA
    private final RequestManager glide;

    // FOR COMMUNICATION
    private Listener callback;

    // CONSTRUCTOR
    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options, RequestManager glide, Listener callback) {
        super(options);
        this.glide = glide;
        this.callback = callback;
    }

    // VIEW HOLDER
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent,false));
    }

    // UPDATE VIEW HOLDER

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message model) {
        holder.updateWithMessages(model, this.glide);
    }

    public void OnDataChanged() {
        super.onDataChanged();
        this.callback.OnDataChanged();
    }
}