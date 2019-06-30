package tech.shmy.dd_app.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.shmy.dd_app.R;
import tech.shmy.dd_app.activity.SearchActivity;
import tech.shmy.dd_app.defs.BaseFragment;
import tech.shmy.dd_app.fragment.home_tabs.AnimeFragmentTab;
import tech.shmy.dd_app.fragment.home_tabs.EpisodeFragmentTab;
import tech.shmy.dd_app.fragment.home_tabs.IndexFragmentTab;
import tech.shmy.dd_app.fragment.home_tabs.MovieFragmentTab;
import tech.shmy.dd_app.fragment.home_tabs.VarietyFragmentTab;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.viewpagertab)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    ImageView imageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getContext())
                .add("推荐", IndexFragmentTab.class)
                .add("电影", MovieFragmentTab.class)
                .add("电视剧", EpisodeFragmentTab.class)
                .add("综艺", VarietyFragmentTab.class)
                .add("动漫", AnimeFragmentTab.class)
                .create());
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
//        smartTabLayout.getMaxScrollAmount()
    }
    @OnClick(R.id.search)
    void onClick() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        pushActivity(intent);
    }
}
