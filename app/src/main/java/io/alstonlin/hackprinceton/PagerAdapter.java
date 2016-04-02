package io.alstonlin.hackprinceton;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * The Adapter for MainActivity to handle all the Fragments.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_TABS = 2;
    private MainActivity activity;


    public PagerAdapter(FragmentManager fm, MainActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CameraFragment cam = CameraFragment.newInstance(activity);
                return cam;
            case 1:
                HistoryFragment data = HistoryFragment.newInstance(activity);
                return data;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }
}