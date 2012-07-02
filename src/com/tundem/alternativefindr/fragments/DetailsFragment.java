package com.tundem.alternativefindr.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.R;
import com.tundem.alternativefindr.entity.Application;

public class DetailsFragment extends BaseFragment {
	private Application mApplication;

	public DetailsFragment() {
	}

	public DetailsFragment(String title, Application application) {
		this.mApplication = application;

		setTitle(title);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ScrollView view = (ScrollView) inflater.inflate(R.layout.frg_details, container, false);

		LinearLayout platforms = (LinearLayout) view.findViewById(R.id.details_platforms);
		if (mApplication.getPlatforms() != null) {
			((TextView) platforms.findViewById(R.id.title)).setText(getString(R.string.alt_platforms));
			String platforms_content = "";
			for (String s : mApplication.getPlatforms()) {
				platforms_content = platforms_content + s + "\n";
			}
			platforms_content = platforms_content.substring(0, platforms_content.length() - 1);
			((TextView) platforms.findViewById(R.id.content)).setText(platforms_content);
		}
		else
		{
			platforms.setVisibility(View.GONE);
		}

		LinearLayout tags = (LinearLayout) view.findViewById(R.id.details_tags);
		if (mApplication.getTags() != null) {
			((TextView) tags.findViewById(R.id.title)).setText(getString(R.string.alt_tags));
			String tags_content = "";
			for (String s : mApplication.getTags()) {
				tags_content = tags_content + s + "\n";
			}
			tags_content = tags_content.substring(0, tags_content.length() - 1);
			((TextView) tags.findViewById(R.id.content)).setText(tags_content);
		}
		else
		{
			tags.setVisibility(View.GONE);
		}

		return view;
	}
}
