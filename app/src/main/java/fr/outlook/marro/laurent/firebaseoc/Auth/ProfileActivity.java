package fr.outlook.marro.laurent.firebaseoc.Auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import butterknife.BindView;
import fr.outlook.marro.laurent.firebaseoc.Api.UserHelper;
import fr.outlook.marro.laurent.firebaseoc.Base.BaseActivity;
import fr.outlook.marro.laurent.firebaseoc.R;
import fr.outlook.marro.laurent.firebaseoc.models.User;

public class ProfileActivity extends BaseActivity {

    // --------------------
    //FOR DESIGN
    // --------------------

    @BindView(R.id.connected_image) ImageView imageViewProfile;
    @BindView(R.id.surnameName) TextView textViewUsername;
    @BindView(R.id.surnameNameEmail) TextView textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.updateUIWhenCreating();
    }

    @Override
    public int getFragmentLayout() { return R.layout.nav_header_home; }

    // --------------------
    // UI
    // --------------------

    private void updateUIWhenCreating(){

        if (this.getCurrentUser() != null){

            //Get picture URL from Firebase
            if (this.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageViewProfile);
            }

            //Get email & username from Firebase
            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.surname_name_email) : this.getCurrentUser().getEmail();

            //Update views with data
            this.textViewEmail.setText(email);

            // 5 - Get additional data from Firestore
            UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User currentUser = documentSnapshot.toObject(User.class);
                    String username = TextUtils.isEmpty(currentUser.getUsername()) ? getString(R.string.Surname_Name) : currentUser.getUsername();
                    textViewUsername.setText(username);
                }
            });
        }
    }
}