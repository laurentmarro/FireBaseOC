package fr.outlook.marro.laurent.firebaseoc.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import fr.outlook.marro.laurent.firebaseoc.Controllers.Fragments.MessageFragment;
import fr.outlook.marro.laurent.firebaseoc.Controllers.Fragments.UserFragment;

public class FragmentAdapter extends FragmentPagerAdapter {

    // Default Constructor
    public FragmentAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public int getCount() {
        return (2); // Number of page to show
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: //Page number 1
                return UserFragment.newInstance();
            case 1: //Page number 2
                return MessageFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: //Page number 1
                return "USERS";
            case 1: //Page number 2
                return "MESSAGES";
            default:
                return null;
        }
    }
}