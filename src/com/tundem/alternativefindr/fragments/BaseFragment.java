package com.tundem.alternativefindr.fragments;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
	public String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
