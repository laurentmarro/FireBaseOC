package fr.outlook.marro.laurent.firebaseoc;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.widget.TextView;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import java.util.Collections;
import java.util.Objects;
import butterknife.BindView;
import butterknife.OnClick;
import fr.outlook.marro.laurent.firebaseoc.Base.BaseActivity;
import fr.outlook.marro.laurent.firebaseoc.Helpers.HomeActivity;

public class MainActivity extends BaseActivity {

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_main;
    }

    // FOR DATA

    // Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;

    // FOR DESIGN

    // Get Coordinator Layout
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.updateUIWhenResuming();
    }

    // Update UI when activity is resuming
    private void updateUIWhenResuming(){
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
    // UTILS
    // --------------------

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
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

    // Launching Home Activity
    private void startHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}