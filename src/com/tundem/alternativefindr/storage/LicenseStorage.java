package com.tundem.alternativefindr.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tundem.alternativefindr.cfg.Cfg;
import com.tundem.alternativefindr.entity.License;
import com.tundem.log.Lg;

public class LicenseStorage {
	/* SETTINGS */
	private String what = "license";

	private File cacheDir;

	public LicenseStorage() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), Cfg.SD_SAVE_FOLDER + what
					+ "Storage/");
		} else {
			Lg.v(getClass(), "no SD Card");
			return;
		}

		if (cacheDir != null) {
			if (!cacheDir.exists())
				cacheDir.mkdirs();
		}
	}

	public List<License> readJSON() {
		Gson gson = new Gson();

		final File suspend_f = new File(cacheDir, what);

		FileInputStream fis = null;
		ObjectInputStream is = null;
		String input = "";

		try {
			fis = new FileInputStream(suspend_f);
			is = new ObjectInputStream(fis);
			input = is.readUTF();
		} catch (Exception e) {
			Lg.v(getClass(), e.getMessage());
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (is != null)
					is.close();

			} catch (Exception e) {
			}
		}

		if (input != "") {
			Type collectionType = new TypeToken<List<License>>() {
			}.getType();
			return gson.fromJson(input, collectionType);
		} else {
			return null;
		}
	}

	public void writeJSON(List<License> applicationList) {
		Gson gson = new Gson();

		final File suspend_f = new File(cacheDir, what);

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		boolean keep = true;

		try {
			fos = new FileOutputStream(suspend_f, false);
			oos = new ObjectOutputStream(fos);
			oos.writeUTF(gson.toJson(applicationList));
		} catch (Exception e) {
			keep = false;
			Lg.e("Error saveObject: " + e.toString());
		} finally {
			try {
				if (oos != null)
					oos.close();
				if (fos != null)
					fos.close();
				if (keep == false)
					suspend_f.delete();
			} catch (Exception e) {
				/* do nothing */
			}
		}
	}
}