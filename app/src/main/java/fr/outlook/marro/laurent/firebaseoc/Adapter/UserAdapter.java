package fr.outlook.marro.laurent.firebaseoc.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.RequestManager;
import java.util.List;
import fr.outlook.marro.laurent.firebaseoc.Controllers.Activities.MessageActivity;
import fr.outlook.marro.laurent.firebaseoc.Models.User;
import fr.outlook.marro.laurent.firebaseoc.R;
import fr.outlook.marro.laurent.firebaseoc.ViewHolder.UserViewHolder;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    // FOR DATA
    private Context context;
    private List<User> users;
    private RequestManager glide;

    // CONSTRUCTOR
    public UserAdapter(Context context, List<User> users, RequestManager glide) {
        this.context = context;
        this.users = users;
        this.glide = glide;
    }

    // VIEW HOLDER
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    // UPDATE VIEW HOLDER
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        viewHolder.updateWithUsers(this.users.get(position), this.glide);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userId", users.get(position).getUid());
                context.startActivity(intent);
            }
        });
    }

    // RETURN THE TOTAL COUNT OF ITEMS
    @Override
    public int getItemCount() {
        return this.users.size();
    }
}