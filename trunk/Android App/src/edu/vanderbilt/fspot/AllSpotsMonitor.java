/**
 * 
 */
package edu.vanderbilt.fspot;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

/**
 * @author Hamilton Turner
 * 
 */
public class AllSpotsMonitor extends Service {
	public static final Long KEY_LISTENER = 983453l;
	private IAllSpotsMonitor listener;
	private AllSpotsRunnable threadBody;

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		listener = (IAllSpotsMonitor) Constants.interActivityStorage
				.get(KEY_LISTENER);
		Constants.interActivityStorage.remove(KEY_LISTENER);

		threadBody = new AllSpotsRunnable();
		new Thread(threadBody).start();
		
		Log.i("ActivityManager", "Started service: edu.vanderbilt.fspot/.AllSpotsMonitor");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Will cause run() to fall thru, terminating the loop
		threadBody.stop();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private void update(Map<String, Integer> values) {
		listener.acceptNewSpotsInfo(values);
	}

	private class AllSpotsRunnable implements Runnable {
		private AtomicBoolean active = new AtomicBoolean(true);
		private static final int POLL_PERIOD = 5 * 1000; // ms
		
		public void run() {
			while (active.get()) {
				final StringBuffer urlString = new StringBuffer(
						Constants.Server_URL);
				urlString.append(Constants.Server_Lots_URI);
				urlString.append("?mobile=1");

				try {
					final DocumentBuilder builder = DocumentBuilderFactory
							.newInstance().newDocumentBuilder();
					final Document document = builder.parse((new URL(urlString.toString())).openStream());
					final NodeList list = document.getElementsByTagName("lot");
					final HashMap<String, Integer> values = new HashMap<String, Integer>();
					for (int i = 0; i < list.getLength(); ++i) {
						final String id = list.item(i).getAttributes().getNamedItem("id").getNodeValue();
						final String spotsStr = list.item(i).getAttributes().getNamedItem("spots").getNodeValue();
						final Integer spots = Integer.parseInt(spotsStr);
						values.put(id, spots);
					}
					
					update(values);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (FactoryConfigurationError e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

				SystemClock.sleep(POLL_PERIOD);

			}
		}

		public void stop() {
			active.set(false);
		}
	}

}
