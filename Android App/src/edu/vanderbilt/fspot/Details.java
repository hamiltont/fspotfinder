/**
 * 
 */
package edu.vanderbilt.fspot;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Hamilton Turner
 * 
 */
public class Details extends Activity implements ISpotsAvailable {
	private Lot currentLot_;
	private TextView lotName_;
	private TextView spotTimeCounter_;
	private TextView imageTimeCounter_;
	private TextView spotsOpen_;
	private ImageView lotImage_;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);

		lotName_ = (TextView) findViewById(R.details.lot_name);
		lotImage_ = (ImageView) findViewById(R.details.lot_image);
		spotTimeCounter_ = (TextView) findViewById(R.details.spot_time_counter);
		imageTimeCounter_ = (TextView) findViewById(R.details.image_time_counter);
		spotsOpen_ = (TextView) findViewById(R.details.spots_open);
	}

	@Override
	protected void onStart() {
		super.onStart();

		final String id = getIntent().getExtras().getString(
				Constants.KEY_LOT_NAME);
		final Long lotsKey = getIntent().getExtras().getLong(Constants.LOTS);
		final LotArray lots = (LotArray) Constants.interActivityStorage
				.get(lotsKey);
		Constants.interActivityStorage.remove(lotsKey);

		currentLot_ = lots.findLotById(id);

		lotName_.setText(currentLot_.getName());
		lotName_.invalidate();
		
		final Intent service = new Intent(this, SpotsMonitor.class);
		final String lotID = currentLot_.getId();
		service.putExtra(SpotsMonitor.KEY_LOT_ID, lotID);
		Constants.interActivityStorage.put(SpotsMonitor.KEY_LISTENER, this);
		this.startService(service);

		lotImage_.setImageDrawable(currentLot_.getImage(getResources()));
		lotImage_.invalidate();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		final Intent service = new Intent(this, SpotsMonitor.class);
		stopService(service);
	}
	
	public void updateImage(final BitmapDrawable newImage) {
		runOnUiThread(new Runnable() {
			
			public void run() {
				final Calendar now = new GregorianCalendar();
				final StringBuffer time = new StringBuffer();
				time.append(now.get(Calendar.HOUR));
				time.append(":");
				final int min = now.get(Calendar.MINUTE);
				if (min < 10)
					time.append("0");
				time.append(min);
				time.append(":");
				final int sec = now.get(Calendar.SECOND);
				if (sec < 10)
					time.append("0");
				time.append(sec);
				
				imageTimeCounter_.setText(time.toString());
				imageTimeCounter_.invalidate();
				
				lotImage_.setImageDrawable(newImage);
				lotImage_.invalidate();
			}
		});
	}

	public void updateSpotsAvailable(final Integer spotsAvailable) {
		Log.i("tag", "Update received + " + spotsAvailable);
		runOnUiThread(new Runnable() {

			public void run() {
				final Calendar now = new GregorianCalendar();
				final StringBuffer time = new StringBuffer();
				time.append(now.get(Calendar.HOUR));
				time.append(":");
				final int min = now.get(Calendar.MINUTE);
				if (min < 10)
					time.append("0");
				time.append(min);
				time.append(":");
				final int sec = now.get(Calendar.SECOND);
				if (sec < 10)
					time.append("0");
				time.append(sec);
				
				spotTimeCounter_.setText(time.toString());
				spotTimeCounter_.invalidate();
				
				spotsOpen_.setText(spotsAvailable.toString());
				spotsOpen_.postInvalidate();
			}
			
		});
	}
}
