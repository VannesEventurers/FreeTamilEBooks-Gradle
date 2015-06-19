package com.freetamilebooks.freetamilebooks.leftmenu;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.freetamilebooks.freetamilebooks.R;
import com.freetamilebooks.freetamilebooks.app.FTEApp;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class SlidingMenuActivity extends SlidingFragmentActivity {

    private LeftMenuFragment leftMenuFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame);
        setBehindContentView(R.layout.home_menu_frame);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            leftMenuFragment = new LeftMenuFragment();
            transaction.replace(R.id.menu_frame, leftMenuFragment);
            transaction.commit();
        } else {
            leftMenuFragment = (LeftMenuFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }

        SlidingMenu menu = getSlidingMenu();
        menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setFadeDegree(0.35f);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
    }

    protected FTEApp getApp() {
        return (FTEApp) getApplication();
    }

    protected LeftMenuFragment getLeftMenu() {
        return leftMenuFragment;
    }
}
