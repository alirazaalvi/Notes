package com.ali.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.os.Environment;
import android.util.Log;

public class Utility {

	public static boolean copyFile(String sourceFileName,
			String destinationFileName, String sourceFilePath,
			String destinationFilePath,String sourceDir, String destinationDir,Boolean sysToSd) {
		
		
		
		try {
			
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				File currentDB;
				File backupDB;
				if(sysToSd)
				{
					currentDB = new File(data, sourceFilePath);
					backupDB = new File(sd, destinationFilePath);
				}
				else
				{
					currentDB = new File(sd, sourceFilePath);
					backupDB = new File(data, destinationFilePath);
				}
				
				File backupDBDirectory = new File(sd.toString() + "/"
						+ destinationDir);
				if (!backupDBDirectory.isDirectory()) {
					backupDBDirectory.mkdir();
				}
				
				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB)
							.getChannel();
					FileChannel dst = new FileOutputStream(backupDB)
							.getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			}
		} catch (Exception e) {
			
		}

		return true;
	}
}
