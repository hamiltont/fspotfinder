/**
 * 
 */
package edu.vanderbilt.fspot;

import java.util.HashMap;

/**
 * @author Hamilton Turner
 * 
 */
public class Constants {
	//public static final String Server_URL = "http://10.24.70.24/~hamiltont";
	public static final String Server_URL = "http://adam-albright.com/fspot";
	public static final String Server_Images_Folder = "/";
	public static final String Server_Latest_URI = "/latest.php";
	public static final String Server_Lots_URI = "/lots.php";
	public static final String Server_Available_Spots_URI = "/available_spots.php";
	
	public static final String Local_Images_Folder = "/f-spot";

	public static final String KEY_LOT_NAME = "lot name";
	public static final String LOTS = "lots";

	public static final HashMap<Long, Object> interActivityStorage = new HashMap<Long, Object>();
}
