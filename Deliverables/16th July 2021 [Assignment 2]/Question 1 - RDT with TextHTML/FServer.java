/*

General Protocol Requirements
Client File Request Format

REQUEST filename CRLF, with no spaces between REQUEST, filename and CRLF
Server Message Format

RDT sequence_number payload CRLF, where 
the payload is a byte array of 512, and
the sequence_number represents the consignment number and is an ascending number between 0 and 255 stored in 1 byte


DONE-->
*
* [Last Packet]
At the very last consignment, the message format is as follows:
RDT sequence_number payload END CRLF
In the last consignment, the payload is less than, or equals to 512 bytes.
* 
* [Client Acknowledgement Format
ACK sequence_number CRLF
The sequence_number represents the consignment that the Client is expecting next, so it will be 1, 2, 3, 4, ... until the Client is notified of the last consignment.
Upon receiving the last consignment, the Client sends an ACK with sequence_number 0, waits for 500ms and terminates the connection.
Negative ACK is not required.

Requirements for Stop-and-Wait
Time-Out = 30ms]

*/


import java.net.*;
import java.io.*;
import java.util.*;
 
public class FServer {
 
	public static void main(String[] args) {
 
		DatagramSocket ss = null;
		FileInputStream fis = null;
		DatagramPacket rp, sp;
		byte[] rd, sd;

		InetAddress ip;
		int port;
		
		try {			
			
			//ss = new DatagramSocket(Integer.parseInt(args[0]));
			ss = new DatagramSocket(10001);
			System.out.println("Server is up....");
			while(true){
			// read file into buffer
			fis = new FileInputStream("demoText.html");
			
			int consignment=0;
			String strConsignment;
			int result = 0; // number of bytes read
	 
			while(true && result!=-1){
	 
				rd=new byte[100];
				sd=new byte[512];
				 
				rp = new DatagramPacket(rd,rd.length);
				 
				ss.receive(rp);
				 
				// get client's consignment request from DatagramPacket
				ip = rp.getAddress(); 
				port =rp.getPort();
				System.out.println("Client IP Address = " + ip);
				System.out.println("Client port = " + port);
				System.out.println("REQUEST_"+"demoText.html"+"_CRLF");

				strConsignment = new String(rp.getData());
				System.out.println("CLIENT SENDS : "+(strConsignment));
				String numberOnly= strConsignment.replaceAll("[^0-9]", ""); //EXTRA
				
				//consignment = Integer.parseInt(strConsignment.trim());
				consignment = Integer.parseInt(numberOnly.trim());				
				
				System.out.println("Client ACK = " + consignment);

				// prepare data
				result = fis.read(sd);
				if (result == -1) {
					//sd = new String("END").getBytes(); //RDT sequence_number payload END CRLF
					sd = new String("RDT_"+consignment+"_512_"+"END_"+"CRLF").getBytes(); //RDT sequence_number payload END CRLF					
					consignment = -1;
				}
				sp=new DatagramPacket(sd,sd.length,ip,port);
				 
				ss.send(sp);
				 
				rp=null;
				sp = null;
				 
				System.out.println("Sent Consignment #" + consignment);
	 
			}
		}
		}

		catch (IOException ex) {
			System.out.println(ex.getMessage());

		}

		finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		} //End Finally
		
	} //End Main
}

