package com.jskaleel.fte.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jskaleel.fte.R;
import com.jskaleel.fte.app.FTEApp;
import com.jskaleel.fte.leftmenu.HomeActivity;
import com.jskaleel.fte.utils.ColorFilterUtils;
import com.jskaleel.fte.utils.DeviceUtils;
import com.jskaleel.fte.webservice.FTEHttpClient;

public class BaseFragment extends Fragment {

    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.back) {
                onLeftActionClick(v);
            }else if(v.getId() == R.id.iv_search) {
                onRightActionClick(v);
            }else if(v.getId() == R.id.iv_facebook) {
                onFacebookIconClick(v);
            }
        }
    };

    public HomeActivity getHome() {
        return (HomeActivity) getActivity();
    }
    public FTEApp getApp() {
        return (FTEApp) getActivity().getApplication();
    }

    public FTEHttpClient getHttpClient() {
        return getApp().getHttpClient();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setPageTitle(String title) {
        TextView txt = (TextView) getView().findViewById(R.id.title);
        txt.setVisibility(View.VISIBLE);
        if (txt != null) {
            txt.setText(title);
        }
    }

    public void setLeftDrawable(int backIconResource) {
        ImageView back = (ImageView) getView().findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(onClick);
        if (back != null) {
            back.setImageResource(backIconResource);
            ColorFilterUtils.setColorFilter(back);
        }
    }

    public void setRightDrawable(int searchIcon) {
        ImageView search = (ImageView) getView().findViewById(R.id.iv_search);
        search.setVisibility(View.VISIBLE);
        search.setOnClickListener(onClick);
        if (search != null) {
            search.setImageResource(searchIcon);
            ColorFilterUtils.setColorFilter(search);
        }
    }

    public void setFacebookDrawable(int facebookIcon) {
        ImageView facebook = (ImageView) getView().findViewById(R.id.iv_facebook);
        facebook.setVisibility(View.VISIBLE);
        facebook.setOnClickListener(onClick);
        if (facebook != null) {
            facebook.setImageResource(facebookIcon);
            ColorFilterUtils.setColorFilter(facebook);
        }
    }

    public void setPageTitle(int title) {
        setPageTitle(getString(title));
    }

    public void onLeftActionClick(View v) {
        DeviceUtils.hideSoftKeyboard(getActivity());
        getHome().getSlidingMenu().toggle();
    }

    public void onRightActionClick(View v) {

    }

    public void onFacebookIconClick(View v) {

    }

    public void onFragmentOpened() {

    }

    public void onFragmentClosed() {

    }

    public BaseFragment getActiveFragment() {
        return (BaseFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }
}
