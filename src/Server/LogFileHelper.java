/**
COMP:6231_Distributed System_Design
Assignment-1: DPSS(Distributed Player Status System) using Java RMI.
Author:Arvindan Balasubramanian
Student Id: 6591159
Date: 02-02-2014
**/

package Server;
//Common Class which helps for writing logs in client and in server for tracking all the operations carried out in the server
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogFileHelper {

	
	
	String fileName="";
	String Operation ="";
	String invokedBy ="";
	String message="";
	
	BufferedWriter bout;
	PrintWriter pout;
	//int LogID=0;
	
	public LogFileHelper(String fileName, String operation, String invokedBy,
			String message) {
		//super();
		this.fileName = fileName;
		Operation = operation;
		this.invokedBy = invokedBy;
		this.message = message;
		
		
		try {
			bout = new BufferedWriter(new FileWriter(new File(fileName),true));
			pout = new PrintWriter(bout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getOperation() {
		return Operation;
	}
	public void setOperation(String operation) {
		Operation = operation;
	}
	public String getInvokedBy() {
		return invokedBy;
	}
	public void setInvokedBy(String invokedBy) {
		this.invokedBy = invokedBy;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString(){
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return "Log :- \t"+dateFormat.format(cal.getTime())+ "\n"+ this.Operation + "\n" + this.invokedBy + "\n" + this.message+"\n"+ "<----------------------------->";
	}
	public synchronized void create(){
		
		
		pout.println(this.toString());
		pout.flush();
		pout.close();
		try {
			bout.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		
	}
	
}
