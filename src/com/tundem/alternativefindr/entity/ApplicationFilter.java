package com.tundem.alternativefindr.entity;

import java.util.List;

import com.tundem.alternativefindr.cfg.Cfg;

public class ApplicationFilter {
	public static final int ACTION_CATEGORY = 1;
	public static final int ACTION_SEARCH = 2;
	public static final int ACTION_ALTERNATIVES= 3;
	
	public static final int TYP_CATEGORY = 1;
	public static final int TYP_QUERY = 2;
	public static final int TYP_SEARCHTERM = 3;
	public static final int TYP_ALTERNATIVES = 4;

	private int typ;
	private int action = ACTION_CATEGORY;
	private String searchTerm;
	private String query;
	private int itemCount = Cfg.API_ITEMCOUNT;
	private List<String> platforms;
	private List<String> licenses;

	public String getGeneratedQuery() {
		String api_url = Cfg.API_URL;
		String query = api_url;
		
		if(typ == TYP_CATEGORY)
		{
			query = query + "software/" + Cfg.API_CATEGORY + "/?platform="
					+ typ + "&count=" + itemCount;
		}
		else if(typ == TYP_QUERY)
		{
			query = query + query;
			if(query.contains("?"))
			{
				query = query + "&count=" + itemCount;
			}
			else
			{
				query = query + "?count=" + itemCount;
			}
		}
		else if(typ == TYP_SEARCHTERM)
		{
			if(Cfg.ACTUAL_PlATFORM == null)
				query = query + "search/software/" + searchTerm + "?count=" + itemCount;
			else
				query = query + "search/software/" + searchTerm + "?count=" + itemCount + "&platform=" + Cfg.ACTUAL_PlATFORM.getIdentifier();
		}
		else if(typ == TYP_ALTERNATIVES)
		{
			query = query + "search/software/" + searchTerm + "?count=" + itemCount;
		}
		
		return query;
	}
	
	public ApplicationFilter(int mCategory, String searchTerm) {
		this.typ = mCategory;
		setSearchTerm(searchTerm);
	}
	
	public ApplicationFilter(int typ, String searchTerm, String query) {
		this.typ = typ;
		this.query = query;
		setSearchTerm(searchTerm);
	}

	public ApplicationFilter(int typ, String searchTerm, String query, int itemCount, List<String> platforms,
			List<String> licenses) {
		this.typ = typ;
		setSearchTerm(searchTerm);
		this.query = query;
		this.itemCount = itemCount;
		this.platforms = platforms;
		this.licenses = licenses;
	}
	public String getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(String searchTerm) {
		searchTerm = searchTerm.replace(" ", "-");
		this.searchTerm = searchTerm;
	}

	public int getTyp() {
		return typ;
	}

	public void setTyp(int typ) {
		this.typ = typ;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public List<String> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(List<String> platforms) {
		this.platforms = platforms;
	}

	public List<String> getLicenses() {
		return licenses;
	}

	public void setLicenses(List<String> licenses) {
		this.licenses = licenses;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
}
