/**
 * 
 */
package edu.vanderbilt.fspot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

/**
 * Local service intended to poll for the number of available spots. Also keeps
 * track of the latest image timestamp, allowing the app to know when it needs
 * to re-request the lot image
 * 
 * 
 * @author Hamilton Turner
 * 
 */
public class SpotsMonitor extends Service {
	private static final int POLL_PERIOD = 1000; // ms
	private long lastImageTimestamp_ = 0;
	private String lotId;
	public static final String KEY_LOT_ID = "lotid";
	public static final Long KEY_LISTENER = 79879l;
	private ISpotsAvailable listener;

	private SpotsRunnable threadBody = null;

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		lotId = intent.getExtras().getString(KEY_LOT_ID);
		listener = (ISpotsAvailable) Constants.interActivityStorage
				.get(KEY_LISTENER);
		Constants.interActivityStorage.remove(KEY_LISTENER);

		threadBody = new SpotsRunnable();
		new Thread(threadBody).start();

		Log.i("ActivityManager",
				"Started service: edu.vanderbilt.fspot/.SpotsMonitor");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Will cause run() to fall thru, terminating the loop
		threadBody.stop();
	}

	private void update(long newTimestamp, int spots) {
		if (newTimestamp > lastImageTimestamp_) {
			final BitmapDrawable image = getImage();
			if (image != null) {
				lastImageTimestamp_ = newTimestamp;
				listener.updateImage(image);
			}
		}

		listener.updateSpotsAvailable(spots);
	}

	private BitmapDrawable getImage() {
		final StringBuffer urlString = new StringBuffer(Constants.Server_URL);
		urlString.append(Constants.Server_Latest_URI);
		urlString.append("?lot=");
		// urlString.append(lotId); // We only have one image feed, not one for
		// each lot
		urlString.append("fspot");

		URL url = null;
		try {
			url = new URL(urlString.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		BitmapDrawable image = null;
		try {
			image = new BitmapDrawable(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (image.getIntrinsicHeight() == -1) {
			Log.i("fspot", "Bad image returned from server url "
					+ urlString.toString());
			return null;
		}

		return image;
	}

	@Override
	public IBinder onBind(Intent i) {
		return null;
	}

	private class SpotsRunnable implements Runnable {
		private AtomicBoolean active = new AtomicBoolean(true);

		public void run() {
			while (active.get()) {
				// Poll available_spots, send callbacks when new information is
				// available
				final HttpClient c = new DefaultHttpClient();
				final StringBuffer url = new StringBuffer(Constants.Server_URL);
				url.append(Constants.Server_Available_Spots_URI);
				url.append("?lot=");
				url.append(lotId);

				final HttpGet get = new HttpGet(url.toString());

				HttpResponse resp = null;
				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				try {
					resp = c.execute(get);
					resp.getEntity().writeTo(bao);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (resp == null)
					Log.e("fspot",
							"Some error occurred, we had no response from "
									+ url.toString());
				else if (resp.getStatusLine().getStatusCode() != 200)
					Log.e("fspot", "Server returned a Status-Code: "
							+ resp.getStatusLine().getStatusCode() + " from "
							+ url.toString());
				else if (bao.size() == 0)
					Log.e("fspot", "No data sent back from server url at "
							+ url.toString());

				String result = null;
				try {
					result = bao.toString("UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				if (result != null && result.equals("") == false) {
					String[] values = result.split(",");
					final long newTimestamp = Long.parseLong(values[0]);
					final int spots = Integer.parseInt(values[1]);
					update(newTimestamp, spots);
				}

				SystemClock.sleep(POLL_PERIOD);
			}
		}

		public void stop() {
			active.set(false);
		}
	}

}
