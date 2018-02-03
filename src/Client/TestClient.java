/**
COMP:6231_Distributed System_Design
Assignment-1: DPSS(Distributed Player Status System) using Java RMI.
Author:Arvindan Balasubramanian
Date: 02-02-2014
**/

package Client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import Server.GeoGameServerInterface;
import Server.GeoGameServerPorts;
import Server.LogFileHelper;
//Tests Client which runs the client Threads to check multithreading in the server
public class TestClient extends Thread {
	
	
	 String userIP = null;
	 String Username ="";
	 Scanner keyboard = new Scanner(System.in);
	
	GeoGameServerInterface serverobj = null;
	LogFileHelper log = null;

	public TestClient(String userIP ) throws MalformedURLException,
			RemoteException, NotBoundException, Exception {
	this.Username = Username;
	this.userIP = userIP;
	serverobj = (GeoGameServerInterface) Naming.lookup("rmi://localhost:"
				+ GeoGameServerPorts.resolveIP(userIP).getRMI_PORT() + "/"
				+ GeoGameServerPorts.resolveIP(userIP).name());
	}
	
public static void main(String[] args) {
	String userChoice;

	Scanner inputchoice = new Scanner(System.in);
	do {

		System.out.println("1. Concurrency Demo - MulthiThreading, Synchronization");
		System.out.println("0. Exit System");

		System.out.println("Enter your choice");
		userChoice = inputchoice.nextLine();
		try {

			switch (Integer.parseInt(userChoice.substring(0, 1))) {

			// create Account
			case 1:
				TestClient client1 = new TestClient("132.123.11.1");
                TestClient client2 = new TestClient("132.233.22.2"); 
                TestClient client3 = new TestClient("132.112.23.6");
               
                
                TestClient client4 = new TestClient("182.133.45.8");  
                TestClient client5 = new TestClient("182.222.49.9");
                TestClient client6 = new TestClient("182.103.2.2");
                  
                TestClient client7 = new TestClient("93.112.22.4");
                TestClient client8 = new TestClient("93.114.45.6");                     
                TestClient client9 = new TestClient("93.199.67.7");
                 
                
                
                
                client1.start();
                client2.start();
                client3.start();
                client4.start();
                client5.start();
                client6.start();
                client7.start();
                client8.start();
                client8.start();
                client9.start();
                
                
                
                client1.join();client2.join();client3.join();client4.join();client5.join();client6.join();client7.join();
                client8.join();client9.join();
                
                
				break;
			case 0: 
				break;
			}
			} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	} while (Integer.parseInt(userChoice.substring(0, 1)) != 0);

}



public void run() {

	LogFileHelper log = null;
	String logMessage="";
	Boolean status=null;
	try{
		
		
	status = serverobj.createPlayerAccount("Aravindan", "Balasubramanian", "24", "a_balas", "user123", this.userIP);
	log = new LogFileHelper("a_balas"+this.userIP+".txt", "Creating User Account : ", "a_balas",String.valueOf(status));
	log.create();
	

	status = serverobj.playerSignIn("a_balas", "user123", this.userIP);
	log = new LogFileHelper("a_balas"+this.userIP+".txt", "Player Sign IN: ", "a_balas",String.valueOf(status));
	log.create();

	status = serverobj.playerSignOut("a_balas", this.userIP);
	log = new LogFileHelper("a_balas"+this.userIP+".txt", "Player Sign Out : "," a_balas",String.valueOf(status));
	log.create();

	status = serverobj.createPlayerAccount("Neil", "Harris", "30", "npHarris", "user123", this.userIP);
	log = new LogFileHelper("npHarris"+this.userIP+".txt", "Creating User Account : ", "npHarris",String.valueOf(status));
	log.create();
	
	logMessage = serverobj.getPlayerStatus("Admin","Admin", this.userIP);
	log = new LogFileHelper("Admin"+this.userIP+".txt", "Get Player Status : ", "a_balas",logMessage);
    log.create();

	
	
	

	
	
	
	
	
	
}
catch(Exception e){
	
}

	
}



}
