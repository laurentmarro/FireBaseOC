package fr.outlook.marro.laurent.firebaseoc.Helpers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;
import butterknife.BindView;
import fr.outlook.marro.laurent.firebaseoc.Auth.ProfileActivity;
import fr.outlook.marro.laurent.firebaseoc.R;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {


    //FOR DESIGN
    @BindView(R.id.connected_image) ImageView imageViewProfile;
    @BindView(R.id.surnameName) TextView textViewUsername;
    @BindView(R.id.surnameNameEmail) TextView textViewEmail;

    //FOR UX
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    String[] PARAMETERS = {"I'm Hungry !", "I'm Hungry !", "Available Workmates"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomNavigationView();
        this.configureProfile();
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

    //
    // UX
    //

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
                // ACTIVITY TO DO
                break;
            case R.id.activity_settings:
                // ACTIVITY TO DO
                break;
            case R.id.activity_logout:
                // ACTIVITY TO DO
                break;
            case R.id.mapview:
                // ACTIVITY TO DO
                toolbar.setTitle(PARAMETERS[0]);
                break;
            case R.id.listview:
                toolbar.setTitle(PARAMETERS[1]);
                // ACTIVITY TO DO
                break;
            case R.id.workmates:
                // ACTIVITY TO DO
                toolbar.setTitle(PARAMETERS[2]);
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
        Objects.requireNonNull(getSupportActionBar()).setTitle(PARAMETERS[0]);
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
    // UTILS
    // --------------------

    @Nullable
    protected FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }
    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

    // --------------------
    // UI
    // --------------------

    // Update UI when activity is creating
    private void configureProfile(){
        Intent myIntent = new Intent(this, ProfileActivity.class);
        startActivity(myIntent);
    }
}