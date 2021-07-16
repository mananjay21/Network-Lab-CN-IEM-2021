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


 
public class FClient extends Thread{
 
	public static void main(String[] args) {
	 
	    DatagramSocket cs = null;
		FileOutputStream fos = null;

		try {

	    	cs = new DatagramSocket();
	 
			byte[] rd, sd;
			String reply;
			DatagramPacket sp,rp;
			int count=0;
			boolean end = false;
			
			System.out.println("REQUEST_"+"demoText.html"+"_CRLF");
			fos = new FileOutputStream("demoText1.html");

			while(!end)
			{
			    //String ack = "" + count;
			    String ack = "ACK_" + count + "_CRLF";
			    	  
				// send ACK      
			    sd=ack.getBytes();	
			    sp=new DatagramPacket(sd,sd.length,InetAddress.getByName("127.0.0.1"),Integer.parseInt("10001"));
			    cs.send(sp);	

				// get next consignment
				rd=new byte[512];
				rp=new DatagramPacket(rd,rd.length); 
			    cs.receive(rp);	

				// concat consignment 
			    reply=new String(rp.getData());	 
			    System.out.println(reply);
				fos.write(rp.getData());

				if (reply.trim().equals("RDT_"+count+"_512_"+"END_"+"CRLF")){ 				
					ack = "ACK_" + 0 + "_CRLF + CLOSE TRANSMISSION";
					sd=ack.getBytes();
					sp=new DatagramPacket(sd,sd.length,InetAddress.getByName("127.0.0.1"),Integer.parseInt("10001"));
					cs.send(sp);			
					end = true;
					try{Thread.sleep(500);}catch(InterruptedException e){System.out.println(e);}
					System.out.println("Waited for 500MS");
					System.exit(0);
}
				count++;
			}

		} catch (IOException ex) {
			System.out.println(ex.getMessage());

		} finally {

			try {
				if (fos != null)
					fos.close();
				if (cs != null)
					cs.close();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
}
