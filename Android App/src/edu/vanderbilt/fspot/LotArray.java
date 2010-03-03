/**
 * 
 */
package edu.vanderbilt.fspot;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

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

	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	public int describeContents() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	public void writeToParcel(Parcel dest, int flags) {
	}
}
