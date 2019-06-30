package tech.shmy.dd_app.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import tech.shmy.dd_app.R;
import tech.shmy.dd_app.activity.VideoListActivity;
import tech.shmy.dd_app.adapter.MenuAdapter;
import tech.shmy.dd_app.adapter.MenuContainerAdapter;
import tech.shmy.dd_app.defs.BaseFragment;
import tech.shmy.dd_app.entity.MenuEntity;


public class CategoryFragment extends BaseFragment {
    private List<MenuEntity> menuEntityList = new ArrayList<>();
    private List<MenuEntity> menuContainerEntityList = new ArrayList<>();
    public ListView listView;
    public GridView gridView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        listView = view.findViewById(R.id.menu);
        gridView = view.findViewById(R.id.menu_container);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView.setBackgroundColor(Color.parseColor("#eeeeee"));
//        menuEntityList = new ArrayList<>();
//        menuContainerEntityList = new ArrayList<>();
        menuEntityList.add(new MenuEntity(1, "电影", getMovieMenuEntityListChildren()));
        menuEntityList.add(new MenuEntity(2, "电视剧", getEpisodeMenuEntityListChildren()));
        menuEntityList.add(new MenuEntity(3, "综艺", getVarietyMenuEntityListChildren()));
        menuEntityList.add(new MenuEntity(4, "动漫", getAnimeMenuEntityListChildren()));
        MenuAdapter menuAdapter = new MenuAdapter(getContext(), menuEntityList);
        listView.setAdapter(menuAdapter);
        MenuContainerAdapter menuContainerAdapter = new MenuContainerAdapter(getContext(), menuContainerEntityList);
        gridView.setAdapter(menuContainerAdapter);

        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            MenuEntity menuEntity = menuEntityList.get(i);
            menuContainerEntityList = menuEntity.children;
            menuContainerAdapter.setMenuEntities(menuContainerEntityList);
            menuContainerAdapter.notifyDataSetChanged();
            menuAdapter.setId(menuEntity.id);
            menuAdapter.notifyDataSetChanged();
        });
        gridView.setOnItemClickListener((adapterView, view12, i, l) -> {
            Intent intent = new Intent(getContext(), VideoListActivity.class);
            intent.putExtra("id", menuContainerEntityList.get(i).id);
            intent.putExtra("name", menuContainerEntityList.get(i).name);
            pushActivity(intent);

        });
        MenuEntity initMenuEntity = menuEntityList.get(0);
        menuContainerEntityList = initMenuEntity.children;

        menuAdapter.setId(initMenuEntity.id);
        menuContainerAdapter.setMenuEntities(menuContainerEntityList);
    }

    private List<MenuEntity> getMovieMenuEntityListChildren() {
        List<MenuEntity> menuEntityList = new ArrayList<>();
        menuEntityList.add(new MenuEntity(1, "全部电影"));
        menuEntityList.add(new MenuEntity(5, "动作片"));
        menuEntityList.add(new MenuEntity(6, "喜剧片"));
        menuEntityList.add(new MenuEntity(7, "爱情片"));
        menuEntityList.add(new MenuEntity(8, "科幻片"));
        menuEntityList.add(new MenuEntity(9, "恐怖片"));
        menuEntityList.add(new MenuEntity(10, "剧情片"));
        menuEntityList.add(new MenuEntity(11, "战争片"));
        menuEntityList.add(new MenuEntity(12, "纪录片"));
        menuEntityList.add(new MenuEntity(13, "音乐片"));
        menuEntityList.add(new MenuEntity(14, "微电影"));
        menuEntityList.add(new MenuEntity(15, "伦理片"));
        menuEntityList.add(new MenuEntity(16, "福利片"));
        return menuEntityList;
    }

    private List<MenuEntity> getEpisodeMenuEntityListChildren() {
        List<MenuEntity> menuEntityList = new ArrayList<>();
        menuEntityList.add(new MenuEntity(2, "全部电视剧"));
        menuEntityList.add(new MenuEntity(17, "国产剧"));
        menuEntityList.add(new MenuEntity(18, "香港剧"));
        menuEntityList.add(new MenuEntity(19, "台湾剧"));
        menuEntityList.add(new MenuEntity(20, "韩国剧"));
        menuEntityList.add(new MenuEntity(21, "日本剧"));
        menuEntityList.add(new MenuEntity(22, "欧美剧"));
        menuEntityList.add(new MenuEntity(23, "海外剧"));
        return menuEntityList;
    }

    private List<MenuEntity> getVarietyMenuEntityListChildren() {
        List<MenuEntity> menuEntityList = new ArrayList<>();
        menuEntityList.add(new MenuEntity(3, "全部综艺"));
        return menuEntityList;
    }

    private List<MenuEntity> getAnimeMenuEntityListChildren() {
        List<MenuEntity> menuEntityList = new ArrayList<>();
        menuEntityList.add(new MenuEntity(4, "全部动漫"));
        return menuEntityList;
    }
}
