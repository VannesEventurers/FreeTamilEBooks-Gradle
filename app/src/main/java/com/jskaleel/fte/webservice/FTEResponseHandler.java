package com.jskaleel.fte.webservice;

import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

abstract class FTEResponseHandler extends TextHttpResponseHandler {
    private static final String DEFAULT_ERROR = "Unable to connect server. Please try again.";
    private String url, method;

    public FTEResponseHandler(String url, FTEHttpClient.HttpMethod method) {
        this.url = url;
        this.method = method.name();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onProgress(long bytesWritten, long totalSize) {
        super.onProgress(bytesWritten, totalSize);
        onProgress((bytesWritten * 100) / totalSize);
    }

    public void onProgress(long percent) {

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        onSuccess(responseString);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        if (statusCode == 404 || statusCode == 500) {
            onFailure(statusCode, throwable, DEFAULT_ERROR);
            return;
        }

        if (responseString == null) {
            onFailure(statusCode, throwable, DEFAULT_ERROR);
            return;
        }

        onFailure(statusCode, throwable, responseString);
    }

    public void onSuccess(String content) {

    }


    public void onFailure(int statusCode, Throwable throwable, String message) {

    }

    @Override
    public void onFinish() {
        super.onFinish();
    }

}
