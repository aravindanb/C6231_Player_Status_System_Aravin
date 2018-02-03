package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;
/**
 * 
 * 
 * 
 *
 */
public interface GeoGameServerInterface extends Remote {

	public boolean createPlayerAccount(String FirstName, String LastName,
			String Age, String Username, String Password, String IPAddress)
			throws RemoteException, Exception;

	public boolean playerSignIn(String Username, String Password,
			String IPAddress) throws RemoteException, Exception;

	public boolean playerSignOut(String Username, String IPAddress)
			throws RemoteException, Exception;

	public String getPlayerStatus(String AdminUsername, String AdminPassword,
			String IPAddress) throws RemoteException, Exception;

}
