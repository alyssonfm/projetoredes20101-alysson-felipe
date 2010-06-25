
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
 * Classe Servidor. Classe responsavel por implementar as funcionalidades do servidor UDP do sistema.<br>
 * @author Alysson Filgueira e Felipe Barbosa.<br>
 * @version 1.0.0.5 24 de junho de 2010.<br>
 */
public class Servidor extends Thread {
	private Roteador roteador;
	private String porta;
	private int tempoSleep = 10;


	/**
	 * Construtor padrao da classe.<br>
	 */
	public Servidor() {

	}

	/**
	 * Construtor que recebe um roteador como parametro.<br>
	 * @param roteador o roteador que esta sendo adicionado ao servidor.<br>
	 */
	public Servidor(Roteador roteador) {
		super("Servidor Numero porta: [ " + roteador.getPorta() + " ] - IP: "
				+ roteador.getIp());
		
		this.roteador = roteador;
		this.porta = roteador.getPorta();
	}

	/**
	 * Funcao getPorta. Retorna a porta do roteador que eh passado como parametro no construtor ou no procedimento setPorta.<br>
	 * @return A porta do roteador.<br>
	 */
	public String getPorta() {
		return this.porta;
	}

	/**
	 * Procedimento setPorta. Atualiza a porta do roteador, passando um novo valor para a porta como parametro.<br>
	 * @param porta A nova porta do roteador.<br>
	 */
	public void setPorta(String porta) {
		this.porta = porta;
	}

	/**
	 * Funcao getRoteador. Retorna o roteador que foi passado como parametro no construtor ou no procedimento setRoteador.<br>
	 * @return O roteador.<br>
	 */
	public Roteador getRoteador() {
		return this.roteador;
	}

	/**
	 * Procedimento setRoteador. Atualiza o roteador da rede.<br>
	 * @param roteador O roteador que atualiza o antigo roteador da rede.<br>
	 */
	public void setRoteador(Roteador roteador) {
		this.roteador = roteador;
	}
	
	/**
	 * Funcao getTempoSleep. Funcao que retorna o tempo no qual a thread em execucao dorme (cessa a execucao temporariamente). <br> 
	 * @return O tempo de sleep para a thread atual.<br>
	 */
	public int getTempoSleep(){
		return this.tempoSleep;
	}
	
	/**
	 * Procedimento setTempoSleep. Procedimento no qual podemos atualizar o tempo para sleep da thread do servidor.<br>
	 * @param time O novo tempo para sleep.<br>
	 */
	public void setTempoSleep(int time){
		this.tempoSleep = time;
	}

	/**
	 * Procedimento run. Responsavel pela funcionalidade do servidor, as trocas de pacotes (e mensagens) entre os roteadores. <br>
	 */
	@SuppressWarnings("unchecked")
	public void run() {
		while (true) {
			try {
				sleep(getTempoSleep());				
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
							if ((getRoteador().getDistancia(idVizinho) != Utilitarios.getValorInfinito()) && (!listaReceberam.contains(idVizinho)))  {

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
				System.out.println("Erro!!!!");
				new Exception("Erro durante a execucao do servidor!");
			}
		}
	}

}
