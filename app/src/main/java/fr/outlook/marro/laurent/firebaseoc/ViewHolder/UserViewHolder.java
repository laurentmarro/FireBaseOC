package fr.outlook.marro.laurent.firebaseoc.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.bumptech.glide.RequestManager;
import butterknife.ButterKnife;
import fr.outlook.marro.laurent.firebaseoc.Models.User;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithUsers(User user, RequestManager glide) {
    }
}