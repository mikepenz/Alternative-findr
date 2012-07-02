package com.tundem.alternativefindr.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tundem.alternativefindr.cfg.Cfg;
import com.tundem.alternativefindr.entity.Platform;
import com.tundem.log.Lg;

public class PlatformStorage {
	/* SETTINGS */
	private String what = "platforms";

	private File cacheDir;

	public PlatformStorage() {
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

	public List<Platform> readJSON() {
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
			Type collectionType = new TypeToken<List<Platform>>() {
			}.getType();
			return gson.fromJson(input, collectionType);
		} else {
			return null;
		}
	}

	public void writeJSON(List<Platform> platformList) {
		Gson gson = new Gson();
		
		List<Platform> platforms_temp = new LinkedList<Platform>();
		for(Platform p : platformList)
		{
			if(p.getIdentifier().toLowerCase().contains("android"))
			{
				if(p.getIdentifier().toLowerCase().contains("tablet")){
					platforms_temp.add(1, p);
				}
				else
				{
					platforms_temp.add(0, p);
				}
			}
			else
			{
				platforms_temp.add(p);
			}
		}

		final File suspend_f = new File(cacheDir, what);

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		boolean keep = true;

		try {
			fos = new FileOutputStream(suspend_f, false);
			oos = new ObjectOutputStream(fos);
			oos.writeUTF(gson.toJson(platforms_temp));
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