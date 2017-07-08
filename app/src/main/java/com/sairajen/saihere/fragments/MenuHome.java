package com.sairajen.saihere.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sairajen.saihere.R;
import com.sairajen.saihere.adapter.MenuAdapter;
import com.sairajen.saihere.model.HomeMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gmonetix
 */
public class MenuHome extends Fragment {

    private View view;
    private RecyclerView recyclerView;

    private List<HomeMenu> menuList;
    private MenuAdapter homeAdapter;

    public MenuHome() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.menu_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        String[] title = getResources().getStringArray(R.array.menu_title);
        String[] subTitle = getResources().getStringArray(R.array.menu_sub_title);
        int[] thumbnail = {R.drawable.menu1,R.drawable.menu2,R.drawable.menu3,R.drawable.menu4,R.drawable.menu5,R.drawable.menu6,
                R.drawable.menu7,R.drawable.menu8,R.drawable.menu3,R.drawable.menu9,R.drawable.menu10,R.drawable.menu11,R.drawable.capps,R.drawable.menu12};

        menuList = new ArrayList<>();

        for (int i=0; i<title.length; i++) {
            HomeMenu menu = new HomeMenu();
            menu.setTitle(title[i]);
            menu.setSubTitle(subTitle[i]);
            menu.setThumbnail(thumbnail[i]);
            menuList.add(menu);
        }

        homeAdapter = new MenuAdapter(getActivity(),menuList);
        recyclerView.setAdapter(homeAdapter);

        return view;
    }

}
