package fr.outlook.marro.laurent.firebaseoc.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.RequestManager;
import java.util.List;
import fr.outlook.marro.laurent.firebaseoc.Models.User;
import fr.outlook.marro.laurent.firebaseoc.R;
import fr.outlook.marro.laurent.firebaseoc.ViewHolder.WorkmateViewHolder;

public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateViewHolder> {

    // FOR DATA
    private List<User> users;
    private RequestManager glide;

    // CONSTRUCTOR
    public WorkmateAdapter(List<User> users, RequestManager glide) {
        this.users = users;
        this.glide = glide;
    }

    // VIEW HOLDER
    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context;
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workmate_item, parent, false);
        return new WorkmateViewHolder(view);
    }

    // UPDATE VIEW HOLDER
    @Override
    public void onBindViewHolder(@NonNull WorkmateViewHolder viewHolder, int position) {
        viewHolder.updateWithUsers(this.users.get(position), this.glide);

        // Click to access to restaurant

//        viewHolder.itemView.setOnClickListener(view -> {
//            Intent messageIntent = new Intent(context, MessageActivity.class);
//            messageIntent.putExtra("receiver", users.get(position).getUid());
//            context.startActivity(messageIntent);
//        });
    }

    // RETURN THE TOTAL COUNT OF ITEMS
    @Override
    public int getItemCount() {
        return this.users.size();
    }

}