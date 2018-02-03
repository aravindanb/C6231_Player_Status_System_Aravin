/**
COMP:6231_Distributed System_Design
Assignment-1: DPSS(Distributed Player Status System) using Java RMI.
Author:Arvindan Balasubramanian
Date: 02-02-2014
**/

package Server;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//Implements All the remote Interface Defined in GameServerInterface

public class GeoGameServerInterfaceImpl implements GeoGameServerInterface {

	
	//Hash table Definition
	
	Hashtable<Character, List<Account>> HashTable = new Hashtable<Character, List<Account>>();
	LogFileHelper log = null;
	private String ServerIP;
	

	public GeoGameServerInterfaceImpl(String ipAddress) {
		this.ServerIP = ipAddress;
	}

	
	//Create new player server remote method
	@Override
	public synchronized boolean createPlayerAccount(String FirstName, String LastName,
			String Age, String Username, String Password, String IPAddress)
			throws RemoteException, Exception {

		boolean status = false;
		String logMessage = "";

		Account playerAccount = null;

		playerAccount = new Account(FirstName, LastName, Age, Username,
				Password, IPAddress);
		List<Account> playerList = HashTable.get(Username.toUpperCase().charAt(0));

		// If there is Key for the first character of the Username, check whether the Username already exists in the Hash table
			if (playerList != null) {
		
			for (Account playerAcnt : playerList) {
				if (Username.equals(playerAcnt.getUsername())) {
					logMessage = "Username Exists.Choose Another One!";
					logMessage = logMessage
							+ " \n Account couldn't be created\n"
							+ "Operation Status: " + String.valueOf(status);
					log = new LogFileHelper(GeoGameServerPorts.resolveIP(
							IPAddress).name()
							+ ".txt", "Create a new Player Account \n"
							+ playerAccount, Username, logMessage + "\n"
							+ HashTable);
					log.create();
					throw new Exception(logMessage);
				}
			}

			status = playerList.add(playerAccount);
			
		} 
			// If there is no Key for the first character of the Username create one and return operation status
			else if (playerList == null) {
			playerList = Collections.synchronizedList(new ArrayList<Account>());
			HashTable.put(playerAccount.getUsername().toUpperCase().charAt(0),playerList);

			System.out.println(playerAccount);

			status = playerList.add(playerAccount);
		}
		
		if (status) {
			logMessage = "User Account Successfully created \n"
					+ "Operation Status: " + String.valueOf(status);
		} else {
			logMessage = logMessage + "Account couldn't be created\n"
					+ "Operation Status: " + String.valueOf(status);
		}
		log = new LogFileHelper(GeoGameServerPorts.resolveIP(IPAddress).name()
				+ ".txt", "Create a new Player Account \n" + playerAccount,
				Username, logMessage + "\n" + HashTable);
		log.create();

		System.out.println(HashTable);
		return status;
		
	}
    // Player Sign-In remote method in server
	@Override
	public  boolean playerSignIn(String Username, String Password,
			String IPAddress) throws Exception {

		boolean status = false;
		String logMessage = "";
		List<Account> playerList = HashTable.get(Username.toUpperCase().charAt(0));
        
		//for each account check the player list for matching Username and password and sign-in by setting player status to online(true)
		for (Account account : playerList) {
			if (account.getUsername().equals(Username)) {
				logMessage = "User Account Exists!";
				if (!account.getPassword().equals(Password)) {
					account.setPlayerStatus(false);
					logMessage = logMessage
							+ "\n Wrong PAssword. Try Again! : "
							+ "\nOperation Status" + String.valueOf(status)
							+ " \n Player Online? :"
							+ String.valueOf(account.getPlayerStatus());
					log = new LogFileHelper(GeoGameServerPorts.resolveIP(IPAddress).name()
							+ ".txt", " Player Signing in \n", Username,
							logMessage + "\n");
					log.create();

					throw new Exception(logMessage);

				} else if (account.getPassword().equals(Password)) {
					status = true;
					account.setPlayerStatus(true);
					logMessage = logMessage + "\n You are Signed in  : "
							+ "\n Operation Status : " + String.valueOf(status)
							+ " \n Player Online? : "
							+ String.valueOf(account.getPlayerStatus());
					System.out.println(logMessage);
					break;
				}

			}

			else {
				logMessage = "User Account Doesn't Exist. Enter the correct username \n : "
						+ "Operation Status : "
						+ String.valueOf(status)
						+ "Player Online ? "
						+ String.valueOf(account.getPlayerStatus());
				log = new LogFileHelper(GeoGameServerPorts.resolveIP(IPAddress)
						.name() + ".txt", "Player signing in \n", Username,
						logMessage + "\n");
				log.create();
				throw new Exception(logMessage);
			}
		}

		log = new LogFileHelper(GeoGameServerPorts.resolveIP(IPAddress).name()
				+ ".txt", "Player Signing in\n", Username, logMessage + "\n");
		log.create();

		return status;
	}

	// Player Sign-In remote method in server
	@Override
	public boolean playerSignOut(String Username, String IPAddress)
			throws Exception {

		boolean status = false;
		String logMessage = "";

		List<Account> players = HashTable.get(Username.toUpperCase().charAt(0));
		
		//If list is null; Print "No accounts Found"
		if (players == null) {
			logMessage = "No Accounts Found!" + "\n Operation Status : "
					+ String.valueOf(status) + " \n Player Online? : "
					+ String.valueOf(false);
			throw new Exception(logMessage);
		}
		//for each account check the player list for matching Username and sign out by setting player status to online(true)
		if (players != null) {
			for (Account account : players) {
				if ((account.getUsername().equals(Username))) {
					if ((account.getPlayerStatus().equals(true))) {
						account.setPlayerStatus(false);
						status = true;
						logMessage = logMessage + "\n You are Signed Out  : "
								+ "\n Operation Status : "
								+ String.valueOf(status)
								+ " \n Player Online? : "
								+ String.valueOf(account.getPlayerStatus());
						break;
					}

				}
			}
		}

		try {
			log = new LogFileHelper(GeoGameServerPorts.resolveIP(IPAddress)
					.name() + ".txt", "Player Signing Out\n", Username,
					logMessage + "\n");
			log.create();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return status;
	}

	//This method is invoked by the Admin Client; For requesting the player status across the three Geo Servers (UDP is used to contact all the servers)
	@Override
	public String getPlayerStatus(String AdminUsername, String AdminPassword,
			String IPAddress) throws RemoteException, Exception {

		DatagramSocket clientsocket = null;
		String logMessage = "";/*GeoGameServerPorts.resolveIP(IPAddress).name()
				+ ": \n" + " Number of Online Players : " + countOnline()
				+ "  Number of Offline Players" + countOffline();*/

		for (GeoGameServerPorts geoservers : GeoGameServerPorts.values()) {
			if (!GeoGameServerPorts.resolveIP(IPAddress).equals(IPAddress)) {
				try {
					clientsocket = new DatagramSocket();
					int SERVER_UDP_PORT = geoservers.getUDP_PORT();
					InetAddress hostAddress = InetAddress
							.getByName("localhost");
					
					
					byte[] requestbuffer = "Request".getBytes();
					DatagramPacket request = new DatagramPacket(requestbuffer,
							"Request".length(), hostAddress, SERVER_UDP_PORT);
					clientsocket.send(request);

					byte[] receivebuffer = new byte[1000];
					DatagramPacket reply = new DatagramPacket(receivebuffer,receivebuffer.length);
					clientsocket.receive(reply);

					logMessage = logMessage
							+ "  "
							+ (geoservers.name() + "\t" + new String(
									reply.getData())).trim()+"\n";
				}

				catch (SocketException e) {
					logMessage = "Socket Exception occured: " + e.getMessage();
				} catch (IOException e) {
					logMessage = "IO Exception occured: " + e.getMessage();
				} finally {
					if (clientsocket != null)
						clientsocket.close();
				}

			}
		}

		log = new LogFileHelper(GeoGameServerPorts.resolveIP(IPAddress).name()
				+ ".txt", "Getting Player Status\n", AdminUsername, logMessage
				+ "\n");
		log.create();
		
		return logMessage;
	}
   // Checks for online status and returns the count
	public AtomicInteger countOnline() {

		AtomicInteger count = new AtomicInteger(0);
		Enumeration<List<Account>> values = HashTable.elements();

		while (values.hasMoreElements()) {
			for (Account account : values.nextElement()) {
				if (account.getPlayerStatus().equals(true)) {
					count.getAndIncrement();
				}
			}
		}
		return count;

	}
	// Checks for offline status and returns the count
	public AtomicInteger countOffline() {

		AtomicInteger count = new AtomicInteger(0);
		Enumeration<List<Account>> values = HashTable.elements();

		while (values.hasMoreElements()) {
			for (Account account : values.nextElement()) {
				if (account.getPlayerStatus().equals(false)) {
					// count++;

					count.getAndIncrement();
				}
			}
		}
		return count;

	}

	public String getServerIP() {
		return ServerIP;
	}

	//Main method for the Server; Run this start and to instantiate the server objects in the RMI Registry for all the three servers
	public static void main(String[] args) {

		List<GeoGameServerInterfaceImpl> ServerObjects = new ArrayList<GeoGameServerInterfaceImpl>();
		Registry registry = null;
		try {
			GeoGameServerInterfaceImpl serverobject;

			for (GeoGameServerPorts geoservers : GeoGameServerPorts.values()) {
				serverobject = new GeoGameServerInterfaceImpl(geoservers.getIPAddress());
				ServerObjects.add(serverobject);
			}

			for (GeoGameServerInterfaceImpl object : ServerObjects) {

				Remote remoteobject = UnicastRemoteObject.exportObject(object,GeoGameServerPorts.resolveIP(object.getServerIP()).getRMI_PORT());
				registry = LocateRegistry.createRegistry(GeoGameServerPorts.resolveIP(object.getServerIP()).getRMI_PORT());
				registry.rebind(GeoGameServerPorts.resolveIP(object.getServerIP()).name(), remoteobject);
				System.out.println("Server at : "+ GeoGameServerPorts.resolveIP(object.getServerIP()).getServerName() + " is up and Running");
				
				new GeoServerUDPSupport(object).start();

			}

		} catch (RemoteException e) {
			System.out.println(e.getMessage());
			if (registry != null) {
				for (GeoGameServerInterfaceImpl object : ServerObjects) {
					try {
						registry.unbind(GeoGameServerPorts.resolveIP(
								object.getServerIP()).getServerName());
					} catch (RemoteException e2) {
						System.out.println(e2.getMessage());
					} catch (NotBoundException e1) {
						System.out.println(e1.getMessage());

					} catch (Exception e1) {
						System.out.println(e1.getMessage());
						e1.printStackTrace();
					}
				}
			}

		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

	}

}