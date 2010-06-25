
package roteamento;

import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import conexoes.No;
import conexoes.Servidor;

/**
 * Classe Roteador. Classe responsavel por implementar uma abstracao de um roteador real.<br>
 * @author Alysson Filgueira e Felipe Barbosa. <br>
 * @version 1.0.0.5 21 de junho de 2010.
 */
public class Roteador extends TimerTask {

	private Servidor servidor;
	private String numeroRoteador;
	private String porta;
	private String ip;
	private TabelaRoteamento tabela;
	private Vizinhos vizinhos;
	private static int delay = 10000;
	private static int periodo = 10000;

	/**
	 * Construtor default da classe Roteador.
	 */
	public Roteador() {

	}

	/**
	 * Construtor da Classe Roteador.<br>
	 * @param routerNumber - numero do roteador que a classe recebe como parametro.
	 * @throws Exception
	 */
	public Roteador(String routerNumber) throws Exception {
		this.numeroRoteador = routerNumber;
		tabela = inicializaTabela(routerNumber);
		Saltos conjunto = new Saltos();
		conjunto.addSalto(routerNumber);
		tabela.adicionaNovoCaminho(routerNumber, 0, routerNumber, conjunto);
		this.porta = (String)Utilitarios.getPortaEIp(numeroRoteador).keySet().toArray()[0];
		this.ip = (String)Utilitarios.getPortaEIp(numeroRoteador).values().toArray()[0];
		adicionarVizinhos();
		criaServidorETempo();
	}

	/**
	 * Funcao getServidor(). <br> 
	 * @return o servidor da rede.
	 */
	public Servidor getServidor() {
		return servidor;
	}


	/**
	 * Funcao getNumeroRoteador().<br>
	 * @return o numero do roteador.
	 */
	public String getNumeroRoteador() {
		return numeroRoteador;
	}

	/**
	 * Procedimento sendTable
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void TabelaDeEnvio() throws Exception {
		Iterator it = getVizinhos().iterator();
		while (it.hasNext()) {
			String idVizinho = (String) it.next();
			new No(this, idVizinho).start();
		}
	}

	/**
	 * Procedimento setIdRoteador. <br>
	 * @param numeroRoteador - id do roteador que estamos atualizando.
	 */
	public void setIdRoteador(String numeroRoteador) {
		this.numeroRoteador = numeroRoteador;
	}


	/**
	 * Funcao getIp. <br>
	 * @return o ip do roteador.
	 */
	public String getIp() {
		return ip;
	}
	
	/**
	 * Procedimento setTabela. <br>
	 * @param tabela - Tabela de Roteamento a ser atualizada como sendo a tabela do roteador.<br>
	 */
	public void setTabela(TabelaRoteamento tabela) {
		this.tabela = tabela;
	}

	/**
	 * Funcao getDistancia. <br>
	 * @param numeroVizinho - identificador do roteador vizinho, ao qual se deseja obter a distancia para chegar ate ele.<br> 
	 * @return um valor inteiro - a distancia do roteador ate o seu vizinho.
	 */
	public int getDistancia(String numeroVizinho) {
		return this.tabela.getTabela().get(numeroVizinho).getDistancia();
	}

	/**
	 * Procedimento setIp. Seta o ip do roteador. <br>
	 * @param ip - Ip do roteador, caso se deseje atualiza-lo. <br>
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Funcao getPorta. Funcao usada para retornar o numero da porta do roteador.
	 * @return A porta do roteador.
	 */
	public String getPorta() {
		return porta;
	}

	/**
	 * Procedimento setPorta. Usado para setar a porta do roteador.<br>
	 * @param porta A porta do roteador a ser setada. <br>
	 */
	public void setPorta(String porta) {
		this.porta = porta;
	}


	/**
	 * Procedimento setServidor. Seta o servidor da rede.<br>
	 * @param servidor O servidor da rede do roteadro.<br>
	 */
	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}

	/**
	 * Funcao getVizinhos. Usada para retornar os vizinhos do roteador.<br>
	 * @return Os vizinhos do roteador.<br>
	 */
	public Vizinhos getVizinhos() {
		return vizinhos;
	}

	/**
	 * Procedimento setaNoInfinito. Usado para setar a distancia ate um vizinho para o infinito caso nao se tenha a distancia ate ele.<br>
	 * @param numeroVizinho Numero do vizinho cuja distancia sera setada para infinito. Vizinho que nao esta ligado, nao se conhece distancia ate ele. <br>
	 */
	@SuppressWarnings("unchecked")
	public void setaNoInfinito(String numeroVizinho) {
		TabelaRoteamento tabelaRoteamento = getTabela();
		Saltos conjunto = new Saltos();
		conjunto.addSalto("***");
		tabelaRoteamento.adicionaNovoCaminho(numeroVizinho, Utilitarios.getValorInfinito(),
				"***", conjunto);
		Set<String> set = tabela.getTabela().keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			String nextRoteador = (String) it.next();
			SaltosEDistancia ds = tabela.getTabela().get(nextRoteador);
			Saltos conjuntoDeSaltos = ds.getConjuntoDeSaltos();
			if (conjuntoDeSaltos.contem(numeroVizinho)) {
				SaltosEDistancia novoDS = new SaltosEDistancia(Utilitarios.getValorInfinito(),
						"***", conjunto);
				getTabela().adicionaNovoCaminho(novoDS, nextRoteador);
			}
		}

	}
	
	/**
	 * 
	 * @return
	 */
	public static int getDelay(){
		return delay;
	}
	
	/**
	 * 
	 * @param delay
	 */
	public static void setDelay(int d){
		delay = d;
	}
	
	/**
	 * 
	 * @return
	 */
	public static int getPeriodo(){
		return periodo;
	}
	
	/**
	 * 
	 * @param periodo
	 */
	public static void setPeriodo(int p) {
		periodo = p;
	}
	
	/**
	 * Procedimendo criaServidorETempo. Cria o servidor e o time para o roteador.
	 */
	private void criaServidorETempo() {
		servidor = new Servidor(this);
		servidor.start();
		Timer timer = new Timer();
		timer.schedule(this, getDelay(), getPeriodo());
	}
	
	/**
	 * Procedimento setVizinhos. Usado para setar os vizinhos do roteador. <br> 
	 * @param vizinhos Vizinhos do roteador.<br>
	 */
	public void setVizinhos(Vizinhos vizinhos) {
		this.vizinhos = vizinhos;
	}
	
	/**
	 * Procedimento adicionarVizinhos. Adiciona os vizinhos do roteador a sua tabela de vizinhos. <br>
	 * @throws Exception Lanca excecao caso os vizinhos a serem adicionados nao facam parte do arquivo de configuracao dos roteadores.
	 */
	public void adicionarVizinhos() throws Exception {
		vizinhos = Utilitarios.retornarVizinhos(getNumeroRoteador());
	}

	/**
	 * Procedimento atualizaTabela. Usado para atualizar a tabela de roteamento passada como parametro. <br>
	 * @param tabelaOrigem Atualiza a tabela passada como parametro. <br>
	 */
	public void atualizaTabela(TabelaRoteamento tabelaOrigem) {
		getTabela().atualizaTabela(tabelaOrigem);
	}

	/**
	 * Funcao inicializaTabela. Incializa a tabela de roteamento do roteador passado como parametro. <br>
	 * @param numeroRoteador O numero do roteador que sera inicializada a tabela de roteamento.<br>
	 * @return A tabela de roteamento inicial do roteador.<br>
	 * @throws Exception - Lanca excecao caso o numero do roteador passado como parametro nao seja encontrado no arquivo de configuracao dos roteadores. <br>
	 */
	public TabelaRoteamento inicializaTabela(String numeroRoteador) throws Exception {
		return Utilitarios.inicializarTabela(numeroRoteador);
	}

	/**
	 * Procedimento que imprime a tabela de roteamento do roteador, apos passados determinados segundos. <br> 
	 */
	@Override
	public void run() {
		System.out.println(getTabela());
		try {
			TabelaDeEnvio();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Funcao getTabela. Retorna a tabela de roteamento do roteador. <br>
	 * @return a tabela de roteamento do roteador.
	 */
	public TabelaRoteamento getTabela() {
		return tabela;
	}
}
