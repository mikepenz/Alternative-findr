package com.tundem.alternativefindr.cab;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.tundem.alternativefindr.AlternativefindrActivity;
import com.tundem.alternativefindr.ApplicationActivity;
import com.tundem.alternativefindr.R;
import com.tundem.alternativefindr.entity.Application;

public class ApplicationCallback implements ActionMode.Callback {
	private static ApplicationCallback SINGLETON = null;
	private ApplicationActivity act;
	private Application application;
	private ActionMode mMode;
	private MenuInflater inflater;

	//Hidden construktor
	private ApplicationCallback() {
	}

	/* IF NULL SOMETHING WENT WRONG?! */
	public static ApplicationCallback getInstance(ApplicationActivity act, MenuInflater inflater, Application application) {
		if (SINGLETON == null) {
			SINGLETON = new ApplicationCallback();
		}
		SINGLETON.act = act;
		SINGLETON.application = application;
		SINGLETON.inflater = inflater;

		return SINGLETON;
	}
	
	public static ApplicationCallback getInstance(ApplicationActivity act, Activity activity, MenuInflater inflater) {
		if (SINGLETON == null) {
			SINGLETON = new ApplicationCallback();
		}
		SINGLETON.act = act;
		SINGLETON.inflater = inflater;

		return SINGLETON;
	}
	
	public static void setApplication(Application application)
	{
		SINGLETON.application = application;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		inflater.inflate(R.menu.cab_application, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		if (mode == mMode) {
			mMode = null;
		}
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		if(application == null)
		{
			mode.finish();
			return true;
		}
		
		switch (item.getItemId()) {
		case R.id.item_search:
			Intent intent = new Intent(Intent.ACTION_SEARCH);
			intent.putExtra(SearchManager.QUERY, application.getID());
    		intent.setClass(act, AlternativefindrActivity.class);
    		act.startActivity(intent);
			act.finish();
			break;
		case R.id.item_share:
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, application.getName());
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, application.getShortDescription() + "\n\n\n\n"
					+ act.getString(R.string.alt_foundwith));
			act.startActivity(Intent.createChooser(shareIntent, act.getString(R.string.alt_sharenow)));
			break;
		case R.id.item_external:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(application.getUrl()));
			act.startActivity(browserIntent);
			break;
		}

		mode.finish();
		return true;
	}

	public ActionMode getmMode() {
		return mMode;
	}

	public void setmMode(ActionMode mMode) {
		this.mMode = mMode;
	}
}