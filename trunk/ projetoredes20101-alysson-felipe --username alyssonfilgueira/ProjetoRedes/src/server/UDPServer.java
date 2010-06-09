package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.StringTokenizer;

import client.UDPClient;

import router.Router;


public class UDPServer extends Thread {
	private Router router;
	private int serverPort;

	public UDPServer(int serverPort) {
		this.serverPort = serverPort;
	}

	public void setRouter(Router router) {
		this.router = router;
	}

	public void run() {
		try {
			DatagramSocket socket = new DatagramSocket(serverPort);

			byte[] bytesToSend = new byte[1024];
			byte[] bytesToReceive = new byte[1024];

			DatagramPacket receivePacket = new DatagramPacket(bytesToReceive, bytesToReceive.length);
			while (true) {
				socket.receive(receivePacket);

				InetAddress clientAddress = receivePacket.getAddress();
				int clientPort = receivePacket.getPort();

				String receivedData = new String(receivePacket.getData(),receivePacket.getOffset(), receivePacket.getLength());

//				System.out.println("NO SERVER RECEBI OS DADOS: "+receivedData);

				StringTokenizer st = new StringTokenizer(receivedData, UDPClient.TOKENIZER);
				String neighborID = st.nextToken();
				router.updateTable(neighborID/*, st.nextToken()*/);

				DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, clientAddress, clientPort);

				socket.send(sendPacket);
			}

		} catch (Exception e) {
			
		}
	}
}