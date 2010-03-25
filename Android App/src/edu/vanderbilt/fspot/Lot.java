/**
 * 
 */
package edu.vanderbilt.fspot;


/**
 * @author Hamilton Turner
 * 
 */
public class Lot {
	private String name_;
	private String id_;
	private int spotsAvailable_ = 0;

	public Lot(String name, String identifier) {
		name_ = name;
		id_ = identifier;
	}

	public String getName() {
		return name_;
	}

	public String getId() {
		return id_;
	}

	public int getSpotsOpen() {
		return spotsAvailable_;
	}
	
	public void setSpotsOpen(int spots) {
		spotsAvailable_ = spots;
	}
}
