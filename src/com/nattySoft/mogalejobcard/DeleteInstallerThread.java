package com.nattySoft.mogalejobcard;

import java.io.File;

import android.os.Environment;

public class DeleteInstallerThread extends Thread {

	@Override
	public void run() {

		try {
			File fileOrDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/mogaleupdate");
			File[] child = fileOrDirectory.listFiles();;
//			do
//				{if (fileOrDirectory.isDirectory())
//	            for (File child : fileOrDirectory.listFiles())
//	                recursiveDelete(child);
//
//				fileOrDirectory.delete();
//				}while
			if(child!=null)
			{
				for (int i = 0; i < child.length; i++) {
					child[i].delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
