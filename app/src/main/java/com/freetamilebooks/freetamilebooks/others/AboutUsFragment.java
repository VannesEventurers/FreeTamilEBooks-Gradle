package com.freetamilebooks.freetamilebooks.others;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.freetamilebooks.freetamilebooks.R;
import com.freetamilebooks.freetamilebooks.base.BaseFragment;
import com.freetamilebooks.freetamilebooks.utils.AlertUtils;

public class AboutUsFragment extends BaseFragment {
    private WebView webView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setPageTitle(getString(R.string.aboutus));
        setLeftDrawable(R.drawable.left_menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, null);

        init(view);
        setupDefaults();
        setupEvents();

        return view;
    }

    private void init(View view) {
        webView = (WebView) view.findViewById(R.id.fragment_webview);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupDefaults() {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading_));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if(mProgressDialog != null && getActiveFragment() instanceof HelpUsFragment) {
                    mProgressDialog.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if(mProgressDialog != null && getActiveFragment() instanceof HelpUsFragment) {
                    mProgressDialog.dismiss();
                }
                AlertUtils.showAlert(getActivity(), description);
            }
        });

        webView.loadUrl("file:///android_asset/htmlfiles/about_us.html");
    }

    private void setupEvents() {
    }
}