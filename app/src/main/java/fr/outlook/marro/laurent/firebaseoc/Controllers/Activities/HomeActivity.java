package fr.outlook.marro.laurent.firebaseoc.Controllers.Activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;
import fr.outlook.marro.laurent.firebaseoc.Controllers.Fragments.ListFragment;
import fr.outlook.marro.laurent.firebaseoc.Controllers.Fragments.MapFragment;
import fr.outlook.marro.laurent.firebaseoc.Controllers.Fragments.WorkmateFragment;
import fr.outlook.marro.laurent.firebaseoc.R;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener{

    // --------------------
    // FOR UX
    // --------------------

    private static final int SIGN_OUT_TASK = 10;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    String userName, email, photoUrl;
    ImageView imageViewProfile;
    TextView textViewUsername, textViewEmail;
    private SearchView searchView;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomNavigationView();
        this.configureFirstFragment();
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
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_home, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        // Expanding the search view
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);

        // Code for changing the textcolor and hint color for the search view

        SearchView.SearchAutoComplete searchAutoComplete =
                searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.GRAY);
        searchView.setBackgroundColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.BLACK);

        // Code for changing the search icon
        ImageView searchIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        searchIcon.setImageResource(R.drawable.baseline_search_black_24dp);

        // Code for changing the voice search icon
        ImageView voiceIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_voice_btn);
        voiceIcon.setImageResource(R.drawable.baseline_mic_black_24dp);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("TAG", "onQueryTextSubmit: "+query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("TAG", "onQueryTextChange: "+newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle Navigation Item Click
        Fragment selectedFragment = new MapFragment();
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.activity_restaurant:
                Log.i("TAG", "Activity Restaurant");
                // ACTIVITY TO DO
                break;
            case R.id.activity_chat:
                startChatActivity();
                break;
            case R.id.activity_settings:
                Log.i("TAG", "Activity Settings");
                // ACTIVITY TO DO
                break;
            case R.id.activity_logout:
                signOutUserFromFirebase();
                break;
            case R.id.mapview:
                toolbar.setTitle(R.string.im_hungry);
                searchView.setQueryHint(getString(R.string.searchRestaurants));
                selectedFragment = new MapFragment();
                break;
            case R.id.listview:
                toolbar.setTitle(R.string.im_hungry);
                searchView.setQueryHint(getString(R.string.searchRestaurants));
                selectedFragment = new ListFragment();
                break;
            case R.id.workmates:
                toolbar.setTitle(R.string.available_workmates);
                searchView.setQueryHint(getString(R.string.searchWorkmates));
                selectedFragment = new WorkmateFragment();
                break;
            default:
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
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

    // 5- Configure First Fragment
    private void configureFirstFragment(){
        Fragment selectedFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }

    // --------------------
    // UI
    // --------------------

    protected FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser(); }

    @SuppressLint("ResourceType")
    private void updateUI() {

        // Display in the drawerLayout : nav_header_home
        NavigationView navigationView = findViewById(R.id.navigationView);
        View navigationViewHeaderView =  navigationView.getHeaderView(0);
        textViewUsername = navigationViewHeaderView.findViewById(R.id.surnameName);
        textViewEmail = navigationViewHeaderView.findViewById(R.id.surnameNameEmail);
        imageViewProfile = navigationViewHeaderView.findViewById(R.id.connected_image);

        // Get Data from FIREBASE or default
        if (this.getCurrentUser() != null) {
            userName = this.getCurrentUser().getDisplayName();
            email = this.getCurrentUser().getEmail();
        } else {
            userName = getString(R.string.Surname_Name);
            email = getString(R.string.surname_name_email);
        }

        // Get Picture from Firebase or default
        Uri photo = this.getCurrentUser().getPhotoUrl();
        if (photo == null) {
            photoUrl = getString(R.string.urlnopicture);
        } else {
            photoUrl = String.valueOf(photo);
        }

        // Save SenderPhotoUrl for message Activity

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        editor.putString("sender_photo_url",photoUrl);
        editor.apply();

        // DISPLAY
        textViewUsername.setText(userName);
        textViewEmail.setText(email);
        Glide.with(this)
                .load(photoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(imageViewProfile);
    }

    private void signOutUserFromFirebase(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted());
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(){
        return aVoid -> {
            switch (HomeActivity.SIGN_OUT_TASK){
                case SIGN_OUT_TASK:
                    startMainActivity();
                    break;
                default:
                    break;
            }
        };
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startChatActivity(){
        Intent chatIntent = new Intent(this, ChatActivity.class);
        chatIntent.putExtra("userid", this.getCurrentUser().getUid());
        startActivity(chatIntent);
    }
}