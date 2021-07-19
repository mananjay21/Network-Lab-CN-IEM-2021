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
				ss.setSoTimeout(3000);
				System.out.println("Server is up....");
				while(true){					
					fis = new FileInputStream("demoText.html");			
					int consignment=0;
					String strConsignment;
					int result = 0; // number of bytes read
		 
					while(true && result!=-1){
						try{

						
						ss.setSoTimeout(3000); ////// Timeout on While True needs to be returned without stopping the code execution
						rd=new byte[100];
						sd=new byte[512];
						 
						rp = new DatagramPacket(rd,rd.length);						 
						ss.receive(rp);
						 
						// get client's consignment request from DatagramPacket
						ip = rp.getAddress(); 
						port =rp.getPort();
						
						{
						System.out.println("Client IP Address = " + ip);
						System.out.println("Client port = " + port);
						System.out.println("REQUEST_"+"demoText.html"+"_CRLF");
						} // General Info Section
						
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

			catch(SocketTimeoutException ex){
				System.out.println("timeout");
			}
			
			
			 
						}	//while(true && result!=-1)
						
					} //while(true)
					
			} //try

			catch (IOException ex) {
				System.out.println(ex.getMessage());
				return;
			}
		
				//catch (SocketTimeoutException ex) {		Skipping this will end the program when Request Times Out	
	//		return;			I need the program to keep running on the event of a Timeout
	//	}
		
		



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

