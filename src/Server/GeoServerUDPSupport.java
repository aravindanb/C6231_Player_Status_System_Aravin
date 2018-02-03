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
import java.net.SocketException;
  
  
public class GeoServerUDPSupport extends Thread{
  
      
      
    int UDP_PORT;
    GeoGameServerInterfaceImpl geoserver = null;;
      
      
    public GeoServerUDPSupport(GeoGameServerInterfaceImpl server) throws Exception {
    this.geoserver = server;
    this.UDP_PORT = GeoGameServerPorts.resolveIP(server.getServerIP()).getUDP_PORT();
    }
  
  
  
    public void run(){
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket(this.UDP_PORT);
            byte[] buffer = new byte[10000];
              
            while(true){
                  
                DatagramPacket request = new DatagramPacket(buffer,buffer.length);
                socket.receive(request);
                buffer =( "Number of Online Players : " + String.valueOf(geoserver.countOnline())+"   " + "Number of Offline Players : "+ String.valueOf(geoserver.countOffline())).getBytes();
                DatagramPacket reply = new DatagramPacket(buffer,buffer.length,
                        request.getAddress(),request.getPort());                
                socket.send(reply);
                  
            }
              
              
        }
        catch(SocketException e){
        System.out.println("Socket : " + e.getMessage());   
        }
        catch(IOException e){
            System.out.println("Socket : " + e.getMessage());
        }
          
        finally{
            if(socket!=null){
                socket.close();
            }
                  
        }
    }
}