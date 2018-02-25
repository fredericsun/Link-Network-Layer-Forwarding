package edu.wisc.cs.sdn.vnet.sw;

import edu.wisc.cs.sdn.vnet.Iface;
import net.floodlightcontroller.packet.MACAddress;

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
	
	
	public TableEntry(MACAddress add, Iface interFace) {
		this.address = add;
		this.iface = interFace;
		this.timeStamp = System.currentTimeMillis()/1000;
	}
	
	public MACAddress getAddressFromEntry() {
		return this.address;
	}
	
	public Iface getInterfaceFromEntry() {
		return this.iface;
	}
	
	
	public long getTimeStamp() {
		return this.timeStamp;
	}
	
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
