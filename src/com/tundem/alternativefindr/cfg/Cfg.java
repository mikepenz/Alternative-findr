package com.tundem.alternativefindr.cfg;

import com.tundem.alternativefindr.entity.Application;
import com.tundem.alternativefindr.entity.Platform;

public class Cfg {
	//PARAM
	public static Application APP_PARAM;
	
	
	// Programm Statics
	public static String APP_NAME = "Alternative Findr";
	public static String PREFS_NAME = "alternativefindr";

	// Storage Statics
	public static String SD_SAVE_FOLDER = APP_NAME + "/";

	//Contact
	public static String CONTACT = "office@tundem.com";

	// Debugging
	public static String LOG = APP_NAME;
	public static boolean DEBUG_OUTPUT = true;
	public static int DEBUT_OUTPUT_VALUE = 6;

	// API Statics
	public static String API_URL = "http://alternativeto.net/about/api/";
	
	// API Standard COUNT
	public static int API_ITEMCOUNT = 10;
	public static String API_CATEGORY = "recent";
	public static int LOADMORE_VALUE = 10;
	
	// Helper
	public static Platform ACTUAL_PlATFORM;
}
