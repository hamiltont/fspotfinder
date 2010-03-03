/**
 * 
 */
package edu.vanderbilt.fspot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;

/**
 * @author Hamilton Turner
 * 
 */
public class Lot {
	private String name_;
	private String id_;
	private int spotsAvailable_ = 0;
	private long lastSpotUpdate_ = 0; // local time passed since spots updated
	private long lastImageUpdate_ = 0; // local time since image updated

	public Lot(String name, String identifier) {
		name_ = name;
		id_ = identifier;
	}

	public String getName() {
		return name_;
	}

	public String getId() {
		return id_;
	}

	public void storeImage(String imageData, Context c) {
		Cache.storeImage(imageData, c);
	}

	public int getSpotsOpen() {
		return spotsAvailable_;
	}

	public String getTimeSinceLastSpotUpdate() {
		return getTimeElapsedSince(lastSpotUpdate_);
	}

	public String getTimeSinceLastImageUpdate() {
		return getTimeElapsedSince(lastImageUpdate_);
	}

	private String getTimeElapsedSince(long timePoint) {
		final long timeDifferenceInSecond = (System.currentTimeMillis() - timePoint) / 1000;

		if (timeDifferenceInSecond > 60 * 30) {
			return "> 30 min";
		} else if (timeDifferenceInSecond > 60) {
			final long timeDifferenceInMin = timeDifferenceInSecond / 60;
			final StringBuffer builder = new StringBuffer();
			builder.append(timeDifferenceInMin);
			builder.append(" min");
			return builder.toString();
		} else {
			final StringBuffer builder = new StringBuffer();
			builder.append(timeDifferenceInSecond);
			builder.append(" sec");
			return builder.toString();
		}
	}
	
	public BitmapDrawable getImage(Resources r) {
		URL url = null;
		try {
			url = new URL("http://192.168.18.1/~hamiltont/test.jpg");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// TODO - change this to BitmapDrawable(Resources, InputStream)
		BitmapDrawable image = null;

		try {
			image = new BitmapDrawable(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}
}
