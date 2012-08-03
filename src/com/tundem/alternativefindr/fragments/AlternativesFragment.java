package com.tundem.alternativefindr.fragments;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tundem.alternativefindr.adapter.AlternativesListViewAdapter;
import com.tundem.alternativefindr.cfg.Cfg;
import com.tundem.alternativefindr.entity.Application;
import com.tundem.alternativefindr.entity.ApplicationFilter;
import com.tundem.log.Lg;

public class AlternativesFragment extends BaseFragment {
	private Activity act;
	public ApplicationFilter mApplicationFilter;
	private int mActCount = Cfg.LOADMORE_VALUE;

	private AlternativesListViewAdapter mAdapter;

	public PullToRefreshListView pullToRefreshListView;
	
	public AlternativesFragment() {
	}

	public AlternativesFragment(Activity act, String title, ApplicationFilter applicationFilter) {
		this.act = act;

		this.mApplicationFilter = applicationFilter;

		setTitle(title);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_alternatives, container, false);
		pullToRefreshListView = (PullToRefreshListView)view.findViewById(R.id.frag_listview_alternatives);
				
		//Set up pull-to-refresh
		pullToRefreshListView.setMode(Mode.BOTH);
		pullToRefreshListView.setRefreshingLabel("Refresh");
		pullToRefreshListView.setReleaseLabel("Refresh now...", Mode.PULL_DOWN_TO_REFRESH);
		pullToRefreshListView.setPullLabel("Refresh", Mode.PULL_DOWN_TO_REFRESH);
		pullToRefreshListView.setReleaseLabel("Load more...", Mode.PULL_UP_TO_REFRESH);
		pullToRefreshListView.setPullLabel("Refresh", Mode.PULL_UP_TO_REFRESH);
		

		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				Lg.v("onRefresh");
				executeDown();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if (mApplicationFilter != null) {
					DownloadMoreTasks task = new DownloadMoreTasks();
					mApplicationFilter.setItemCount(mApplicationFilter.getItemCount() + mActCount);
					task.execute(new ApplicationFilter[] { mApplicationFilter });
				}
			}

		});
		
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		if (pullToRefreshListView != null) {
			pullToRefreshListView.getRefreshableView().smoothScrollToPosition(0);
			executeDown();
			pullToRefreshListView.setRefreshing();
			//listView.onRefresh();
		}
		super.onViewCreated(view, savedInstanceState);
	}

	public void executeDown()
	{
		if (mApplicationFilter != null) {
			DownloadTasks task = new DownloadTasks();
			
			if(mAdapter != null && mAdapter.getCount()>Cfg.API_ITEMCOUNT)
				mApplicationFilter.setItemCount(mAdapter.getCount());
			
			task.execute(new ApplicationFilter[] { mApplicationFilter });
		}
	}
	
	/* --------------DownloadTask */
	public class DownloadTasks extends AsyncTask<ApplicationFilter, List<Application>, List<Application>> {
		@Override
		protected List<Application> doInBackground(ApplicationFilter... input) {
			return tasksOperation(input);
		}

		@Override
		protected void onPostExecute(List<Application> applications) {
			/* HelpStorage hs = new HelpStorage(); if (help != null) { hs.writeJSON(help); } else { help = hs.readJSON(); } */
			
			if (applications != null && applications.size() > 0) {
				//PERHAPS NEEDS TO BE REMOVED?!
				if (ApplicationFilter.ACTION_ALTERNATIVES == mApplicationFilter.getAction()) {
					applications.remove(0);
					//Sort the application by their votes
					//Collections.sort(applications, new SortApplication());
				}

				mAdapter = new AlternativesListViewAdapter(act, applications);
				pullToRefreshListView.getRefreshableView().setAdapter(mAdapter);
			} else {
				pullToRefreshListView.setPullToRefreshEnabled(false);
			}
			
			pullToRefreshListView.setLastUpdatedLabel(DateUtils.formatDateTime(getActivity(),
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL));
			pullToRefreshListView.onRefreshComplete();
		}
	}

	/* --------------DownloadMoreTask */
	public class DownloadMoreTasks extends AsyncTask<ApplicationFilter, List<Application>, List<Application>> {
		@Override
		protected List<Application> doInBackground(ApplicationFilter... input) {
			return tasksOperation(input);
		}

		@Override
		protected void onPostExecute(List<Application> applications) {
			/* HelpStorage hs = new HelpStorage(); if (help != null) { hs.writeJSON(help); } else { help = hs.readJSON(); } */

			int items = mAdapter.getCount();
			if (applications != null) {
				if (items != applications.size()) {
					List<Application> temp_applications = new LinkedList<Application>();
					for (int i = items; i < applications.size(); i++) {
						boolean found = false;
						for(Application a : mAdapter.getApplications())
						{
							if(a.getID().equals(applications.get(i).getID()))
								found = true;
						}
						
						if(!found)
							temp_applications.add(applications.get(i));
					}

					mAdapter.addApplications(temp_applications);
					
					/*
					if (ApplicationFilter.ACTION_ALTERNATIVES == mApplicationFilter.getAction()) 
						mAdapter.addSortedApplications(temp_applications);
					*/
				}
			}

			pullToRefreshListView.setLastUpdatedLabel(DateUtils.formatDateTime(getActivity(),
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL));
			pullToRefreshListView.onRefreshComplete();
			
			
			if (items != applications.size()) 
				pullToRefreshListView.setMode(Mode.PULL_DOWN_TO_REFRESH);
		}
	}

	/* --------------tasksOperation */
	public List<Application> tasksOperation(ApplicationFilter... input) {
		String result = "";
		InputStream instream = null;

		ApplicationFilter applicationFilter = input[0];

		// http
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpUriRequest request = new HttpGet(applicationFilter.getGeneratedQuery());

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
				Type collectionType = new TypeToken<List<Application>>() {
				}.getType();
				return new Gson().fromJson(result, collectionType);
			}
		} catch (Exception e) {
			Lg.e("Error parsing data " + e.toString());
		}

		return null;
	}

	/* --------------Getter&Setter */
	public ApplicationFilter getmApplicationFilter() {
		return mApplicationFilter;
	}

	public void setmApplicationFilter(ApplicationFilter mApplicationFilter) {
		this.mApplicationFilter = mApplicationFilter;
	}
}
