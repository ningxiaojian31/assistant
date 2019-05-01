package cn.zdxh.assistant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MainFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<String> titleDatas;

    //构造器，传入必须的FragmentManager对象，以及Fragment和Tab
    public MainFragmentAdapter(FragmentManager fm, List<Fragment> mFragments,List<String> titleDatas) {
        super(fm);
        this.mFragments=mFragments;
        this.titleDatas=titleDatas;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleDatas.get(position);
    }
}
