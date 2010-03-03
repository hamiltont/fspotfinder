/**
 * 
 */
package edu.vanderbilt.fspot;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Hamilton Turner
 * 
 */
public class Details extends Activity {
	private Lot currentLot_;
	private TextView lotName_;
	private ImageView lotImage_;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		lotName_ = (TextView) findViewById(R.details.lot_name);
		lotImage_ = (ImageView) findViewById(R.details.lot_image);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		final String id = getIntent().getExtras().getString(Constants.KEY_LOT_NAME);
		final Long lotsKey = getIntent().getExtras().getLong(Constants.LOTS);
		final LotArray lots = (LotArray)Constants.interActivityStorage.get(lotsKey);
		Constants.interActivityStorage.remove(lotsKey);
		
		currentLot_ = lots.findLotById(id);
		
		lotName_.setText(currentLot_.getName());
		lotName_.invalidate();
		
		lotImage_.setImageDrawable(currentLot_.getImage(getResources()));
		lotImage_.invalidate();
		
		Log.i("adsf", "force a reinstall!");
		
	}
}
