package com.tysci.cls.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2015/11/27.
 */
public class CustomFragmentPagerAdapter<T extends Fragment> extends FragmentStatePagerAdapter {
    private List<T> fragments;
    private String[] titles=null;

    public CustomFragmentPagerAdapter(FragmentManager fm, List<T> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public CustomFragmentPagerAdapter(FragmentManager fm, String[] titles, List<T> fragments){
        super(fm);
        this.titles=titles;
        this.fragments=fragments;
    }

    @Override
    public T getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position>=0&&position<titles.length){
            return titles[position];
        }else {
            return super.getPageTitle(position);
        }
    }
}
