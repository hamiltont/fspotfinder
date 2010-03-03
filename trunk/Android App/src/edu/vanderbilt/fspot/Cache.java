/**
 * 
 */
package edu.vanderbilt.fspot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;

/**
 * @author Hamilton Turner
 *
 */
public class Cache {
	private String localImagePath_; // such as /sdcard/f-spot/towers1.jpg
	private String serverImagePath_ = "ss.png"; // such as towers1.jpg
	
	public static void init() {
		
		// Make directory on SD card for cache
		final File SDDirectory = Environment.getExternalStorageDirectory();
		if (SDDirectory.canWrite())
				(new File(SDDirectory, Constants.Local_Images_Folder)).mkdir();

	}
	
	public static void storeImage(String imageData, Context c) {
		/*
		//
		InputStream imageStream = c.getResources().openRawResource(R.drawable.ss);
		
		final StringBuffer fileNameBuffer = new StringBuffer(
				Constants.Local_Images_Folder);
		fileNameBuffer.append("/");
		fileNameBuffer.append(serverImagePath_);
		final String fileName = fileNameBuffer.toString();

		// final boolean storeOnSD = (Environment.getExternalStorageState() ==
		// Environment.MEDIA_MOUNTED);
		// TODO - if store on SD fails, use local space

		final File SDDirectory = Environment.getExternalStorageDirectory();
		try {
			if (SDDirectory.canWrite()) {
				final File imageFile = new File(SDDirectory, fileName);
				final FileWriter imageWriter = new FileWriter(imageFile);
				final BufferedWriter out = new BufferedWriter(imageWriter);
				
				while (imageStream.available() != 0)
					out.write(imageStream.read());
				
				out.flush();
				out.close();
				
				imageStream.close();
			}
		} catch (IOException e) {}
*/
	}
}
