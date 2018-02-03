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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Server.GeoGameServerInterface;
import Server.GeoGameServerPorts;
import Server.LogFileHelper;
//Admin client to request Player status across the three Geo Servers
public class AdministratorClient {

	static String userIP = null;
	static Scanner keyboard = new Scanner(System.in);
	GeoGameServerInterface serverobj = null;
	LogFileHelper log = null;

	public AdministratorClient(String userIP) throws MalformedURLException,
			RemoteException, NotBoundException, Exception {

		serverobj = (GeoGameServerInterface) Naming.lookup("rmi://localhost:"
				+ GeoGameServerPorts.resolveIP(userIP).getRMI_PORT() + "/"
				+ GeoGameServerPorts.resolveIP(userIP).name());
	}

	public static String userInputValidation(String userInput) throws Exception {

		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]{5,15}$");
		Matcher matcher = pattern.matcher(userInput);
		boolean status = matcher.matches();
		String logMessage = "";
		if (!status) {
			logMessage = "Not a valid entry. Please Try Again";
			System.out.println(logMessage);
			userInputValidation(keyboard.next());
			// throw new Exception(logMessage);
		}

		return userInput;

	}

	public static void matchUserIP() {

		System.out.println("-------------Enter Your IP Address------------");
		Scanner input = new Scanner(System.in);
		userIP = input.next();

		try {
			System.out.println("Directing to your corresponding server in :"
					+ GeoGameServerPorts.resolveIP(userIP).ServerName);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			String logMessage = e.getMessage();
			matchUserIP();
		}

	}

	public void run() throws Exception {

		String userChoice;

		Scanner inputchoice = new Scanner(System.in);

		do {

			System.out.println("1. getPlayerStatus");
			System.out.println("2. Exit System");

			System.out.println("Enter your choice");
			userChoice = inputchoice.nextLine();

			String AdministratorUsername = "";
			String AdministratorPassword = "";

			switch (Integer.parseInt(userChoice.substring(0, 1))) {

			// getplayerstatus
			case 1:
				String playerCountStatus = "";
				System.out.println("Enter the User Name for Administrator");
				try {
					AdministratorUsername = userInputValidation(inputchoice
							.nextLine());
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}

				System.out.println("Enter the Password for Administrator");
				try {
					AdministratorPassword = userInputValidation(inputchoice
							.nextLine());
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
					e1.printStackTrace();
				}

				try {
					if ((AdministratorUsername.equals("Admin") && (AdministratorPassword
							.equals("Admin")))) {

						playerCountStatus = serverobj.getPlayerStatus(
								AdministratorUsername, AdministratorPassword,
								userIP);
						System.out.println(playerCountStatus);

					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
					log = new LogFileHelper(AdministratorUsername
							+ GeoGameServerPorts.resolveIP(userIP).name()
							+ ".txt", "Current Player Status \n",
							AdministratorUsername, "\n " + e.getMessage());
					log.create();
				}

				log = new LogFileHelper(AdministratorUsername
						+ GeoGameServerPorts.resolveIP(userIP).name() + ".txt",
						"Current Player Status \n", AdministratorUsername,
						"\n " + playerCountStatus);
				log.create();

				break;
			case 2:
				System.out.println("System Exiting");
				break;

			}

		} while (Integer.parseInt(userChoice.substring(0, 1)) != 2);
	}

	public static void main(String[] args) {

		matchUserIP();
		try {
			AdministratorClient administratorclient = new AdministratorClient(
					userIP);
			administratorclient.run();
		} catch (MalformedURLException e) {
			System.out
					.println("Administrator Cannot Connect " + e.getMessage());

		} catch (RemoteException e) {
			System.out
					.println("Administrator Cannot Connect " + e.getMessage());

		} catch (NotBoundException e) {
			System.out
					.println("Administrator Cannot Connect " + e.getMessage());

		} catch (Exception e) {
			System.out
					.println("Administrator Cannot Connect " + e.getMessage());

		}
	}

}
