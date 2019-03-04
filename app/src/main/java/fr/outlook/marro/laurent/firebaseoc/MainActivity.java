package fr.outlook.marro.laurent.firebaseoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Collections;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.outlook.marro.laurent.firebaseoc.Api.UserHelper;
import fr.outlook.marro.laurent.firebaseoc.Helpers.HomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this); //Configure Butterknife
        this.updateUX();
    }

    // --------------------
    // FOR DATA
    // --------------------

    // --------------------
    // User
    // --------------------

    private String uid, username, email, urlPicture;

    // --------------------
    // Identifier for Sign-In Activity
    // --------------------

    private static final int RC_SIGN_IN = 123;

    // --------------------
    // FOR DESIGN
    // --------------------

    // --------------------
    // Get Coordinator Layout
    // --------------------

    @BindView(R.id.main_activity_coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.main_activity_button_email) TextView EmailButton;
    @BindView(R.id.main_activity_button_google) TextView GoogleButton;
    @BindView(R.id.main_activity_button_facebook) TextView FacebookButton;

    // --------------------
    // ACTIONS
    // --------------------

    @OnClick(R.id.main_activity_button_email)
    public void onClickEmailButton() {
        // Launch EmailSign-In Activity when user clicked on Email Button
        if(this.isCurrentUserLogged()) {
            this.startHomeActivity();
        } else {
            this.startEmailSignInActivity();
        }
    }

    @OnClick(R.id.main_activity_button_google)
    public void onClickGoogleButton() {
        // Launch GoogleSign-In Activity when user clicked on Email Button
        if(this.isCurrentUserLogged()) {
            this.startHomeActivity();
        } else {
            this.startGoogleSignInActivity();
        }
    }

    @OnClick(R.id.main_activity_button_facebook)
    public void onClickFacebookButton() {
        // Launch GoogleSign-In Activity when user clicked on Email Button
        if(this.isCurrentUserLogged()) {
            this.startHomeActivity();
        } else {
            this.startFacebookSignInActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle SignIn Activities response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
        updateUX();
    }

    // --------------------
    // Update UI when activity is resuming
    // --------------------

    private void updateUX(){
        this.EmailButton.setText(this.isCurrentUserLogged()
                ? getString(R.string.button_login_text_logged) :
                getString(R.string.email));
        this.GoogleButton.setText(this.isCurrentUserLogged()
                ? getString(R.string.button_login_text_logged) :
                getString(R.string.google));
        this.FacebookButton.setText(this.isCurrentUserLogged()
                ? getString(R.string.button_login_text_logged) :
                getString(R.string.facebook));
    }

    // --------------------
    // UI
    // --------------------

    // Show Snack Bar with a message
    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    // --------------------
    // NAVIGATION
    // --------------------

    private void startGoogleSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.food_bowl)
                        .build(),
                RC_SIGN_IN);
    }

    private void startFacebookSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(
                                        new AuthUI.IdpConfig.FacebookBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.food_bowl)
                        .build(),
                RC_SIGN_IN);
    }

    private void startEmailSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Collections.singletonList(
                                        new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.food_bowl)
                        .build(),
                RC_SIGN_IN);
    }

    // --------------------
    // REST REQUEST
    // --------------------

    private void createUserInFirestore(){

        if (this.getCurrentUser() != null){
            uid = this.getCurrentUser().getUid();
            username = this.getCurrentUser().getDisplayName();
            email = this.getCurrentUser().getEmail();
            if (this.getCurrentUser().getPhotoUrl() != null) {
                urlPicture = this.getCurrentUser().getPhotoUrl().toString();
            } else {
                urlPicture = getString(R.string.urlnopicture);
            }
            UserHelper.createUser(uid, username, email, urlPicture).addOnFailureListener(this.onFailureListener());
        }
    }

    // --------------------
    // Launching Home Activity
    // --------------------

    private void startHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    // --------------------
    // UTILS
    // --------------------

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                this.createUserInFirestore();
                showSnackBar(this.coordinatorLayout, getString(R.string.connection_succeed));
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_authentication_canceled));
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_no_internet));
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_unknown_error));
                }
            }
        }
    }

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

    // --------------------
    // ERROR HANDLER
    // --------------------

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }
}