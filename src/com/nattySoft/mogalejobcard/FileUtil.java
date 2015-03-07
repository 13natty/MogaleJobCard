package com.nattySoft.mogalejobcard;

import java.io.File;

public class FileUtil {

	public static void createDirectory(String directoryPath) throws Exception {
		String[] dirs = directoryPath.split("/");
		String currentDir = dirs[0];

		for (int i = 1; i < dirs.length; i++) {
			currentDir += "/" + dirs[i];
			File f = new File(currentDir);
			if (!f.exists()) {
				if (!f.mkdirs()) {
					throw new Exception("Could not create user directory.");
				}
			}
		}
	}
}
