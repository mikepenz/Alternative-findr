package com.tundem.alternativefindr;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.tundem.alternativefindr.cab.AlternativesCallback;
import com.tundem.alternativefindr.cfg.Cfg;
import com.tundem.alternativefindr.entity.Application;
import com.tundem.alternativefindr.entity.ApplicationFilter;
import com.tundem.alternativefindr.entity.License;
import com.tundem.alternativefindr.entity.Platform;
import com.tundem.alternativefindr.fragments.AlternativesFragment;
import com.tundem.alternativefindr.fragments.BaseFragment;
import com.tundem.alternativefindr.fragments.adapter.AlternativesFragmentAdapter;
import com.tundem.alternativefindr.storage.LicenseStorage;
import com.tundem.alternativefindr.storage.PlatformStorage;
import com.tundem.utils.dialogs.CustomToast;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.wrapp.android.webimage.WebImage;

public class AlternativefindrActivity extends SherlockFragmentActivity{
	//Modern Stuff
	private ViewPager mPager;
	private PageIndicator mIndicator;
	private AlternativesFragmentAdapter mAdapter;
	private List<BaseFragment> CONTENT = new LinkedList<BaseFragment>();
	
	//Search Stuff
	private String mSearchTerm = "";
	private int startFragment = 1;
	private AlternativesCallback alternativesCallback;

	//Important Stuff
	private List<License> license;
	private List<Platform> platforms;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		
		//Init actionbar
		init_actionbar();
		
		//Animate
		View v = this.findViewById(R.id.layout);
		animate(v).setDuration(0).alpha(0).start();
		animate(v).setDuration(1000).alpha(1).start();

		//Clear cache if older than an hour
		WebImage.clearOldCacheFiles(this, 43200);

		// Get the intent, verify the action and get the query
		handleIntent(getIntent(), true);
		
		//Load platforms ;) 
		PlatformStorage ps = new PlatformStorage();
		platforms = ps.readJSON();
		
		//Load licenses ;)
		LicenseStorage ls = new LicenseStorage();
		license = ls.readJSON();
		
		if(platforms == null || license == null)
		{
			CustomToast.makeCustomToast(this, getString(R.string.somethingwrong), 2500);
		}

		// Do all inits ;)
		init_data();
		init_tabs();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent, false);
	}

	public void handleIntent(Intent intent, boolean onCreate) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			mSearchTerm = intent.getStringExtra(SearchManager.QUERY);
			startFragment = 0;

			if (!onCreate) {
				//mPager.setCurrentItem(startFragment);
				mIndicator.setCurrentItem(startFragment);
				updateCurrentListView();
			}
		}
		else if(Intent.ACTION_SEARCH_LONG_PRESS.equals(intent.getAction()))
		{
			onSearchRequested();
		}
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
					if(af.getmApplicationFilter().getAction() == ApplicationFilter.ACTION_SEARCH)
					{
						ApplicationFilter appFilter = af.getmApplicationFilter();
						appFilter.setSearchTerm(mSearchTerm);
						af.setmApplicationFilter(appFilter);
					}
					
					af.pullToRefreshListView.getRefreshableView().smoothScrollToPosition(0);
					af.executeDown();
					af.pullToRefreshListView.setRefreshing();
				}
			}
		}
	}

	public void showApplicationActionbar(Application application)
	{
		AlternativesCallback.setApplication(application);
		if (alternativesCallback.getmMode() == null) {
			alternativesCallback.setmMode(startActionMode(alternativesCallback));
		}
	}
	
	// --------------------------------------------------------------
	// INIT
	// --------------------------------------------------------------

	private void init_data() {
		alternativesCallback = AlternativesCallback.getInstance(this, getSupportMenuInflater());
		
		ApplicationFilter af = new ApplicationFilter(ApplicationFilter.TYP_SEARCHTERM, mSearchTerm);
		af.setAction(ApplicationFilter.ACTION_SEARCH);
		af.setItemCount(25);
		CONTENT.add(new AlternativesFragment(this, getString(R.string.alt_search), af));
		//CONTENT.add(new SearchFragment(this, getString(R.string.alt_search), 20, mSearchTerm, null));
		
		af = new ApplicationFilter(ApplicationFilter.TYP_CATEGORY, getString(R.string.alt_all));
		af.setAction(ApplicationFilter.ACTION_CATEGORY);
		af.setItemCount(10); //Standard ;)
		
		CONTENT.add(new AlternativesFragment(this, getString(R.string.alt_all), af));
		
		for(Platform p : platforms)
		{
			af = new ApplicationFilter(ApplicationFilter.TYP_CATEGORY, p.getIdentifier());
			CONTENT.add(new AlternativesFragment(this, p.getName(), af));
		}
	}

	private void init_tabs() {
		mAdapter = new AlternativesFragmentAdapter(getSupportFragmentManager(), CONTENT);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setPageMargin(10);
		mPager.setPageMarginDrawable(new ColorDrawable(getResources().getColor(R.color.alternativeto_mediumblue)));
		mPager.setCurrentItem(startFragment);

		mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		mIndicator.setCurrentItem(startFragment);
		mIndicator.setOnPageChangeListener(onPageChangeListener);
	}

	public void init_actionbar() {
		ActionBar ab = getSupportActionBar();
		
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayUseLogoEnabled(true);

		ab.setListNavigationCallbacks(ArrayAdapter.createFromResource(this, R.array.listValues_sections,
				R.layout.sherlock_spinner_dropdown_item), navigationListener);

		if (ab.getNavigationMode() != ActionBar.NAVIGATION_MODE_LIST) {
			ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		}
	}

	// --------------------------------------------------------------
	// LISTENER
	// --------------------------------------------------------------

	public OnNavigationListener navigationListener = new OnNavigationListener() {
		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
			switch (itemPosition) {
			case 0:
				Cfg.API_CATEGORY = "popular";
				updateCurrentListView();
				return false;
			case 1:
				Cfg.API_CATEGORY = "recent";
				updateCurrentListView();
				return false;
			case 2:
				Cfg.API_CATEGORY = "likes";
				updateCurrentListView();
				return false;
			case 3:
				Cfg.API_CATEGORY = "views";
				updateCurrentListView();
				return false;
			default:
				return false;
			}
		}
	};

	// --------------------------------------------------------------
	// ONMETHODS
	// --------------------------------------------------------------

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the options menu from XML
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.acb_alternatives, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		
		switch (item.getItemId()) {
		case R.id.item_info:
			intent.setClass(AlternativefindrActivity.this, InfoActivity.class);
			startActivity(intent);
			return true;
		case R.id.item_about:
			intent.setClass(AlternativefindrActivity.this, AboutActivity.class);
			startActivity(intent);
			return true;
		case R.id.item_refresh:
			updateCurrentListView();
			return true;
		case R.id.item_search:
			onSearchRequested();
			return true;
		case R.id.item_ext_search:
			CustomToast.makeCustomToast(this, getString(R.string.notimplementedyet), 300);
			//Toast.makeText(this, getString(R.string.notimplementedyet),3000).show();
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						onBack(null);
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						break;
					}
				}
			};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getString(R.string.info));
			builder.setMessage(getString(R.string.sure))
					.setPositiveButton(getString(R.string.yes), dialogClickListener)
					.setNegativeButton(getString(R.string.no), dialogClickListener).show();

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
	
	
	
	public OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			if(position>1)
			{
				Cfg.ACTUAL_PlATFORM = platforms.get(position);
			}
			else
			{
				Cfg.ACTUAL_PlATFORM = null;
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	};
}