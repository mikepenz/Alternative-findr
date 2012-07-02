package com.tundem.alternativefindr;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tundem.alternativefindr.cfg.Cfg;
import com.tundem.alternativefindr.entity.License;
import com.tundem.alternativefindr.entity.Platform;
import com.tundem.alternativefindr.storage.LicenseStorage;
import com.tundem.alternativefindr.storage.PlatformStorage;
import com.tundem.log.Lg;
import com.tundem.utils.dialogs.CustomToast;
import com.tundem.utils.network.CheckNetworkStatus;

public class SplashscreenActivity extends SherlockFragmentActivity {
	private static final long SPLASHTIME = 2500;
	private boolean isLicenseReady = false;
	private boolean isPlatformsReady = false;
	private boolean timeOver = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.act_splashscreen);

		init_actionbar();
		
		View v = this.findViewById(R.id.layout);
		animate(v).setDuration(0).alpha(0).start();
		animate(v).setDuration(1000).alpha(1).start();

		//Check if there's an internet connection
		if (!CheckNetworkStatus.haveNetworkConnection(this)) {
			CustomToast.makeCustomToast(this, getString(R.string.nointernet), 3000);
			SplashscreenActivity.this.finish();
		}
		else
		{
			LicenseTask lTask = new LicenseTask();
			lTask.execute();
			PlatformsTask pTask = new PlatformsTask();
			pTask.execute();
			
			 new Handler().postDelayed(new Runnable()
	         {
	             @Override
	             public void run()
	             {
	        		if(!SplashscreenActivity.this.isFinishing() && isLicenseReady && isPlatformsReady)
	        		{
	             		finishiing();
	        		}
	        		else
	        		{
	        			timeOver = true;
	        		}
	             }
	         }, SPLASHTIME);
		}
	}

	public void init_actionbar() {
		ActionBar ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(true);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayUseLogoEnabled(true);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	public void finishiing()
	{
		Intent intent = new Intent();
		intent.setClass(SplashscreenActivity.this, AlternativefindrActivity.class);
		startActivity(intent);
		SplashscreenActivity.this.finish();
	}

	// --------------------------------------------------------------
	// Get Tasks ;)
	// --------------------------------------------------------------
	public class LicenseTask extends AsyncTask<String, List<License>, List<License>> {
		@Override
		protected List<License> doInBackground(String... input) {
			String result = "";
			InputStream instream = null;

			// http
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpUriRequest request = new HttpGet(Cfg.API_URL + "license");

				HttpResponse response = httpclient.execute(request);
				instream = response.getEntity().getContent();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// convert response to string
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream));

				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				result = sb.toString();
				instream.close();
			} catch (Exception e) {
				Lg.e("Error converting result " + e.toString());
			}

			try {
				if (!TextUtils.isEmpty(result)) {
					result = result.replace("&quot;", "'");
					result = Html.fromHtml(result).toString();
				}
			} catch (Exception e) {
				Lg.e("Error stringoperations " + e.toString());
			}

			// parse json data
			try {
				if (!TextUtils.isEmpty(result)) {
					Type collectionType = new TypeToken<List<License>>() {
					}.getType();
					return new Gson().fromJson(result, collectionType);
				}
			} catch (Exception e) {
				Lg.e("Error parsing data " + e.toString());
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<License> license) {
			LicenseStorage ls = new LicenseStorage();
			ls.writeJSON(license);
			
			isLicenseReady = true;
			
    		if(!SplashscreenActivity.this.isFinishing() && timeOver && isPlatformsReady)
    		{
         		finishiing();
    		}
		}
	}
	
	public class PlatformsTask extends AsyncTask<String, List<Platform>, List<Platform>> {
		@Override
		protected List<Platform> doInBackground(String... input) {
			String result = "";
			InputStream instream = null;

			// http
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpUriRequest request = new HttpGet(Cfg.API_URL + "platforms");

				HttpResponse response = httpclient.execute(request);
				instream = response.getEntity().getContent();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// convert response to string
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream));

				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				result = sb.toString();
				instream.close();
			} catch (Exception e) {
				Lg.e("Error converting result " + e.toString());
			}

			try {
				if (!TextUtils.isEmpty(result)) {
					result = result.replace("&quot;", "'");
					result = Html.fromHtml(result).toString();
				}
			} catch (Exception e) {
				Lg.e("Error stringoperations " + e.toString());
			}

			// parse json data
			try {
				if (!TextUtils.isEmpty(result)) {
					Type collectionType = new TypeToken<List<Platform>>() {
					}.getType();
					return new Gson().fromJson(result, collectionType);
				}
			} catch (Exception e) {
				Lg.e("Error parsing data " + e.toString());
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Platform> platforms) {
			PlatformStorage ps = new PlatformStorage();
			ps.writeJSON(platforms);
			
			isPlatformsReady = true;
			
    		if(!SplashscreenActivity.this.isFinishing() && timeOver && isLicenseReady)
    		{
         		finishiing();
    		}
		}
	}
}