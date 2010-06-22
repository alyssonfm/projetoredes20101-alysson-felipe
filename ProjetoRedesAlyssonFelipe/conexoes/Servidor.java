
package conexoes;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import roteamento.Roteador;
import roteamento.TabelaRoteamento;
import roteamento.Saltos;
import roteamento.Utilitarios;

/**
 * 
 * @author Alysson
 *
 */
public class Servidor extends Thread {
	private Roteador roteador;
	private String porta;


	/**
	 * 
	 */
	public Servidor() {

	}

	/**
	 * 
	 * @param roteador
	 */
	public Servidor(Roteador roteador) {
		super("Servidor Numero porta: [ " + roteador.getPorta() + " ] - IP: "
				+ roteador.getIp());
		
		this.roteador = roteador;
		this.porta = roteador.getPorta();
	}

	/**
	 * 
	 * @return
	 */
	public String getPorta() {
		return porta;
	}

	/**
	 * 
	 * @param porta
	 */
	public void setPorta(String porta) {
		this.porta = porta;
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
	 */
	public void run() {
		while (true) {
			try {
				sleep(10);				
				DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(getPorta()));
				byte[] receiveData = new byte[1024];
				byte[] sendData = new byte[1024];

				while (true) {
					
					DatagramPacket receivePacket = new DatagramPacket(
							receiveData, receiveData.length);
					serverSocket.receive(receivePacket);
					InetAddress IPAddress = receivePacket.getAddress();
					int port = receivePacket.getPort();
					String tabelaRecebidaString = new String(receivePacket
							.getData(), receivePacket.getOffset(),
							receivePacket.getLength());
					if (tabelaRecebidaString.startsWith("!")) {
						StringTokenizer st = new StringTokenizer(
								tabelaRecebidaString, "!");
						String idRoteadorMorto = st.nextToken();
						getRoteador().setaNoInfinito(idRoteadorMorto);
						LinkedList<String> listaReceberam = new LinkedList<String>();
						StringTokenizer stReceberam = new StringTokenizer(st
								.nextToken(), "#");
						while (stReceberam.hasMoreTokens()) {
							listaReceberam.add(stReceberam.nextToken());
						}

						Iterator it = getRoteador().getVizinhos().iterator();
						while (it.hasNext()) {
							String idVizinho = (String) it.next();
							if ((getRoteador().getDistancia(idVizinho) != Utilitarios.INFINITO) && (!listaReceberam.contains(idVizinho)))  {

								Iterator it2 = getRoteador().getVizinhos().iterator();
								while (it2.hasNext()) {
									String idVizinho2 = (String) it2.next();
									if (!listaReceberam.contains(idVizinho2)) {
										listaReceberam.add(idVizinho2);
									}
								}
								String msgRoteadorMorto = Utilitarios.geraMsgRoteadorMorto(listaReceberam,	idRoteadorMorto);

								System.out.println("[" + getRoteador().getNumeroRoteador() + "] Enviou Aviso de Morte do (" + Utilitarios.pegaRoteadorMortoPelaMsg(msgRoteadorMorto) + ") para [" + idVizinho + "]" );
								new NoMorto(getRoteador(), idVizinho, msgRoteadorMorto);
							}
						}
					} else {
						TabelaRoteamento tabelaVizinho = TabelaRoteamento
								.transformaStringParaTabela(tabelaRecebidaString);
						Saltos conjunto = new Saltos();
						conjunto.addSalto(tabelaVizinho.getIdTabela());
						getRoteador().getTabela().adicionaNovoCaminho(
								tabelaVizinho.getIdTabela(),
								Utilitarios.getDistanciaEntreRoteadores(
										getRoteador().getNumeroRoteador(),
										tabelaVizinho.getIdTabela()),
								tabelaVizinho.getIdTabela(),conjunto);
						TabelaRoteamento.comparaTabelas(tabelaVizinho,
								getRoteador().getTabela());
						System.out
								.println("["
										+ getRoteador().getNumeroRoteador()
										+ "] Recebeu  Tabela de Roteamento de ["
										+ Utilitarios
												.pegaDonoPelaMsgTabela(tabelaRecebidaString)
										+ "]");
					}

					DatagramPacket sendPacket = new DatagramPacket(sendData,
							sendData.length, IPAddress, port);
					serverSocket.send(sendPacket);

				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error!!!!");
				new Exception("Error!");
			}
		}
	}

}
