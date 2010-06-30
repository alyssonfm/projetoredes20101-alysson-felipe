package conexoes;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import excecoes.ClientException;

import roteamento.Roteador;

/**
 * Classe NoMorto. Subclasse da classe No, a classe NoMorto eh usada para 
 * simular o que acontece caso um no morra (nao envie sua tabela de roteamento aos demais nos), 
 * um no ao detectar um no como morto avisa aos seus vizinhos que o no morreu, 
 * para que os mesmos atualizem as suas tabelas de roteamento.<br>
 * @author Alysson Filgueira e Felipe Barbosa.<br>
 * @version 1.0.0.5 29 de junho de 2010.<br>
 */
public class NoMorto extends No{

	private String avisoRoteadorMorto;
	
	/**
	 * Construtor da Classe.<br>
	 * @param roteador o roteador do no morto.<br>
	 * @param idDestino o id do roteador de destino.<br>
	 * @param avisoRoteadorMorto o aviso de que o roteador do no esta morto.<br>
	 * @throws Exception Pode lancar excecao caso a funcao getPortaEIp da classe Utilitarios lance excecao.<br>
	 */
	public NoMorto(Roteador roteador, String idDestino, String avisoRoteadorMorto) throws Exception {
		// Usa o construtor da classe pai para os parametros que sao iguais.
		super(roteador, idDestino);
		// Parte do construtor que difere do construtor da classe pai.
		this.avisoRoteadorMorto = avisoRoteadorMorto;
		executaCliente();
	}
	
	/**
	 * Funcao getAvisoRoteadorMorto. Funcao usada para pegar o aviso de que um no na rede esta morto.<br>
	 * @return o aviso de que o no morreu.<br>
	 */
	public String getAvisoRoteadorMorto() {
		return avisoRoteadorMorto;
	}

	/**
	 * Procedimento setAvisoRoteadorMorto. Usado para atualizar o aviso do roteador morto.<br>
	 * @param avisoRoteadorMorto o aviso do roteador morto.<br>
	 */
	public void setAvisoRoteadorMorto(String avisoRoteadorMorto) {
		this.avisoRoteadorMorto = avisoRoteadorMorto;
	}

	/**
	 * Procedimento executaCliente. Procedimento no qual ocorre a troca de mensagens do NoMorto com os demais nos da rede.<br>
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

			clientSocket.setSoTimeout(getTimeOut());
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, Integer
							.parseInt(getPortaDestino()));

			clientSocket.send(sendPacket);

			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			clientSocket.receive(receivePacket);
			clientSocket.close();
		} catch (Exception e) {
			new ClientException("Nao foi possivel estabelecer conexao");
		}
	}
}
