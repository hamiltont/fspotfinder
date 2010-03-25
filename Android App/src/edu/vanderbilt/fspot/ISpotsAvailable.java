/**
 * 
 */
package edu.vanderbilt.fspot;

import android.graphics.drawable.BitmapDrawable;

/**
 * @author Hamilton Turner
 *
 */
public interface ISpotsAvailable {

	public void updateSpotsAvailable(final Integer spotsAvailable);
	
	public void updateImage(final BitmapDrawable newImage);
}
