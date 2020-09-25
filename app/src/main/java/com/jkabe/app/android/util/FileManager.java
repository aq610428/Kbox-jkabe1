package com.jkabe.app.android.util;

import android.graphics.Bitmap;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
	private static String PATH = AppInfo.getCompanyName() + File.separator
			+ AppInfo.getFolderName() + File.separator + AppInfo.getUserId();// 基本路径;

	private static String LOG_PATH = AppInfo.getCompanyName()+ File.separator +AppInfo.getFolderName();

	/**
	 * 获取上传的log信息文件路径
	 * @return 文件路径名称
     */
	public static String getLogPath(){
		return getRootFilePath()+LOG_PATH;
	}



	/**
	 * 判断文件夹是否存在，返回文件路径
	 *
	 * @param path
	 * @return
	 */
	private static String exists(String path) {
		if (!new File(path).exists()) {
			new File(path).mkdirs();
			return path;
		} else {
			return path;
		}
	}

	public static String getFilePath() {
		if (hasSDCard()) {
			return exists(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/" + PATH + File.separator);
		} else {
			return null;
		}
	}

	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	public static String getRootFilePath() {
		if (hasSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/";// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath:
			// /data/data/
		}
	}

	public static File screenShot(Bitmap bitmap) {
		try {
			String imagePath = FileManager.getFilePath() + "share.png";
			File file = new File(imagePath);
			FileOutputStream os = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
			os.flush();
			os.close();
			return file;
		} catch (Exception e) {
			LogUtils.e("====screenshot:error====", e.getMessage());
		}
		return null;
	}




}
