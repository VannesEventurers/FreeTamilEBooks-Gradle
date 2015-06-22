package com.freetamilebooks.freetamilebooks.leftmenu;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;

import com.freetamilebooks.freetamilebooks.R;
import com.freetamilebooks.freetamilebooks.base.BaseFragment;
import com.freetamilebooks.freetamilebooks.downloaded.DownloadsFragment;
import com.freetamilebooks.freetamilebooks.home.HomeFragment;
import com.freetamilebooks.freetamilebooks.others.AboutUsFragment;
import com.freetamilebooks.freetamilebooks.others.CommentsFragment;
import com.freetamilebooks.freetamilebooks.others.ContributorsFragment;
import com.freetamilebooks.freetamilebooks.others.HelpUsFragment;
import com.freetamilebooks.freetamilebooks.utils.AlertUtils;
import com.freetamilebooks.freetamilebooks.utils.DeviceUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class HomeActivity extends SlidingMenuActivity implements OnMenuSelectedListener {

    public static final String CURRENT_FRAGMENT = "com.iandmydoctor.android.leftmenu.CURRENT_FRAGMENT";

    private BaseFragment fragment;
    private SlidingMenu.OnOpenListener onOpenListener = new SlidingMenu.OnOpenListener() {
        @Override
        public void onOpen() {
            if (fragment instanceof BaseFragment) {
                ((BaseFragment) fragment).onFragmentClosed();
            }
        }
    };

    private int mCurrentMenu = -1;
    private boolean mUpdateFragmentUI = false;
    private SlidingMenu.OnClosedListener onClosedListener = new SlidingMenu.OnClosedListener() {
        @Override
        public void onClosed() {
            if (fragment != null && mUpdateFragmentUI) {
                if (fragment instanceof BaseFragment) {
                    ((BaseFragment) fragment).onFragmentOpened();
                }
                mUpdateFragmentUI = false;
            }
        }
    };
    private boolean isExitPressed = false;

    @Override
    protected void onResume() {
        super.onResume();
        this.isExitPressed = false;
    }

    @Override
    public void onBackPressed() {
        if (isExitPressed) {
            super.onBackPressed();
            return;
        }
        this.isExitPressed = true;
        AlertUtils.showToast(this, getString(R.string.press_again_to_exit));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isExitPressed = false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame);

        if (DeviceUtils.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        int currentMenu = 0;
        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_FRAGMENT)) {
            currentMenu = savedInstanceState.getInt(CURRENT_FRAGMENT);
        }

        Bundle args = new Bundle();
        if (getIntent() != null) {
            currentMenu = getIntent().getIntExtra(CURRENT_FRAGMENT, currentMenu);
        }

        selectMenu(currentMenu, false, false, true, args);

        getSlidingMenu().setOnClosedListener(onClosedListener);
        getSlidingMenu().setOnOpenListener(onOpenListener);
    }

    public void selectMenu(int menu, boolean checkCurrentFragment, boolean isToggle, boolean refreshOnCreate, Bundle args) {
        BaseFragment f = null;

        switch (menu) {
            case 0:
                f = new HomeFragment();
                break;

            case 1:
                f = new AboutUsFragment();
                break;

            case 2:
                f = new ContributorsFragment();
                break;

            case 3:
                f = new HelpUsFragment();
                break;

            case 4:
                f = new CommentsFragment();
                break;

            case 5:
                f = new DownloadsFragment();
                break;

            default:
                break;
        }


        if (f != null) {
            f.setArguments(args);
            addFragment(f, menu, checkCurrentFragment, isToggle);
        }
    }

    private void addFragment(BaseFragment fragment, int menu, boolean checkCurrentFragment, boolean isToggle) {
        if (checkCurrentFragment) {
            if (fragment != null && menu != mCurrentMenu) {
                mCurrentMenu = menu;
                this.fragment = fragment;
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.replace(R.id.content_frame, fragment);
                t.commit();
                mUpdateFragmentUI = true;
            }
        } else {
            if (fragment != null) {
                mCurrentMenu = menu;
                this.fragment = fragment;
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.replace(R.id.content_frame, fragment);
                t.commit();
                mUpdateFragmentUI = true;
            }
        }

        if (isToggle) {
            getSlidingMenu().toggle();
        }
    }

    @Override
    public void onMenuSelect(int menu, boolean checkCurrentFragment, boolean isToggle) {
        selectMenu(menu, checkCurrentFragment, isToggle, false, null);
    }
}
