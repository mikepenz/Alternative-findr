package com.tundem.alternativefindr.fragments.adapter;

import java.util.LinkedList;
import java.util.List;

import android.support.v4.app.FragmentManager;

import com.tundem.alternativefindr.fragments.BaseFragment;

public class AlternativesFragmentAdapter extends CustomFragmentStatePagerAdapter {
	public List<BaseFragment> CONTENT = new LinkedList<BaseFragment>();

	public AlternativesFragmentAdapter(FragmentManager fm, List<BaseFragment> CONTENT) {
		super(fm);
		this.CONTENT = CONTENT;
	}
	
	@Override
	public BaseFragment getItem(int position) {
		return CONTENT.get(position);
	}

	@Override
	public int getCount() {
		return CONTENT.size();
	}

	@Override
	public String getPageTitle(int position) {
		return getItem(position).getTitle();
	}
}
