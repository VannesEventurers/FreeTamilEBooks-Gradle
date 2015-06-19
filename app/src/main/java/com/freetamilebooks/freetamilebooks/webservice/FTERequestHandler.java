package com.freetamilebooks.freetamilebooks.webservice;

public interface FTERequestHandler {
    void onSuccess(String content);
    void onFailure(int statusCode, Throwable error, String message);
    void onProgressUpdate(long percent);
}
