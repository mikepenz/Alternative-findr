package com.tundem.alternativefindr.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.R;

public class DescriptionFragment extends BaseFragment {
	private String description;

	public DescriptionFragment() {
	}

	public DescriptionFragment(String title, String description) {
		this.description = description;

		setTitle(title);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ScrollView view = (ScrollView) inflater.inflate(R.layout.frg_description, container, false);
		TextView textView = (TextView)view.findViewById(R.id.frag_description_textview);
		textView.setText(Html.fromHtml(description));
		return view;
	}
}
