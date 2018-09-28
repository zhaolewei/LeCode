package com.zlw.lscode.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyFileUtil {

	public static boolean copyFile(File srcFile, File destFile) {
		if (!srcFile.exists()) {
			return false;
		}

		if (destFile.exists()) {
			destFile.delete();
		} else {
			if (!destFile.getParentFile().exists()) {
				if (!destFile.getParentFile().mkdirs()) {
					return false;
				}
			}
		}

		int byteread = 0;
		InputStream in = null;
		OutputStream out = null;

		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];

			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
