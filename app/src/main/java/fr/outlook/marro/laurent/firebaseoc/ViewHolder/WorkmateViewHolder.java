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

public class WorkmateViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.workmates_image) ImageView profile_image;
    @BindView(R.id.doing) TextView username;

    public WorkmateViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithUsers(User user, RequestManager glide) {

        // Var

        String name, photo_url;
        String decided = " n'a pas décidé";

        // Get

        name = user.getUsername()+decided;
        photo_url = user.getphotoURL();

        // Display

        this.username.setText(name);
        glide.load(photo_url).apply(RequestOptions.circleCropTransform()).into(profile_image);
    }
}