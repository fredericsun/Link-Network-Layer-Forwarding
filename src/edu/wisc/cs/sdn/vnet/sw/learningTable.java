package edu.wisc.cs.sdn.vnet.sw;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.MACAddress;
import edu.wisc.cs.sdn.vnet.Device;
import edu.wisc.cs.sdn.vnet.DumpFile;
import edu.wisc.cs.sdn.vnet.Iface;
import edu.wisc.cs.sdn.vnet.sw.TableEntry;
import java.util.HashMap;
public class learningTable {
	
	/**
	 * Fixed Size of the table
	 */
	public static final int TABLE_SIZE = 1024;
	//The time in seconds after which an entry is no longer valid in the table
	public static final int BUFFER_TIME=15;
	/**
	 * Create a list of table entries; this list serves as the table
	 */
	private HashMap<MACAddress, TableEntry> table;
	private int numEntries;
	
	public learningTable() {
		this.table = new HashMap<MACAddress, TableEntry>(this.TABLE_SIZE);
		this.numEntries = 0;
	}
	
	public void addEntry(MACAddress add, TableEntry entry) {
		this.table.put(add, entry);
		//this.numEntries = this.numEntries + 1;
	}
	
	public Iface getMatchingIface(MACAddress address) {
		if (this.table.containsKey(address)) {
			if(this.table.get(address).getTimeStamp() - System.currentTimeMillis()/1000 < BUFFER_TIME) {
				return this.table.get(address).getInterfaceFromEntry();
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
	/**
	 * Check if the incoming address and interface are in the learning table, if not
	 * add it, if it exists, reset the TTL 
	 * @param entry
	 */
	public void updateTable(MACAddress add, Iface interFace) {
		this.table.put(add, new TableEntry(add,interFace));
	}
	
}
