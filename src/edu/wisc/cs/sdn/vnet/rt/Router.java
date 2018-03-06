package edu.wisc.cs.sdn.vnet.rt;

import edu.wisc.cs.sdn.vnet.Device;
import edu.wisc.cs.sdn.vnet.DumpFile;
import edu.wisc.cs.sdn.vnet.Iface;

import net.floodlightcontroller.packet.BasePacket;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPacket;
import net.floodlightcontroller.packet.IPv4;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @author Aaron Gember-Jacobson and Anubhavnidhi Abhashkumar
 */
public class Router extends Device
{	
	/** Routing table for the router */
	private RouteTable routeTable;
	
	/** ARP cache for the router */
	private ArpCache arpCache;
	
	/**
	 * Creates a router for a specific host.
	 * @param host hostname for the router
	 */
	public Router(String host, DumpFile logfile)
	{
		super(host,logfile);
		this.routeTable = new RouteTable();
		this.arpCache = new ArpCache();
	}
	
	/**
	 * @return routing table for the router
	 */
	public RouteTable getRouteTable()
	{ return this.routeTable; }
	
	/**
	 * Load a new routing table from a file.
	 * @param routeTableFile the name of the file containing the routing table
	 */
	public void loadRouteTable(String routeTableFile)
	{
		if (!routeTable.load(routeTableFile, this))
		{
			System.err.println("Error setting up routing table from file "
					+ routeTableFile);
			System.exit(1);
		}
		
		System.out.println("Loaded static route table");
		System.out.println("-------------------------------------------------");
		System.out.print(this.routeTable.toString());
		System.out.println("-------------------------------------------------");
	}
	
	/**
	 * Load a new ARP cache from a file.
	 * @param arpCacheFile the name of the file containing the ARP cache
	 */
	public void loadArpCache(String arpCacheFile)
	{
		if (!arpCache.load(arpCacheFile))
		{
			System.err.println("Error setting up ARP cache from file "
					+ arpCacheFile);
			System.exit(1);
		}
		
		System.out.println("Loaded static ARP cache");
		System.out.println("----------------------------------");
		System.out.print(this.arpCache.toString());
		System.out.println("----------------------------------");
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
		
		/********************************************************************/
		/* TODO: Handle packets                                             */
		if (etherPacket.getEtherType() != Ethernet.TYPE_IPv4) {
            //check whether it is IPv4 format or not
        }
        else {
            IPacket pk = etherPacket.getPayload();
            IPv4 p = (IPv4)pk;
            short checksum = p.getChecksum();
            p.resetChecksum();
            IPv4 pp = (IPv4)pk.deserialize(pk.serialize(), 0, pk.serialize().length);
            short checksum_pre = pp.getChecksum();
            if (checksum != checksum_pre) {
                System.out.println("checksum fails");
            }
            //compute and check the checksum (borrow the code from serialize() of IPv4 class)
            else {
                p.setTtl((byte)(p.getTtl() - 1));
                if (p.getTtl() == 0) {
                    //check whether TTL is legal
                }
                else {
                    //update the checksum
                    p.resetChecksum();
                    IPv4 temp = (IPv4)p.deserialize(p.serialize(), 0, p.serialize().length);
                    boolean flag = false;
                    for (Map.Entry<String, Iface> entry : this.interfaces.entrySet()) {
                        if (p.getDestinationAddress() == entry.getValue().getIpAddress()) {
                            flag = true;
                        }
                    }
                    if (flag) {
                        // check whether the packet’s destination IP address matches one of the interface’s IP addresses
                    }
                    else {
                        // IPv4 packets with a correct checksum, TTL > 1 (pre decrement)
                        // and a destination other than one of the router’s interfaces should be forwarded.
                        if (this.getRouteTable().lookup(p.getDestinationAddress()) == null) {

                        }
                        else {
                            RouteEntry e = this.getRouteTable().lookup(p.getDestinationAddress());
                            int addr = 0;
                            if (e.getDestinationAddress() == 0) {
                                addr = p.getDestinationAddress();
                            }
                            else {
                                addr = e.getDestinationAddress();
                            }
                            if (this.arpCache.lookup(addr) == null) {

                            }
                            else {
                                ArpEntry arp = this.arpCache.lookup(addr);
                                //update the destination MAC address
                                etherPacket.setDestinationMACAddress(arp.getMac().toBytes());
                                //update the source MAC address
                                etherPacket.setSourceMACAddress(e.getInterface().getMacAddress().toBytes());
                                //send the packet
                                sendPacket(etherPacket, e.getInterface());
                                return;
                            }
                        }
                    }
                }
            }
        }

		
		/********************************************************************/
	}
}
