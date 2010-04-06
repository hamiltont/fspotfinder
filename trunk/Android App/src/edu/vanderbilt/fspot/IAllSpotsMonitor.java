/**
 * 
 */
package edu.vanderbilt.fspot;

import java.util.Map;

/**
 * @author Hamilton Turner
 *
 */
public interface IAllSpotsMonitor {
	public void acceptNewSpotsInfo(final Map<String, Integer> spotInfo);
}
