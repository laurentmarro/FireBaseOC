package fr.outlook.marro.laurent.firebaseoc.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.outlook.marro.laurent.firebaseoc.Models.User;
import fr.outlook.marro.laurent.firebaseoc.R;

public class UserViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.username) TextView username;
    @BindView(R.id.profile_image) ImageView profile_image;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithUsers(User user, RequestManager glide) {

        // Var

        String name, photo_url;

        // Get

        name = user.getUsername ();
        photo_url = user.getphotoURL();

        // Display

        this.username.setText(name);
        glide.load(photo_url).apply(RequestOptions.circleCropTransform()).into(profile_image);
    }
}