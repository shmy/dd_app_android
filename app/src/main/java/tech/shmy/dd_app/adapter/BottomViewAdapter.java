package tech.shmy.dd_app.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class BottomViewAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public BottomViewAdapter(FragmentManager manager, List<Fragment> fragments) {
        super(manager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}