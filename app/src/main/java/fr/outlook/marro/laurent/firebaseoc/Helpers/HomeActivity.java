package fr.outlook.marro.laurent.firebaseoc.Helpers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.Objects;
import fr.outlook.marro.laurent.firebaseoc.R;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {


    // --------------------
    //FOR UX
    // --------------------

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

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

    // --------------------
    // UX
    // --------------------

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
            case R.id.activity_home_search:
                Log.i("TAG", "Search");
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

    private void updateUI() {
        // Display in the drawerLayout : nav_header_home
    }


}