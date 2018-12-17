package com.hippo.jun.weandseoul;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hippo.jun.weandseoul.InsideTabFragment1;
import com.hippo.jun.weandseoul.InsideTabFragment2;
import com.hippo.jun.weandseoul.InsideTabFragment3;

/**
 * Created by JUN on 2018-09-19.
 */

public class InsideTabPagerAdapter extends FragmentStatePagerAdapter {

    //Count number of tabs
    private int tabCount;

    public InsideTabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        //Returning the current tabs
        switch (position){
            case 0:
                return new InsideTabFragment1();
            case 1:
                return new InsideTabFragment2();
            case 2:
                return new InsideTabFragment3();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return tabCount;
    }
}