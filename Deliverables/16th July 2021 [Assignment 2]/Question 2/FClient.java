/*

Unfortunately, in the real-world, packets do get lost. 
Consider the case where Frame 1 is lost in transit.  The Sender does not receive  the ACK within a specified time.
Upon timeout, it remembers and re-sends Frame 1.

Up to now, the FServer waits for the next request in the receive blocking call.  We need to implement a timeout on the DatagramSocket - see Lab #9, Exercise #2

        DatagramSocket cs = new DatagramSocket();
        cs.setSoTimeout(3000); // set timeout to 3000ms

In the exception handling of SocketTimeoutException, the FServer needs to re-send the frame that is lost.

(a) Write the pseudo-code for the Sender.

(b) Implement the Stop-and-Wait ARQ with Lost Frame using RDT with Text File demoText.html.  Simulate the lost frames by having the FServer drop or not send them out (see PingServer from Lab #9, Exercise #2).  You may hard-code the frame(s) that will be lost, or use command-line parameters.  

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
