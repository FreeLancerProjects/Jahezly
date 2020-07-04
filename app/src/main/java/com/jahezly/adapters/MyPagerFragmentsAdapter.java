package com.jahezly.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyPagerFragmentsAdapter extends FragmentPagerAdapter {
    private List<String> titleList;
    private List<Fragment> fragmentList;

    public MyPagerFragmentsAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
    }

    public void createFragments_titles(List<String> titleList,List<Fragment> fragmentList){
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
