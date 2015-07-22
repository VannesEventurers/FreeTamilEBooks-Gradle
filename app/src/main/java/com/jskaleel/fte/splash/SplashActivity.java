package com.jskaleel.fte.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import com.jskaleel.fte.R;
import com.jskaleel.fte.leftmenu.HomeActivity;

import java.io.File;

public class SplashActivity extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				File path = new File(Environment.getExternalStorageDirectory(),"/Free_Tamil_Ebooks");

				if(!(path.exists()))
					path.mkdir();
				Intent i = new Intent(SplashActivity.this, HomeActivity.class);
				startActivity(i);
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

}
