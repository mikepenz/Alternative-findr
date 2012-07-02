package com.tundem.alternativefindr;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.tundem.alternativefindr.cab.ApplicationCallback;
import com.tundem.alternativefindr.cfg.Cfg;
import com.tundem.alternativefindr.entity.Application;
import com.tundem.alternativefindr.entity.ApplicationFilter;
import com.tundem.alternativefindr.fragments.AlternativesFragment;
import com.tundem.alternativefindr.fragments.BaseFragment;
import com.tundem.alternativefindr.fragments.DescriptionFragment;
import com.tundem.alternativefindr.fragments.DetailsFragment;
import com.tundem.alternativefindr.fragments.adapter.AlternativesFragmentAdapter;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import com.wrapp.android.webimage.WebImage;
import com.wrapp.android.webimage.WebImageView;

public class ApplicationActivity extends SherlockFragmentActivity {
	private ViewPager mPager;
	private PageIndicator mIndicator;
	private AlternativesFragmentAdapter mAdapter;
	private List<BaseFragment> CONTENT = new LinkedList<BaseFragment>();
	private ApplicationCallback applicationCallback;
	private Application mApplication;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_application);

		mApplication = Cfg.APP_PARAM;
		Cfg.APP_PARAM = null;

		//Init actionbar
		init_actionbar();

		View v = this.findViewById(R.id.layout);
		animate(v).setDuration(0).alpha(0).start();
		animate(v).setDuration(1000).alpha(1).start();

		//Clear cache if older than an hour
		WebImage.clearOldCacheFiles(this, 3600);

		// Do all inits ;)
		init_data();
		init_tabs();
		init_application(v);
	}

	// --------------------------------------------------------------
	// OPERATIONS
	// --------------------------------------------------------------

	public void updateCurrentListView() {
		AlternativesFragmentAdapter afa = (AlternativesFragmentAdapter) mPager.getAdapter();
		if (afa != null) {
			BaseFragment bf = afa.getItem(mPager.getCurrentItem());
			if (bf instanceof AlternativesFragment) {
				AlternativesFragment af = (AlternativesFragment) bf;
				if (af != null) {
					//af.listView.prepareForRefresh();
					af.pullToRefreshListView.getRefreshableView().smoothScrollToPosition(0);
					af.executeDown();
					af.pullToRefreshListView.setRefreshing();
				}
			}
		}
	}

	public void showApplicationActionbar(Application application) {
		ApplicationCallback.setApplication(application);
		if (applicationCallback.getmMode() == null) {
			applicationCallback.setmMode(startActionMode(applicationCallback));
		}
	}

	// --------------------------------------------------------------
	// INIT
	// --------------------------------------------------------------

	private void init_data() {
		applicationCallback = ApplicationCallback.getInstance(this, this, getSupportMenuInflater());

		if (mApplication == null)
			return;

		CONTENT.add(new DescriptionFragment(getString(R.string.alt_descr), mApplication.getShortDescription()));

		CONTENT.add(new DetailsFragment(getString(R.string.alt_detail), mApplication));

		ApplicationFilter af = new ApplicationFilter(ApplicationFilter.TYP_ALTERNATIVES, mApplication.getID());
		af.setAction(ApplicationFilter.ACTION_ALTERNATIVES);
		af.setItemCount(25);
		CONTENT.add(new AlternativesFragment(this, getString(R.string.alt_alt), af));
	}

	private void init_tabs() {
		mAdapter = new AlternativesFragmentAdapter(getSupportFragmentManager(), CONTENT);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setPageMargin(10);
		mPager.setPageMarginDrawable(new ColorDrawable(getResources().getColor(R.color.alternativeto_mediumblue)));

		mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
	}

	public void init_actionbar() {
		ActionBar ab = getSupportActionBar();

		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayUseLogoEnabled(true);
	}

	public void init_application(View v) {
		WebImageView alternative_icon = ((WebImageView) v.findViewById(R.id.alternative_icon));
		TextView alternative_votes = ((TextView) v.findViewById(R.id.alternative_votes));
		TextView alternative_name = ((TextView) v.findViewById(R.id.alternative_name));
		TextView alternative_license = ((TextView) v.findViewById(R.id.alternative_license));
		LinearLayout alternative_platforms = ((LinearLayout) v.findViewById(R.id.alternative_platforms_container));
		//TextView alternative_description = ((TextView) v.findViewById(R.id.alternative_description));

		/* Set ImageView */
		alternative_icon.setImageUrl(mApplication.getIconUrl());

		/* Set Texts */
		alternative_votes.setText(String.valueOf(mApplication.getVotes()) + "\nVotes");
		alternative_name.setText(mApplication.getName());
		alternative_license.setText(mApplication.getLicense());
		//alternative_description.setText(application.getShortDescription());

		if (mApplication.getPlatforms() != null) {
			List<Integer> resIds = new LinkedList<Integer>();
			for (String s : mApplication.getPlatforms()) {
				if (!TextUtils.isEmpty(s)) {
					int resId = getResId(s.toLowerCase());
					if (!resIds.contains(resId) && resId != -1)
						resIds.add(resId);
				}
			}
			for (Integer resId : resIds) {
				ImageView iv = new ImageView(this);

				iv.setScaleType(ScaleType.FIT_CENTER);

				float sizeInDip = 18f;
				int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInDip, this
						.getResources().getDisplayMetrics());
				LayoutParams lp = new LayoutParams(padding, padding);
				iv.setLayoutParams(lp);
				iv.setImageResource(resId);

				alternative_platforms.addView(iv);
			}

		}
	}

	public int getResId(String s) {
		int resId = -1;
		if (s.contains("mac") || s.contains("ipad") || s.contains("iphone")) {
			resId = R.drawable.ico_mac;
		} else if (s.contains("android")) {
			resId = R.drawable.ico_android;
		} else if (s.contains("blackberry")) {
			resId = R.drawable.ico_blackberry;
		} else if (s.contains("windows") || s.contains("windows-phone")) {
			resId = R.drawable.ico_windows;
		} else if (s.contains("linux")) {
			resId = R.drawable.ico_linux;
		} else if (s.contains("online")) {
			resId = R.drawable.ico_online;
		} else if (s.contains("s60")) {
			resId = R.drawable.ico_s60;
		}
		return resId;
	}

	// --------------------------------------------------------------
	// ONMETHODS
	// --------------------------------------------------------------

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the options menu from XML
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.acb_application, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.item_share:
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mApplication.getName());
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, mApplication.getShortDescription() + "\n\n\n\n\n\n"
					+ getString(R.string.alt_foundwith));
			startActivity(Intent.createChooser(shareIntent, getString(R.string.alt_sharenow)));
			return true;
		case R.id.item_external:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mApplication.getUrl()));
			this.startActivity(browserIntent);
			return true;
		default:
			return false;
		}
	}

	public void onBack(View v) {
		this.finish();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}