package conexoes;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Iterator;

import roteamento.Roteador;
import roteamento.TabelaRoteamento;
import roteamento.Utilitarios;

/**
 * Classe No. Classe responsavel por implementar os nos da rede.<br> 
 * @author Alysson Filgueira e Felipe Barbosa<br>
 * @version 1.0.0.5	18 de junho de 2010.<br>
 *
 */

public class No extends Thread{

	protected String portaDestino;
	protected Roteador roteador;
	protected String ipRoteadorDestino;
	protected String idRoteadorDestino;
	protected static int timeout = 1000;
	
	/**
	 * Funcao getTimeOut. Retorna o timeOut do no.<br>
	 * @return TimeOut do no.
	 */
	public static int getTimeOut(){
		return timeout;
	}
	
	/**
	 * Procedimento setTimeOut. Usado para atualizar o valor do timeOut do no, permite tornar o timeOut configuravel.<br>
	 * @param time O valor que atualizara o timeOut do no.
	 */
	public static void setTimeOut(int time){
		timeout = time;
	}

	/**
	 * Funcao getIdRoteadorDestino. Funcao que retorna o id do roteador de destino.
	 * @return idRoteadorDestino - o id do roteador de destino.
	 */
	public String getIdRoteadorDestino() {
		return idRoteadorDestino;
	}

	
	/**
	 * 
	 * @param numeroRoteadorDestino
	 */
	public void setIdRoteadorDestino(String numeroRoteadorDestino) {
		this.idRoteadorDestino = numeroRoteadorDestino;
	}

	/**
	 * 
	 * @return
	 */
	public String getIpRoteadorDestino() {
		return ipRoteadorDestino;
	}

	/**
	 * 
	 * @param ipRoteadorDestino
	 */
	public void setIpRoteadorDestino(String ipRoteadorDestino) {
		this.ipRoteadorDestino = ipRoteadorDestino;
	}

	/**
	 * 
	 * @return
	 */
	public String getPortaDestino() {
		return portaDestino;
	}

	/**
	 * 
	 * @param portaDestino
	 */
	public void setPortaDestino(String portaDestino) {
		this.portaDestino = portaDestino;
	}

	/**
	 * 
	 * @return
	 */
	public Roteador getRoteador() {
		return roteador;
	}

	/**
	 * 
	 * @param roteador
	 */
	public void setRoteador(Roteador roteador) {
		this.roteador = roteador;
	}

	/**
	 * 
	 * @param roteador
	 * @param numeroDestino
	 * @throws Exception
	 */
	public No(Roteador roteador, String numeroDestino) throws Exception {
		
		this.ipRoteadorDestino = (String)Utilitarios.getPortaEIp(numeroDestino).values().toArray()[0];
		this.idRoteadorDestino = numeroDestino;
		this.portaDestino = (String)Utilitarios.getPortaEIp(numeroDestino).keySet().toArray()[0];
		this.roteador = roteador;
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void run() {
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(getIpRoteadorDestino());

			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];

			String sentence;

			sentence = TabelaRoteamento.transformaTabelaParaString(getRoteador()
					.getTabela());

			sendData = sentence.getBytes();

			clientSocket.setSoTimeout(getTimeOut());
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, Integer
							.parseInt(getPortaDestino()));

			clientSocket.send(sendPacket);

			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			clientSocket.receive(receivePacket);
			clientSocket.close();

			System.out.println("{" + getRoteador().getNumeroRoteador()
					+ "{ Enviou Tabela de Roteamento para {"
					+ getIdRoteadorDestino() + "}");

		} catch (SocketTimeoutException stoExcption) {
			getRoteador().setaNoInfinito(idRoteadorDestino);
			String msgRoteadorMorto = Utilitarios.geraMsgRoteadorMorto(
					getRoteador(), idRoteadorDestino);
			try {
				Iterator it = getRoteador().getVizinhos().iterator();
				while (it.hasNext()) {
					String idVizinho = (String) it.next();
					if (getRoteador().getDistancia(idVizinho) != Utilitarios.getValorInfinito()) {
						System.out
								.println("{"
										+ getRoteador().getNumeroRoteador()
										+ "} Enviou Aviso de Morte do ("
										+ Utilitarios
												.pegaRoteadorMortoPelaMsg(msgRoteadorMorto)
										+ ") para [" + idVizinho + "}");
						new NoMorto(getRoteador(), idVizinho, msgRoteadorMorto);
					}
				}
			} catch (Exception e) {
				
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				throw new Exception("Error! :UDPClient");
			} catch (Exception e1) {
			
				e1.printStackTrace();
			}
		}
	}
}
