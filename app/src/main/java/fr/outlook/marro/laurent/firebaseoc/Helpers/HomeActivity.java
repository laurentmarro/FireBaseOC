package fr.outlook.marro.laurent.firebaseoc.Helpers;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.Objects;
import fr.outlook.marro.laurent.firebaseoc.Api.UserHelper;
import fr.outlook.marro.laurent.firebaseoc.R;
import fr.outlook.marro.laurent.firebaseoc.models.User;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    // --------------------
    // FOR UX
    // --------------------

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private String userName, photoUrl, email;
    private ImageView imageViewProfile;
    private TextView textViewUsername, textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomNavigationView();
        this.updateUI();
    }

    @Override
    public void onBackPressed() {
        // Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_home, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle Navigation Item Click
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.activity_restaurant:
                Log.i("TAG", "Activity Restaurant");
                // ACTIVITY TO DO
                break;
            case R.id.activity_settings:
                Log.i("TAG", "Activity Settings");
                // ACTIVITY TO DO
                break;
            case R.id.activity_logout:
                Log.i("TAG", "Activity Logout");
                // ACTIVITY TO DO
                break;
            case R.id.mapview:
                // ACTIVITY TO DO
                Log.i("TAG", "Mapview");
                toolbar.setTitle(R.string.im_hungry);
                break;
            case R.id.listview:
                Log.i("TAG", "Listview");
                toolbar.setTitle(R.string.im_hungry);
                // ACTIVITY TO DO
                break;
            case R.id.workmates:
                // ACTIVITY TO DO
                Log.i("TAG", "workmates");
                toolbar.setTitle(R.string.available_workmates);
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    private void configureToolBar() {
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.im_hungry);
    }

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView() {
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // 4 - Configure BottomNavigationView
    private void configureBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_home_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    // --------------------
    // UI
    // --------------------

    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    @SuppressLint("ResourceType")
    private void updateUI() {
        // Display in the drawerLayout : nav_header_home
        NavigationView navigationView = findViewById(R.id.navigationView);
        View navigationViewHeaderView =  navigationView.getHeaderView(0);

        textViewUsername = navigationViewHeaderView.findViewById(R.id.surnameName);
        textViewUsername.setText(userName);
        textViewEmail = navigationViewHeaderView.findViewById(R.id.surnameNameEmail);
        textViewEmail.setText(email);
        imageViewProfile = navigationViewHeaderView.findViewById(R.id.connected_image);

        // Get datas from firebase

        if (this.getCurrentUser() != null) {
            email = TextUtils.isEmpty(this.getCurrentUser().getEmail())
                    ? getString(R.string.surname_name_email) : this.getCurrentUser().getEmail();

            this.textViewEmail.setText(email);

            UserHelper.getUser(Objects.requireNonNull(this.getCurrentUser()).getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User currentUser = documentSnapshot.toObject(User.class);
                    userName = TextUtils.isEmpty(currentUser.getUsername()) ? getString(R.string.Surname_Name) : currentUser.getUsername();
                    textViewUsername.setText(userName);
                }
            });

            Uri photo = this.getCurrentUser().getPhotoUrl();
            if (photo != null) {
                photoUrl = String.valueOf(this.getCurrentUser().getPhotoUrl());
                Glide.with(this)
                        .load(photoUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageViewProfile);
            }
        }
    }

}