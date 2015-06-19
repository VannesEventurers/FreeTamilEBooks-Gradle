package com.freetamilebooks.freetamilebooks.webservice;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class FTEHttpClient {

    private int SOCKET_TIMEOUT = 60 * 1000;
    private AsyncHttpClient httpClient;
    private Context context;

    public FTEHttpClient(Context context) {
        httpClient = new AsyncHttpClient();
        httpClient.setTimeout(SOCKET_TIMEOUT);
        this.context = context;
    }

    public void performRequest(String url, HttpMethod method, RequestParams params, final FTERequestHandler handler) {
        performRawRequest(url, method, params, new FTEResponseHandler(url, method) {
            @Override
            public void onSuccess(String content) {
                if (handler != null) {
                    handler.onSuccess(content);
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, String message) {
                if (handler != null) {
                    handler.onFailure(statusCode, throwable, message);
                }
            }

            @Override
            public void onProgress(long percent) {
                if(handler != null) {
                    handler.onProgressUpdate(percent);
                }
            }
        });
    }

    private void performRawRequest(String url, HttpMethod method, RequestParams params, FTEResponseHandler handler) {
        if (params == null) {
            params = new RequestParams();
        }

        if (method == HttpMethod.GET) {
            httpClient.get(context, url, params, handler);
        }else {
            throw new NoSuchMethodError("No such HTTP method available");
        }
    }

    public static enum HttpMethod {
        GET
    }
}
