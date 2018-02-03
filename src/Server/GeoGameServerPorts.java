/**
COMP:6231_Distributed System_Design
Assignment-1: DPSS(Distributed Player Status System) using Java RMI.
Author:Aravindan Balasubramanian
Date: 02-02-2014
**/
package Server;

import java.util.Scanner;

//Enum class which contains the UDP and RMI ports for the servers
//New server can be added easily
public enum GeoGameServerPorts {

	NA(2020, 2040, "132.xxx.xxx.xxx", "North_America"), EU(2021, 2041,
			"93.xxx.xxx.xxx", "Europe"), AS(2022, 2042, "182.xxx.xxx.xxx",
			"Asia");

	public final int UDP_PORT;
	public final int RMI_PORT;
	public String IPAddress;
	public String ServerName;

	GeoGameServerPorts(int uDP_PORT, int rMI_PORT, String iPAddress,
			String serverName) {
		UDP_PORT = uDP_PORT;
		RMI_PORT = rMI_PORT;
		IPAddress = iPAddress;
		ServerName = serverName;
	}

	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public String getServerName() {
		return ServerName;
	}

	public void setServerName(String serverName) {
		ServerName = serverName;
	}

	public int getUDP_PORT() {
		return UDP_PORT;
	}

	public int getRMI_PORT() {
		return RMI_PORT;
	}

	public static GeoGameServerPorts resolveIP(String IPAddress)
			throws Exception {

		for (GeoGameServerPorts geoServers : GeoGameServerPorts.values()) {

			if (IPAddress.startsWith("132")) {
				return NA;

			}

			else if (IPAddress.startsWith("93")) {
				return EU;

			}

			else if (IPAddress.startsWith("182")) {
				return AS;

			}

		}
		throw new Exception("Invalid GeoServerIP");

	}
}

/*
 * 
 * public static void main(String[] args) {
 * 
 * Scanner in = new Scanner(System.in); System.out.println("Enter the IP");
 * String IP = in.next();
 * 
 * for(GeoGameServerPorts server : GeoGameServerPorts.values()){
 * 
 * if(IP.startsWith("132")){ System.out.println("hurray" + server.NA); break; }
 * 
 * else if(IP.startsWith("93")){ System.out.println("hurray" + server.EU);
 * break;}
 * 
 * 
 * else if(IP.startsWith("182")){ System.out.println("hurray"+ server.AS);
 * break; } }
 * 
 * 
 * }
 */

