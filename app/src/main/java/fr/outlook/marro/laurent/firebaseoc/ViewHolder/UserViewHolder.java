package fr.outlook.marro.laurent.firebaseoc.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import fr.outlook.marro.laurent.firebaseoc.R;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public TextView username;
    public ImageView profile_image;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        username = itemView.findViewById(R.id.username);
        profile_image = itemView.findViewById(R.id.profile_image);
    }
}
