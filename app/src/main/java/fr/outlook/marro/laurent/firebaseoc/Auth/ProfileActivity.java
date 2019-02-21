package fr.outlook.marro.laurent.firebaseoc.Auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import butterknife.BindView;
import butterknife.OnClick;
import fr.outlook.marro.laurent.firebaseoc.Api.UserHelper;
import fr.outlook.marro.laurent.firebaseoc.Base.BaseActivity;
import fr.outlook.marro.laurent.firebaseoc.R;
import fr.outlook.marro.laurent.firebaseoc.models.User;

public class ProfileActivity extends BaseActivity {

    //FOR DESIGN
    @BindView(R.id.connected_image)
    ImageView imageViewProfile;
    @BindView(R.id.surnameName)
    TextView textViewUsername;
    @BindView(R.id.surnameNameEmail)
    TextView textViewEmail;
    @BindView(R.id.activity_logout)
    MenuItem menuItem;

    //FOR DATA
    private static final int SIGN_OUT_TASK = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.updateUIWhenCreating();
    }

    @Override
    public int getFragmentLayout() { return 0; }

    // --------------------
    // ACTIONS
    // --------------------

    @OnClick(R.id.activity_logout)
    public void onClickSignOutButton() { this.signOutUserFromFirebase(); }

    // --------------------
    // REST REQUESTS
    // --------------------

    private void signOutUserFromFirebase(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }


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
            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ?
                    getString(R.string.surname_name_email) : this.getCurrentUser().getEmail();

            //Update views with data
            this.textViewEmail.setText(email);

            // 5 - Get additional data from Firestore
            UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User currentUser = documentSnapshot.toObject(User.class);
                    String username = TextUtils.isEmpty(currentUser.getUsername()) ?
                            getString(R.string.Surname_Name) : currentUser.getUsername();
                    textViewUsername.setText(username);
                }
            });
        }
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin){
                    case SIGN_OUT_TASK:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
