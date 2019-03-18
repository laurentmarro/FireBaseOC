package fr.outlook.marro.laurent.firebaseoc.Controllers.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import fr.outlook.marro.laurent.firebaseoc.Adapter.FragmentAdapter;
import fr.outlook.marro.laurent.firebaseoc.R;

public class ChatActivity extends AppCompatActivity {

    // --------------------
    // FOR UX
    // --------------------

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.configureViewPagerAndTabs();
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    private void configureToolBar() {
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(false);
    }

    // 2- ConfigureTabLayout and ViewPager
    private void configureViewPagerAndTabs(){

        // Get TabLayout from layout
        tabLayout = findViewById(R.id.activity_home_tabs);
        // Get ViewPager from layout
        viewPager = findViewById(R.id.viewPager);
        //Set Adapter and glue it together
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        // Design purpose. Tabs have the same width
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        // Glue TabLayout and ViewPager together
        tabLayout.setupWithViewPager(viewPager);
    }

}