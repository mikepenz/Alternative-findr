package com.tundem.alternativefindr;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class InfoActivity extends SherlockFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_info);

		init_actionbar();

		View v = this.findViewById(R.id.layout);
		animate(v).setDuration(0).alpha(0).start();
		animate(v).setDuration(1000).alpha(1).start();

		((TextView) v.findViewById(R.id.frag_info_textview)) .setText(Html.fromHtml(getString(R.string.infotext)));
	}

	// ---------- INITS
	public void init_actionbar() {
		ActionBar ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayUseLogoEnabled(true);
	}

	// ---------- OnMethods ;)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the options menu from XML
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.acb, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.item_info:
			return true;
		case R.id.item_about:
			Intent intent = new Intent();
			intent.setClass(InfoActivity.this, AboutActivity.class);
			startActivity(intent);
			this.finish();	
			return true;
		case R.id.item_share:
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.alt_sharesubject));
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.alt_sharebody));
			startActivity(Intent.createChooser(shareIntent, getString(R.string.alt_sharenow)));
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
