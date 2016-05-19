package com.liangfeizc.slidepager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangfeizc on 3/26/15.
 */
public class SlidePagerAdapter extends FragmentStatePagerAdapter {
    private List<String> picList = new ArrayList<>();

    public SlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return SlidePageFragment.newInstance(picList.get(i));
    }

    @Override
    public int getCount() {
        return picList.size();
    }
    
    public void addAll(List<String> picList) {
        this.picList = picList;
    }
    
    public void remove(ViewPager pager, int index){
        if(picList.size()>index){
            pager.setAdapter (null);
            destroyItem(pager,index,getItem(index));
            picList.remove(index);
            pager.setAdapter (this);
        }
    }
}
