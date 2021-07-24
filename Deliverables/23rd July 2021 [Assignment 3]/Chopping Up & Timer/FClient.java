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

				if ((reply.trim().equals("RDT_"+count+"_512_"+"END_"+"CRLF"))&&count<5){
				//if ((reply.trim().equals("RDT_"+count+"_512_"+"END_"+"CRLF"))){ 			
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

		}
		
		 finally {

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
