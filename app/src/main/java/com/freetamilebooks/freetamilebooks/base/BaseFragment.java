package com.freetamilebooks.freetamilebooks.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.freetamilebooks.freetamilebooks.R;
import com.freetamilebooks.freetamilebooks.app.FTEApp;
import com.freetamilebooks.freetamilebooks.leftmenu.HomeActivity;
import com.freetamilebooks.freetamilebooks.utils.ColorFilterUtils;
import com.freetamilebooks.freetamilebooks.utils.DeviceUtils;
import com.freetamilebooks.freetamilebooks.webservice.FTEHttpClient;

public class BaseFragment extends Fragment {

    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.back) {
                onLeftActionClick(v);
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


    public void setPageTitle(int title) {
        setPageTitle(getString(title));
    }

    public void onLeftActionClick(View v) {
        DeviceUtils.hideSoftKeyboard(getActivity());
        getHome().getSlidingMenu().toggle();
    }

    public void onFragmentOpened() {

    }

    public void onFragmentClosed() {

    }

    public BaseFragment getActiveFragment() {
        return (BaseFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }
}
