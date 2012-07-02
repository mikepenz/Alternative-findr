package com.tundem.alternativefindr.entity;

public class License {
	private String Name;
	private String IsFree;
	
	public License(String name, String isFree) {
		Name = name;
		IsFree = isFree;
	}

	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getIsFree() {
		return IsFree;
	}
	public void setIsFree(String isFree) {
		IsFree = isFree;
	}


	@Override
	public String toString() {
		return Name;
	}
}
