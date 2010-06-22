package conexoes;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import roteamento.Roteador;

/**
 * 
 * @author Alysson Filgueira e Felipe Barbosa.<br>
 * @version 1.0.0.5 21 de junho de 2010.<br>
 */
public class NoMorto extends No{

	private String avisoRoteadorMorto;
	

	/**
	 * 
	 * @return
	 */
	public String getAvisoRoteadorMorto() {
		return avisoRoteadorMorto;
	}

	/**
	 * 
	 * @param avisoRoteadorMorto
	 */
	public void setAvisoRoteadorMorto(String avisoRoteadorMorto) {
		this.avisoRoteadorMorto = avisoRoteadorMorto;
	}

	/**
	 * 
	 * @param roteador
	 * @param idDestino
	 * @param avisoRoteadorMorto
	 * @throws Exception
	 */
	public NoMorto(Roteador roteador, String idDestino, String avisoRoteadorMorto)
			throws Exception {

		super(roteador, idDestino);
		this.avisoRoteadorMorto = avisoRoteadorMorto;
		executaCliente();
	}

	/**
	 * 
	 */
	public void executaCliente() {
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress
					.getByName(getIpRoteadorDestino());

			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];

			String sentence = getAvisoRoteadorMorto();

			sendData = sentence.getBytes();

			clientSocket.setSoTimeout(1000);
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, Integer
							.parseInt(getPortaDestino()));

			clientSocket.send(sendPacket);

			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			clientSocket.receive(receivePacket);
			clientSocket.close();
		} catch (Exception e) {
		}
	}
}
