package com.joseantonio.foursquarenewclient.app.adapters;

/**
 * Created by josetorres on 13/05/14.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    public SectionsPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {//Método que devolverá el número de fragments:
        // Show 2 total pages(ListFragment and MapPlaces)
        return 2;
    }

    //Títulos de la parte de ViewIndicator en la parte superior:
    @Override
    public CharSequence getPageTitle(int position) {
        //Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "List";
            case 1:
                return "Map";
        }
        return null;
    }
}
