package edu.wisc.cs.sdn.vnet.sw;

import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.MACAddress;
import edu.wisc.cs.sdn.vnet.Device;
import edu.wisc.cs.sdn.vnet.DumpFile;
import edu.wisc.cs.sdn.vnet.Iface;
import edu.wisc.cs.sdn.vnet.sw.learningTable;
import edu.wisc.cs.sdn.vnet.sw.TableEntry;


/**
 * @author Aaron Gember-Jacobson
 */
public class Switch extends Device
{	
	/**
	 * Creates a router for a specific host.
	 * @param host hostname for the router
	 */
	private learningTable table;
	public Switch(String host, DumpFile logfile)
	{
		super(host,logfile);
		this.table = new learningTable();
	}

	/**
	 * Handle an Ethernet packet received on a specific interface.
	 * @param etherPacket the Ethernet packet that was received
	 * @param inIface the interface on which the packet was received
	 */
	public void handlePacket(Ethernet etherPacket, Iface inIface)
	{
		System.out.println("*** -> Received packet: " +
                etherPacket.toString().replace("\n", "\n\t"));
		
		MACAddress incomingMAC = etherPacket.getSourceMAC();
		MACAddress destinationMAC = etherPacket.getDestinationMAC();
		updateTable(incomingMAC, inIface);
		Iface outIface = this.table.getMatchingIface(destinationMAC);
		if (outIface == null) {
			floodTheMessage(etherPacket,inIface);
		}
		else {
			boolean status = sendPacket(etherPacket, outIface);
		}
		
		
		
		/********************************************************************/
		/* TODO: Handle packets                                             */
		
		/********************************************************************/
	}
	
	private void floodTheMessage(Ethernet etherPacket, Iface inIface) {
		for(String key: this.interfaces.keySet()) {
			if (!this.interfaces.get(key).equals(inIface)) {
				sendPacket(etherPacket, this.interfaces.get(key));
			}
		}
	}
	
	private void updateTable(MACAddress add, Iface interFace){
		this.table.updateTable(add, interFace);
	}
}
