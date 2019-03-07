package fr.outlook.marro.laurent.firebaseoc.Controllers.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import java.util.Objects;
import fr.outlook.marro.laurent.firebaseoc.Adapter.ViewPagerAdapter;
import fr.outlook.marro.laurent.firebaseoc.Controllers.Fragments.ChatFragment;
import fr.outlook.marro.laurent.firebaseoc.Controllers.Fragments.UserFragment;
import fr.outlook.marro.laurent.firebaseoc.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.configureToolBar();
        this.configureTabLayout();
        this.configureViewPager();
        this.configureViewPagerAdapter();
    }

    // --------------------
    // FOR UX
    // --------------------

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    private void configureToolBar() {
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.chat);
    }

    // 2 - Configure TabLayout
    private void configureTabLayout() {
        this.tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    // 3 - Configure TabLayout
    private void configureViewPager() {
        this.viewPager = findViewById(R.id.view_pager);
    }

    // 4 - Configure ViewPagerAdapter
    private void configureViewPagerAdapter() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ChatFragment(), String.valueOf(R.string.chat));
        viewPagerAdapter.addFragment(new UserFragment(), String.valueOf(R.string.users));

        viewPager.setAdapter(viewPagerAdapter);

    }
}