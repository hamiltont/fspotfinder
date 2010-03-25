/**
 * 
 */
package edu.vanderbilt.fspot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.maps.GeoPoint;



import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author Hamilton Turner
 * 
 */
public class LotArray extends ArrayList<Lot> implements Parcelable {
	private static final long serialVersionUID = -5800952553891601148L;

	public LotArray() {
		Lot l = new Lot("Towers Lot 1", "towers1");
		add(l);
		l = new Lot("Towers Lot 2", "towers2");
		add(l);
		l = new Lot("Kensington", "ks");
		add(l);
		l = new Lot("Frat Row 1", "fr1");
		add(l);
		l = new Lot("Frat Row 2", "fr2");
		add(l);
		l = new Lot("Rec Center", "rec");
		add(l);
	}

	public void updateLotArray() {

		final HttpClient c = new DefaultHttpClient();
		final HttpPost post = new HttpPost(Constants.Server_URL
				+ Constants.Server_Lots_URI);

		HttpResponse resp = null;
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			resp = c.execute(post);
			resp.getEntity().writeTo(bao);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (resp == null) {
			Log.e("tag", "Some error occurred, we had no response");
			return;
		} else if (resp.getStatusLine().getStatusCode() != 200) {
			Log.e("tag", "Server returned a Status-Code: "
					+ resp.getStatusLine().getStatusCode());
			return;
		} else if (bao.size() == 0) {
			Log.e("tag","No data sent back from server");
			return;
		}

		String result = null;
		try {
			result = bao.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.i("tag", result);

	}

	public List<String> getLotNames() {
		final ArrayList<String> lotNames = new ArrayList<String>(size());
		for (Lot l : this)
			lotNames.add(l.getName());
		return lotNames;
	}

	public Lot findLotById(String id) {
		for (Lot l : this)
			if (l.getId().equalsIgnoreCase(id))
				return l;
		return null;
	}

	public String findIdByLotName(String lotName) {
		for (Lot l : this)
			if (l.getName().equalsIgnoreCase(lotName))
				return l.getId();
		return "";
	}

	public void testStorage(Context c) {
		Lot l = get(0);
		l.storeImage("some string image data", c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#describeContents()
	 */
	public int describeContents() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	public void writeToParcel(Parcel dest, int flags) {
	}
}
