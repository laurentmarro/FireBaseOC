package fr.outlook.marro.laurent.firebaseoc.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList <Fragment> fragments;
    private ArrayList<String> titles;

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.fragments = new ArrayList<>();
        this.titles = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment (Fragment fragment, String title){
        fragments.add(fragment);
        titles.add(title);
    }

    public CharSequence getPageTitle (int position) {
        return titles.get(position);
    }
}