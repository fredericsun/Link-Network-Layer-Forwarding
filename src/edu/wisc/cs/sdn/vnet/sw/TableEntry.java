package edu.wisc.cs.sdn.vnet.sw;

import edu.wisc.cs.sdn.vnet.Iface;
import net.floodlightcontroller.packet.MACAddress;
/**
 * Class to define the data structure which goes into the learning table.
 * @author rkhandelwal
 *
 */
public class TableEntry {

	/**
	 * Mac address of the entry
	 */
	private MACAddress address;
	/**
	 * Interface value of the entry
	 */
	private Iface iface;
	/**
	 * Time to live for the entry
	 */
	long timeStamp;
	
	/**
	 * Number of entries in the table
	 */
	
	/**
	 * constructor for the class
	 */
	public TableEntry(MACAddress add, Iface interFace) {
		this.address = add;
		this.iface = interFace;
		this.timeStamp = System.currentTimeMillis()/1000;
	}
	/*
	 * returns the MacAddress for a partcular entry
	 */
	public MACAddress getAddressFromEntry() {
		return this.address;
	}
	/*
	 * returns the interface for a particular entry
	 */
	public Iface getInterfaceFromEntry() {
		return this.iface;
	}
	
	/*
	 * returns the timeStamp of when the entry was made
	 */
	public long getTimeStamp() {
		return this.timeStamp;
	}
	/*
	 * function to reset the timestamp of the entry to current time
	 */
	public void resetTTL(){
		this.timeStamp = System.currentTimeMillis()/1000;
	}
	
//	public boolean equalTo(TableEntry entry1) {
//		if(entry1.getAddressFromEntry().equals(this.address)) {
//			if(entry1.getInterfaceFromEntry().equals(this.iface)) {
//				return true;
//			}
//			else
//				return false;
//		}
//		return false;
//	}
	

}
