package com.tundem.log;

import android.util.Log;

import com.tundem.alternativefindr.cfg.Cfg;

/**
 * The class Lg
 * 
 * @author Mike Penz
 * @version 1.0
 */
public class Lg {
	public static void e(String msg) {
		// Error = 1
		if (Cfg.DEBUG_OUTPUT && Cfg.DEBUT_OUTPUT_VALUE >= 1)
			Log.e(Cfg.APP_NAME, msg);
	}

	public static void w(String msg) {
		// Warn = 3
		if (Cfg.DEBUG_OUTPUT && Cfg.DEBUT_OUTPUT_VALUE >= 3)
			Log.w(Cfg.APP_NAME, msg);
	}

	public static void i(String msg) {
		// Info = 4
		if (Cfg.DEBUG_OUTPUT && Cfg.DEBUT_OUTPUT_VALUE >= 4)
			Log.i(Cfg.APP_NAME, msg);
	}

	public static void v(String msg) {
		// Verbose = 5
		if (Cfg.DEBUG_OUTPUT && Cfg.DEBUT_OUTPUT_VALUE >= 5)
			Log.v(Cfg.APP_NAME, msg);
	}

	public static void d(String msg) {
		// Debug = 6
		if (Cfg.DEBUG_OUTPUT && Cfg.DEBUT_OUTPUT_VALUE >= 6)
			Log.d(Cfg.APP_NAME, msg);
	}

	@SuppressWarnings("rawtypes")
	public static void e(Class classs, String msg) {
		// Error = 1
		if (Cfg.DEBUG_OUTPUT && Cfg.DEBUT_OUTPUT_VALUE >= 1)
			Log.e(Cfg.APP_NAME, classs.getSimpleName() + " " + msg);
	}

	@SuppressWarnings("rawtypes")
	public static void w(Class classs, String msg) {
		// Warn = 3
		if (Cfg.DEBUG_OUTPUT && Cfg.DEBUT_OUTPUT_VALUE >= 3)
			Log.w(Cfg.APP_NAME, classs.getSimpleName() + " " + msg);
	}

	@SuppressWarnings("rawtypes")
	public static void i(Class classs, String msg) {
		// Info = 4
		if (Cfg.DEBUG_OUTPUT && Cfg.DEBUT_OUTPUT_VALUE >= 4)
			Log.i(Cfg.APP_NAME, classs.getSimpleName() + " " + msg);
	}

	@SuppressWarnings("rawtypes")
	public static void v(Class classs, String msg) {
		// Verbose = 5
		if (Cfg.DEBUG_OUTPUT && Cfg.DEBUT_OUTPUT_VALUE >= 5)
			Log.v(Cfg.APP_NAME, classs.getSimpleName() + " " + msg);
	}

	@SuppressWarnings("rawtypes")
	public static void d(Class classs, String msg) {
		// Debug = 6
		if (Cfg.DEBUG_OUTPUT && Cfg.DEBUT_OUTPUT_VALUE >= 6)
			Log.d(Cfg.APP_NAME, classs.getSimpleName() + " " + msg);
	}
}
