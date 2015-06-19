package com.freetamilebooks.freetamilebooks.leftmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.freetamilebooks.freetamilebooks.R;
import com.freetamilebooks.freetamilebooks.base.BaseFragment;
import com.freetamilebooks.freetamilebooks.utils.DeviceUtils;
import com.freetamilebooks.freetamilebooks.views.AnimatedExpandableListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;

import java.util.ArrayList;

public class LeftMenuFragment extends BaseFragment {

    private LeftMenuAdapter adapter;
    private AnimatedExpandableListView listMenu;
    private int menuType = 1;
    private OnMenuSelectedListener mOnMenuSelectCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_left_menu_fragment, container, false);
        init(view);
        setupEvents();
        setupDefaults();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnMenuSelectCallback = (OnMenuSelectedListener) activity;
    }

    private void init(View view) {
        listMenu = (AnimatedExpandableListView) view.findViewById(R.id.listMenu);
    }

    private void setupDefaults() {
        adapter = new LeftMenuAdapter(getActivity(), menuType);
        listMenu.setAdapter(adapter);
        setValues();
    }

    public void setValues() {
        if (getView() == null) {
            return;
        }
    }

    private void setupEvents() {
        HomeActivity home = (HomeActivity) getActivity();
        home.getSlidingMenu().setOnOpenedListener(new OnOpenedListener() {
            @Override
            public void onOpened() {
                DeviceUtils.hideSoftKeyboard(getActivity());
                setValues();
            }
        });

        listMenu.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                mOnMenuSelectCallback.onMenuSelect(groupPosition, true, true);
                return true;
            }
        });
    }

    public static class LeftMenuItem {
        public String name;
        public ArrayList<LeftMenuItem> subMenuItems;
        public boolean showSection;

        public LeftMenuItem(String name, ArrayList<LeftMenuItem> subMenuItems, boolean showSection) {
            this.name = name;
            this.subMenuItems = subMenuItems;
            this.showSection = showSection;
        }
    }
}
