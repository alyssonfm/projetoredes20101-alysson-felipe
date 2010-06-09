package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import router.Router;

public class UDPClient extends Thread {
	private static final int TIMEOUT = 3000; // Resend timeout (ms)
	private static final int MAXTRIES = 5; // Max retransmissions
	public static final String TOKENIZER = "@";
	
	private InetAddress serverAddress;
	private int serverPort;
	private Router router;
	
	public UDPClient(InetAddress serverAddress, int serverPort) {
		this.serverAddress = serverAddress; //Server address
		this.serverPort = serverPort;
	}
	
	public void setRouter(Router router) {
		this.router = router;
	}
	public void run() {
	
		try {
			DatagramSocket socket = new DatagramSocket();
			socket.setSoTimeout(TIMEOUT);
			byte[] bytesToSend = new byte[1024]; //eliminar?
			byte[] bytesToReceive = new byte[1024];
			String stringToSend = router.getId()+TOKENIZER+router.getRouteTable().asString(); //envia tabela
//			System.out.println("NO CLIENT ESTOU ENVIANDO "+stringToSend);
			bytesToSend = stringToSend.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, serverAddress, serverPort); //Sending packet
			DatagramPacket receivePacket = new DatagramPacket(bytesToReceive, bytesToReceive.length);
			int tries = 0;
			boolean receivedResponse = false;
			do { 
				socket.send(sendPacket);
//				System.out.println("Sending para"+sendPacket.getPort());
				socket.receive(receivePacket);
				if(!receivePacket.getAddress().equals(serverAddress))  throw new IOException("Received packet from an unknown source");
//				System.out.println(receivePacket.getPort());
				receivedResponse = true;
				tries++;
				
			} while((!receivedResponse) && (tries < MAXTRIES));
			
			if(!receivedResponse) System.out.println("No response... Giving up!");
			else {
				System.out.println("Success! o//");
				router.setNeighborOn(receivePacket.getPort());
			}
			socket.close();
			
		} catch (SocketTimeoutException e) {
			router.setNeighborOff(serverPort);
		}
		 //Maximum receive blocking time (ms)
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


//		DatagramPacket receivePacket = new DatagramPacket(new byte[bytesToSend.length], bytesToSend.length); //Receiving packet
//		
//		int tries = 0;
//		boolean receivedResponse = false;
//		
//		do {
//			socket.send(sendPacket); // Send the echo string
//			try {
//				socket.receive(receivePacket); // Attempt echo reply reception
//				
//				if(!receivePacket.getAddress().equals(serverAddress)) { // Check source
//					throw new IOException("Received packet from an unknown souce");
//				}
//				receivedResponse = true;
//			} catch (InterruptedIOException e) { //We did not get anything
//				tries++;
//				System.out.println("Timed out, "+ (MAXTRIES - tries) + " more tries...");
//			}
//		} while ((!receivedResponse) && (tries < MAXTRIES));
//		
//		if (receivedResponse) {
//			System.out.println("Received: " + new String(receivePacket.getData()));
//		} else {
//			System.out.println("No response -- giving up.");
//		}
//		socket.close();
//		
	}
}