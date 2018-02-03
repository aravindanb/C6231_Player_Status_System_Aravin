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
import java.util.regex.*;

import Server.GeoGameServerInterface;
import Server.GeoGameServerInterfaceImpl;
import Server.GeoGameServerPorts;
import Server.LogFileHelper;
//Player Client which displays the menu for the Client operations
public class PlayerClient {

	static String userIP = null;
	GeoGameServerInterface serverobj = null;
	static Scanner keyboard = new Scanner(System.in);

	public PlayerClient(String userIP) throws MalformedURLException,
			RemoteException, NotBoundException, Exception {
		//Client RMI lookup eg: If instantiating NA server, then RMI registry URL rmi://localhost:2040/NA
		serverobj = (GeoGameServerInterface) Naming.lookup("rmi://localhost:"
				+ GeoGameServerPorts.resolveIP(userIP).getRMI_PORT() + "/"
				+ GeoGameServerPorts.resolveIP(userIP).name());
	}

	LogFileHelper log = null;
	//Resolve the User IP with appropriate Server
	public static void matchUserIP() {

		System.out.println("-------------Enter Your IP Address------------");
		Scanner input = new Scanner(System.in);
		userIP = input.next();

		try {
			System.out.println("Directing to your corresponding server in :"
					+ GeoGameServerPorts.resolveIP(userIP).ServerName);
		} catch (Exception e) {
			System.out.println("Try Again with proper IP" + e.getMessage());
			matchUserIP();
		}

	}
	//User Input validation(Username and Password) rejects characters less than 6 and more than 15 (alphanumeric UserInput is allowed)
	public static String userInputValidation(String userInput) throws Exception {

		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]{6,15}$");
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
	//Age Validate accepts only integer 
	public static String ageValidation(String age) throws Exception {

		String logMessage = "";

		Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
		Matcher matcher = pattern.matcher(age);
		boolean status = matcher.matches();
		/*
		 * try{
		 * 
		 * //if(!((Integer.parseInt(age)<=100)&&(Integer.parseInt(age)>0))){
		 * //logMessage =" Enter an integer for age between 0 and 100.";
		 * 
		 * //throw new Exception(logMessage); }}catch(NumberFormatException e){
		 * System.out.println(logMessage); ageValidation(keyboard.nextLine());
		 * 
		 * }
		 */

		if (!status) {
			System.out.println(" Enter an integer for age between 0 and 100.");
			ageValidation(keyboard.next());
		}

		return age;

	}

	public void run() {

		String userChoice;

		Scanner inputchoice = new Scanner(System.in);

		do {

			System.out.println("1. Create new Account");
			System.out.println("2. Player Account Sign In");
			System.out.println("3. Player Account Sign Out");
			System.out.println("4. Exit System");

			System.out.println("Enter your choice");
			userChoice = inputchoice.nextLine();
			try {

				String FirstName = "";
				String LastName = "";
				String Age = "";
				String Username = "";
				String Password = "";
				String IPAddress = "";

				boolean status = false;
				LogFileHelper log = null;
				String logMessage = "";

				switch (Integer.parseInt(userChoice.substring(0, 1))) {

				// create Account
				case 1:
					boolean accountCreationStatus = false;

					System.out.println("Enter Your First Name");
					FirstName = inputchoice.nextLine();

					System.out.println("Enter Your Last Name");
					LastName = inputchoice.nextLine();

					System.out.println("Enter Your Age");
					String age = "";
					age = ageValidation(inputchoice.nextLine());
					Age = age;

					System.out.println("Enter Your Username");
					Username = inputchoice.nextLine();
					Username = userInputValidation(Username);

					System.out.println("Enter the Password");
					Password = inputchoice.nextLine();
					Password = userInputValidation(Password);
					// System.out.println("IPAddress");
					// IPAddress = inputchoice.nextLine();
					IPAddress = userIP;
					try {
						accountCreationStatus = serverobj.createPlayerAccount(
								FirstName, LastName, Age, Username, Password,
								IPAddress);
						if (accountCreationStatus) {
							System.out.println(Username
									+ "   New Player Account created");
							logMessage = "new Player Account created";
						}
						log = new LogFileHelper(Username
								+ GeoGameServerPorts.resolveIP(userIP).name()
								+ ".txt", "Create a new Player Account \n",
								Username, logMessage + "\n"
										+ accountCreationStatus);
						log.create();
						
					} catch (Exception e) {
						logMessage = e.getMessage();
						System.out.println(logMessage);
						log = new LogFileHelper(Username
								+ GeoGameServerPorts.resolveIP(userIP).name()
								+ ".txt", "Create a new Player Account \n",
								Username, logMessage + "\n"
										+ accountCreationStatus);
						log.create();

					}

					break;

				// Player Sign in
				case 2:

					boolean signinstatus = false;

					System.out.println("Enter your Username");
					Username = userInputValidation(inputchoice.nextLine());

					System.out.println("Enter your password");
					Password = userInputValidation(inputchoice.nextLine());
					try {
						signinstatus = serverobj.playerSignIn(Username,
								Password, userIP);
						if (signinstatus)
							System.out.println("You are Signed in");
					} catch (Exception e) {
						logMessage = e.getMessage();
						System.out.println(logMessage);
						log = new LogFileHelper(Username
								+ GeoGameServerPorts.resolveIP(userIP).name()
								+ ".txt", "Player Signing in \n", Username,
								logMessage + "\n"
										+ String.valueOf(signinstatus));
						log.create();

					}

					log = new LogFileHelper(Username
							+ GeoGameServerPorts.resolveIP(userIP).name()
							+ ".txt", "Player Signing in \n", Username,
							logMessage + "\n" + String.valueOf(signinstatus));
					log.create();

					break;
				// Player Sign out
				case 3:
					// signoutStatus
					boolean signoutstatus = false;
					System.out.println("Enter Username");
					Username = userInputValidation(inputchoice.nextLine());

					try {
						signoutstatus = serverobj.playerSignOut(Username,
								userIP);

						if (signoutstatus)
							System.out.println("You are Signed Out. Offline");
					}

					catch (Exception e) {
						logMessage = e.getMessage();
						System.out.println(logMessage);
					}

					log = new LogFileHelper(Username
							+ GeoGameServerPorts.resolveIP(userIP).name()
							+ ".txt", "Player Signed Out. Offline \n",
							Username, logMessage + "\n"
									+ String.valueOf(signoutstatus));
					log.create();

					break;

				// Player Exit
				case 4:
					String result = serverobj.getPlayerStatus("Admin", "Admin",
							userIP);
					System.out.println(result);
					System.out.println("System Exiting");
					break;

				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} while (Integer.parseInt(userChoice.substring(0, 1)) != 0);

	}

	public static void main(String[] args) throws RemoteException {

		matchUserIP();

		PlayerClient client;
		try {
			client = new PlayerClient(userIP);
			client.run();
		} catch (MalformedURLException e) {
			System.out
			.println("Administrator Cannot Connect " + e.getMessage());
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out
			.println("Administrator Cannot Connect " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out
			.println("Administrator Cannot Connect " + e.getMessage());
			e.printStackTrace();
		}

	}

}
