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

	/**
	 * Atributos da Classe No usados ao londo dos procedimentos e funcoes que fazem parte da classe.<br>
	 */
	protected String portaDestino;
	protected Roteador roteador;
	protected String ipRoteadorDestino;
	protected String idRoteadorDestino;
	protected static int timeout = 1000;
	
	/**
	 * Construtor da classe.<br>
	 * @param roteador O roteador do no.<br>
	 * @param idRoteadorDestino O id do roteador de destino do no.<br>
	 * @throws Exception Excecao lancada caso ocorra algum problema na chamada da funcao getPortaEIp da classe Utilitatios.<br>
	 */
	public No(Roteador roteador, String idRoteadorDestino) throws Exception {
		
		this.ipRoteadorDestino = (String)Utilitarios.getPortaEIp(idRoteadorDestino).values().toArray()[0];
		this.idRoteadorDestino = idRoteadorDestino;
		this.portaDestino = (String)Utilitarios.getPortaEIp(idRoteadorDestino).keySet().toArray()[0];
		this.roteador = roteador;
	}
	
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
	 * Procedimento setIdRoteadorDestino. Usado para atualizar o identificador do roteador de destino.<br>
	 * @param idRoteadorDestino O identificador do roteador de destino.<br>
	 */
	public void setIdRoteadorDestino(String idRoteadorDestino) {
		this.idRoteadorDestino = idRoteadorDestino;
	}

	/**
	 * Funcao getIpRoteadorDestino. Funcao usada para retornar o ip do roteador de destino.<br>
	 * @return O ip do roteador de destino.<br>
	 */
	public String getIpRoteadorDestino() {
		return ipRoteadorDestino;
	}

	/**
	 * Procedimento setIpRoteadorDestino. Usado para atualizar o ip do roteador de destino.<br>
	 * @param ipRoteadorDestino O ip do roteador de destino.<br>
	 */
	public void setIpRoteadorDestino(String ipRoteadorDestino) {
		this.ipRoteadorDestino = ipRoteadorDestino;
	}

	/**
	 * Funcao getPortaDestino. Funcao que retorna a porta de destino do no.<br>
	 * @return A porta de destino do no.<br>
	 */
	public String getPortaDestino() {
		return portaDestino;
	}

	/**
	 * Procedimento setPortaDestino. Usado para atualizar a porta de destino do no.<br>
	 * @param portaDestino a porta atual de destino do no. <br>
	 */
	public void setPortaDestino(String portaDestino) {
		this.portaDestino = portaDestino;
	}

	/**
	 * Funcao getRoteador. Funcao que retorna o roteador do no.<br>
	 * @return O roteador do no.<br>
	 */
	public Roteador getRoteador() {
		return roteador;
	}

	/**
	 * Procedimento setRoteador. Procedimento usado para atualizar o roteador do no.<br>
	 * @param roteador o novo roteador do no.<br>
	 */
	public void setRoteador(Roteador roteador) {
		this.roteador = roteador;
	}

	/**
	 * Procedimento run. Procedimento no qual o no envia e recebe os pacotes de dados para os outros nos na rede.<br>
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

			System.out.println("[" + getRoteador().getNumeroRoteador()
					+ "] Enviou Tabela de Roteamento para: ["
					+ getIdRoteadorDestino() + "]");

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
								.println("["
										+ getRoteador().getNumeroRoteador()
										+ "] Enviou Aviso de Morte do ("
										+ Utilitarios
												.pegaRoteadorMortoPelaMsg(msgRoteadorMorto)
										+ ") para [" + idVizinho + "]");
						new NoMorto(getRoteador(), idVizinho, msgRoteadorMorto);
					}
				}
			} catch (Exception e) {
				
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				throw new Exception("Erro!: UDPClient");
			} catch (Exception e1) {
			
				e1.printStackTrace();
			}
		}
	}
}
