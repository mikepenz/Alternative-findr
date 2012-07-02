package com.tundem.alternativefindr.entity;

public class Platform {
	private String Name;
	private String Identifier;
	
	public Platform(String name, String identifier) {
		Name = name;
		Identifier = identifier;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getIdentifier() {
		return Identifier;
	}
	public void setIdentifier(String identifier) {
		Identifier = identifier;
	}
	
	@Override
	public String toString() {
		return Name;
	}
}
