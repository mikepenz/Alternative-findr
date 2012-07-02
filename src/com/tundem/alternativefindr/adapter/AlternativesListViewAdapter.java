package com.tundem.alternativefindr.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tundem.alternativefindr.AlternativefindrActivity;
import com.tundem.alternativefindr.ApplicationActivity;
import com.tundem.alternativefindr.R;
import com.tundem.alternativefindr.cfg.Cfg;
import com.tundem.alternativefindr.entity.Application;
import com.tundem.alternativefindr.entity.sort.SortApplication;
import com.wrapp.android.webimage.WebImageView;

public class AlternativesListViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Application> applications;
	private Activity act;

	public AlternativesListViewAdapter(Activity act, List<Application> applications) {
		this.act = act;
		this.applications = applications;
		this.inflater = LayoutInflater.from(act);
	}
	
	public List<Application> getApplications() {
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

	public void addApplication(Application application)
	{
		applications.add(application);
		this.notifyDataSetChanged();
	}
	
	public void addApplications(List<Application> applications)
	{
		for(Application a : applications)
		{
			this.applications.add(a);
		}
		this.notifyDataSetChanged();
	}
	
	public void addSortedApplications(List<Application> applications)
	{
		for(Application a : applications)
		{
			this.applications.add(a);
		}
		
		Collections.sort(this.applications, new SortApplication());
		
		this.notifyDataSetChanged();
	}

	
	@Override
	public int getCount() {
		if (applications != null)
			return applications.size();
		else
			return 0;
	}

	@Override
	public Application getItem(int pos) {
		if (applications != null)
			return applications.get(pos);
		else
			return null;
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Application application = this.getItem(position);
		
		//Lg.v(application.toString());

		LinearLayout view = (LinearLayout)inflater.inflate(R.layout.liv_alternatives_item, null);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Cfg.APP_PARAM = application;
         		Intent intent = new Intent();
        		intent.setClass(act, ApplicationActivity.class);
        		act.startActivity(intent);

        		//Finish activity if it's already an applicationactivity!
				if(act instanceof ApplicationActivity)
				{
					((ApplicationActivity)act).finish();
				}
				
        		/*
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(application.getUrl()));
				ctx.startActivity(browserIntent);
				*/
			}
		});
		view.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if(act instanceof ApplicationActivity)
				{
					((ApplicationActivity)act).showApplicationActionbar(application);
				}
				else if(act instanceof AlternativefindrActivity)
				{
					((AlternativefindrActivity)act).showApplicationActionbar(application);
				}
				return true;
			}
		});

		WebImageView alternative_icon = ((WebImageView) view.findViewById(R.id.alternative_icon));
		TextView alternative_votes = ((TextView) view.findViewById(R.id.alternative_votes));
		TextView alternative_name = ((TextView) view.findViewById(R.id.alternative_name));
		LinearLayout alternative_platforms = ((LinearLayout) view.findViewById(R.id.alternative_platforms_container));
		TextView alternative_license = ((TextView) view.findViewById(R.id.alternative_license));
		TextView alternative_description = ((TextView) view.findViewById(R.id.alternative_description));
		TextView alternative_tags = ((TextView) view.findViewById(R.id.alternative_tags));

		/* Set ImageView */
		alternative_icon.setImageUrl(application.getIconUrl());

		/* Set Texts */
		alternative_votes.setText(String.valueOf(application.getVotes()) + "\nVotes");
		alternative_name.setText(application.getName());

		if (application.getPlatforms() != null) {
			List<Integer> resIds = new LinkedList<Integer>();
			for (String s : application.getPlatforms()) {
				if (!TextUtils.isEmpty(s)) {
					int resId = getResId(s.toLowerCase());
					if (!resIds.contains(resId) && resId != -1)
						resIds.add(resId);
				}
			}
			for (Integer resId : resIds) {
				ImageView iv = new ImageView(act);

				iv.setScaleType(ScaleType.FIT_CENTER);

				float sizeInDip = 18f;
				int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInDip, act
						.getResources().getDisplayMetrics());
				LayoutParams lp = new LayoutParams(padding, padding);
				iv.setLayoutParams(lp);
				iv.setImageResource(resId);

				alternative_platforms.addView(iv);
			}

		}
		alternative_license.setText(application.getLicense());
		alternative_description.setText(application.getShortDescription());

		String tags = "Tags: ";
		if (application.getTags() != null) {
			for (String s : application.getTags()) {
				tags = tags + s.charAt(0) + ", ";
			}
			tags.substring(0, tags.length() - 2);
		}
		alternative_tags.setText(tags);

		return view;
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
}