/**
 * 
 */
package edu.vanderbilt.fspot;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Hamilton Turner
 * 
 */
public class LotAdapter extends ArrayAdapter<Lot> {
	private List<Lot> lots_;
	private LayoutInflater layoutInflater_;
	private static final int rowViewResourceId_ = R.layout.row;

	public LotAdapter(Context context, List<Lot> lots) {
		super(context, R.layout.row, lots);

		layoutInflater_ = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		lots_ = lots;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout view = (RelativeLayout) layoutInflater_.inflate(
				rowViewResourceId_, null);
		final TextView title = (TextView) view.findViewById(R.row.lot_title);
		final TextView permits = (TextView) view.findViewById(R.row.lot_permits);
		final TextView numberSpots = (TextView) view.findViewById(R.row.number_spots);
		
		final Lot l = lots_.get(position);
		title.setText(l.getName());
		permits.setText("F Permit");
		numberSpots.setText("5 spots");
		
		return view;
	}
}
