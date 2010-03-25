package edu.vanderbilt.fspot;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class Main extends ListActivity implements IAllSpotsMonitor {
	private LotArray lots_;
	private LotAdapter adapter_;
	private TextView timeDisplay_;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		LotArray lots = new LotArray();
		final LotAdapter lotAdapter = new LotAdapter(this, lots);
		setListAdapter(lotAdapter);
		
		timeDisplay_ = (TextView) findViewById(R.main.spot_time_counter);
		
		lots_ = lots;
		adapter_ = lotAdapter;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		final Intent monitor = new Intent(this, AllSpotsMonitor.class);
		Constants.interActivityStorage.put(AllSpotsMonitor.KEY_LISTENER, this);
		startService(monitor);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		final Intent monitor = new Intent(this, AllSpotsMonitor.class);
		stopService(monitor);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		final TextView lot_name = (TextView) v.findViewById(R.row.lot_title);
		final String lotId = lots_.findIdByLotName(lot_name.getText()
				.toString());

		final Intent i = new Intent(this, Details.class);
		i.putExtra(Constants.KEY_LOT_NAME, lotId);
		Long lotsKey = System.currentTimeMillis();
		Constants.interActivityStorage.put(lotsKey, lots_);
		i.putExtra(Constants.LotArrayKey, lotsKey);
		
		startActivity(i);
	}

	public void acceptNewSpotsInfo(Map<String, Integer> spotInfo) {
		for (Map.Entry<String, Integer> entry : spotInfo.entrySet()) {
			final Lot lot = lots_.findLotById(entry.getKey());
			if (lot != null)
				lot.setSpotsOpen(entry.getValue());
		}
		
		final Calendar now = new GregorianCalendar();
		final StringBuffer time = new StringBuffer("Data As of ");
		time.append(now.get(Calendar.HOUR));
		time.append(":");
		if (now.get(Calendar.MINUTE) < 10)
			time.append("0");
		time.append(now.get(Calendar.MINUTE));
		time.append(":");
		if (now.get(Calendar.SECOND) < 10)
			time.append("0");
		time.append(now.get(Calendar.SECOND));
		final String s = time.toString();
		
		runOnUiThread(new Runnable() {
			public void run() {
				adapter_.notifyDataSetChanged();
				timeDisplay_.setText(s);
				timeDisplay_.invalidate();
			}
		});
		
	}
}