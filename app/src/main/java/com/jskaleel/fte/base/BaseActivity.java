package com.jskaleel.fte.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jskaleel.fte.R;
import com.jskaleel.fte.app.FTEApp;
import com.jskaleel.fte.utils.DeviceUtils;
import com.jskaleel.fte.webservice.FTEHttpClient;

public class BaseActivity extends FragmentActivity implements ActivityFinishCallback {

    public FTEApp getApp() {
        return (FTEApp) getApplication();
    }

    public FTEHttpClient getHttpClient() {
        return getApp().getHttpClient();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DeviceUtils.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void setPageTitle(String title) {
        TextView txt = (TextView) findViewById(R.id.title);
        if (txt != null) {
            txt.setText(title);
        }
    }

    public void setPageTitle(int title) {
        setPageTitle(getString(title));
    }

    public View getBackActionView() {
        return findViewById(R.id.back);
    }


    public void setLeftDrawable(int backIconResource) {
        ImageView back = (ImageView) getBackActionView();
        back.setVisibility(View.VISIBLE);
        if (back != null) {
            back.setImageResource(backIconResource);
        }
    }

    @Override
    public void forceFinish() {
        DeviceUtils.hideSoftKeyboard(this);
        finish();
    }

    @Override
    public void finish() {
        getApp().removeActivity(this);
        super.finish();
    }

    public void onLeftActionClick(View v) {

    }
}
