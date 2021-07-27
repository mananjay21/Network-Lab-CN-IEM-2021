import java.net.*;
import java.io.*;
import java.util.*;
 
public class FServer extends Thread {
 
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("File Name : ");
		String MYFILE = sc.nextLine();
		DatagramSocket ss = null;
		FileInputStream fis = null;
		DatagramPacket rp, sp;
		System.out.println("Port Number : ");
		int portx = sc.nextInt();
		byte[] rd, sd;
		int flag = 0;
		int country = 0;

		InetAddress ip;
		int port;		
					
			try {
				//ss = new DatagramSocket(Integer.parseInt(args[0]));
				
				ss = new DatagramSocket(portx);
				
				System.out.println("Server is up....");
				while(flag==0)
				{
									
					fis = new FileInputStream(MYFILE);			
					int consignment=0;
					String strConsignment;
					int result = 0; // number of bytes read
		 
					while(true && result!=-1){
						try{
							
						if (flag!=0){
							ss.setSoTimeout(3000);
						}
						
						// ss.setSoTimeout(3000); ////// Timeout on While True needs to be returned without stopping the code execution
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
						System.out.println("REQUEST_"+MYFILE+"_CRLF");
						} // General Info Section
						
						strConsignment = new String(rp.getData());
						String filter = strConsignment.trim();
						System.out.println(filter);
						if (filter.length()>14){
							flag = 0;
							System.out.println("Transmission is completed, no packets were dropped.");
							System.exit(0);
						}
						else if(strConsignment!="ACK_0_CRLF + CLOSE TRANSMISSION"){
							flag = 1;
						}	
							
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
							flag = 0;
						}


						
						sp=new DatagramPacket(sd,sd.length,ip,port);						 
						ss.send(sp);
						 
						rp=null;
						sp = null;
						 
						System.out.println("Sent Consignment #" + consignment);
			 }

			catch(SocketTimeoutException ex){
				System.out.println("Acknowledgement Timeout");
				System.out.println("Resending Packet : "+consignment);
				//return;
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

