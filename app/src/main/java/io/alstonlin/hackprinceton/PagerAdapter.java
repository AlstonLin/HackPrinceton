package io.alstonlin.hackprinceton;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * The Adapter for MainActivity to handle all the Fragments.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_TABS = 3;
    private MainActivity activity;


    public PagerAdapter(FragmentManager manager, MainActivity activity) {
        super(manager);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return CameraFragment.newInstance(activity);
            case 1:
                return HistoryFragment.newInstance(activity);
            case 2:
                return VisualizeFragment.newInstance(activity);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }
}