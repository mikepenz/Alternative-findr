package com.tundem.alternativefindr.entity;

import java.util.List;

public class Application {
	private String ID;
	private String Name;
	private String Url;
	private String RelativeUrl;
	private Integer Votes;
	private String IconUrl;
	private List<String> Platforms;
	private String License;
	private String ShortDescription;
	private List<String> Tags;

	public Application(String iD, String name, String url, String relativeUrl, Integer votes, String iconUrl,
			List<String> platforms, String license, String shortDescription, List<String> tags) {
		ID = iD;
		Name = name;
		Url = url;
		RelativeUrl = relativeUrl;
		Votes = votes;
		IconUrl = iconUrl;
		Platforms = platforms;
		License = license;
		ShortDescription = shortDescription;
		Tags = tags;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getRelativeUrl() {
		return RelativeUrl;
	}

	public void setRelativeUrl(String relativeUrl) {
		RelativeUrl = relativeUrl;
	}

	public Integer getVotes() {
		return Votes;
	}

	public void setVotes(Integer votes) {
		Votes = votes;
	}

	public String getIconUrl() {
		return IconUrl;
	}

	public void setIconUrl(String iconUrl) {
		IconUrl = iconUrl;
	}

	public List<String> getPlatforms() {
		return Platforms;
	}

	public void setPlatforms(List<String> platforms) {
		Platforms = platforms;
	}

	public String getLicense() {
		return License;
	}

	public void setLicense(String license) {
		License = license;
	}

	public String getShortDescription() {
		return ShortDescription;
	}

	public void setShortDescription(String shortDescription) {
		ShortDescription = shortDescription;
	}

	public List<String> getTags() {
		return Tags;
	}

	public void setTags(List<String> tags) {
		Tags = tags;
	}

	@Override
	public String toString() {
		return "Application [ID=" + ID + ", Name=" + Name + ", Url=" + Url + ", RelativeUrl=" + RelativeUrl
				+ ", Votes=" + Votes + ", IconUrl=" + IconUrl + ", Platforms=" + Platforms + ", License=" + License
				+ ", ShortDescription=" + ShortDescription + ", Tags=" + Tags + "]";
	}
}
