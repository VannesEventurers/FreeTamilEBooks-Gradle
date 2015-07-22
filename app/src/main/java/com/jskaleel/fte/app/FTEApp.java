package com.jskaleel.fte.app;

import android.app.Application;
import android.os.Build;

import com.jskaleel.fte.base.ActivityFinishCallback;
import com.jskaleel.fte.preferences.UserPreference;
import com.jskaleel.fte.webservice.FTEHttpClient;

import java.util.ArrayList;

public class FTEApp extends Application {



	private FTEHttpClient httpClient;
	private ArrayList<ActivityFinishCallback> mActivities;
	private UserPreference mUserPreference;

	public static boolean GTE_HC_11 = false;
	public static boolean PRE_HC_11 = false;

	@Override
	public void onCreate() {
		super.onCreate();
		httpClient = new FTEHttpClient(this);
		mUserPreference = new UserPreference(this);
		mActivities = new ArrayList<>();
	}

	public FTEHttpClient getHttpClient() {
		return httpClient;
	}

	public UserPreference getUserPreference() {
		return mUserPreference;
	}

	public void removeActivity(ActivityFinishCallback activity) {
		mActivities.remove(activity);
	}

	static {
		int version = Build.VERSION.SDK_INT;

		if (version >= Build.VERSION_CODES.HONEYCOMB)
			GTE_HC_11 = true;
		else
			PRE_HC_11 = true;
	}
}
