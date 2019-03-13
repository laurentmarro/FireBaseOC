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
import fr.outlook.marro.laurent.firebaseoc.ViewHolder.UserViewHolder;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    // FOR DATA
    private List<User> users;
    private RequestManager glide;

    // CONSTRUCTOR
    public UserAdapter(List<User> users, RequestManager glide) {
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
    public void onBindViewHolder(@NonNull UserViewHolder viewHolder, int position) {
        viewHolder.updateWithUsers(this.users.get(position), this.glide);

    }

    // RETURN THE TOTAL COUNT OF ITEMS
    @Override
    public int getItemCount() {
        return this.users.size();
    }

    private User getUser(int position){
        return this.users.get(position); }
}