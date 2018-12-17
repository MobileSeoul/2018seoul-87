package com.hippo.jun.weandseoul;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.hippo.jun.weandseoul.TabFragment1;
import com.hippo.jun.weandseoul.TabFragment2;
import com.hippo.jun.weandseoul.TabFragment3;
import com.hippo.jun.weandseoul.TabFragment4;
import com.hippo.jun.weandseoul.TabFragment5;

/**
 * Created by JUN on 2018-07-16.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                return new TabFragment1();
            case 1:
                return new TabFragment2();
            case 2:
                return new TabFragment3();
            case 3:
                return new TabFragment4();
            case 4:
                return new TabFragment5();
            default:
                return null;
        }
    }
    @Override
    public int getItemPosition(Object object) {
//        Log.d("Test","getItemPosition :: "+(object instanceof TabFragment1));
        if (object instanceof TabFragment1) {
            return POSITION_NONE;
        } else {
            return super.getItemPosition(object); //POSITION_UNCHANGED 리턴함.
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
