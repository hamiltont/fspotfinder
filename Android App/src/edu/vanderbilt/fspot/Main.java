package edu.vanderbilt.fspot;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Main extends ListActivity {
	private LotArray lots_;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		LotArray lots = new LotArray();
		LotAdapter lotAdapter = new LotAdapter(this, lots);

		setListAdapter(lotAdapter);

		lots_ = lots;
		// Cache.init();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		lots_.testStorage(this);

		final TextView lot_name = (TextView) v.findViewById(R.row.lot_title);
		final String lotId = lots_.findIdByLotName(lot_name.getText()
				.toString());

		final Intent i = new Intent(this, Details.class);
		i.putExtra(Constants.KEY_LOT_NAME, lotId);
		Long lotsKey = System.currentTimeMillis();
		Constants.interActivityStorage.put(lotsKey, lots_);
		i.putExtra(Constants.LOTS, lotsKey);
		
		startActivity(i);
	}
}