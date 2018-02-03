/**
COMP:6231_Distributed System_Design
Assignment-1: DPSS(Distributed Player Status System) using Java RMI.
Author:Arvindan Balasubramanian
Date: 02-02-2014
**/

package Server;

/**
 * User Account  Class
 * */
public class Account {

	
	/**
	 * PlayerStatus = true means online
	 * PlayerStatus = false means offline
	 * */
	Boolean PlayerStatus = false;
	
	String FirstName ="";
	String LastName = "";
	String Age = "";
	String Username = "";
	String Password ="";
	String IPAddress ="";
	
	public Account( String firstName, String lastName,
			String age, String userName, String password, String iPAddress) {
		
		
		FirstName = firstName;
		LastName = lastName;
		Age = age;
		Username = userName;
		Password = password;
		IPAddress = iPAddress;
	}
	
	
	
	public Boolean getPlayerStatus() {
		return PlayerStatus;
	}
	public void setPlayerStatus(Boolean playerStatus) {
		PlayerStatus = playerStatus;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getAge() {
		return Age;
	}
	public void setAge(String age) {
		Age = age;
	}
	public String getUsername() {
		return Username;
	}
	public void setUserName(String userName) {
		Username = userName;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	
	public String getIPAddress() {
		return IPAddress;
	}
	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}
	
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		return sb.append("First Name \t :")
				.append( this.FirstName)
				.append("\n")
				.append("Last Name \t :")
				.append(this.LastName)
				.append("\n")
				.append("Age \t :")
				.append(this.Age)
				.append("\n")
				.append("Username \t:")
				.append(this.Username)
				.append("\n")
				.append("IPAddress \t:")
				.append(this.IPAddress)
				.append("\n")
				.toString();
	}
	
}
