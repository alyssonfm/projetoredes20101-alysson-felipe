
package roteamento;

import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import conexoes.No;
import conexoes.Servidor;

/**
 * Classe Roteador. Classe responsavel por implementar 
 * uma abstracao de um roteador real.<br>
 * @author Alysson Filgueira e Felipe Barbosa. <br>
 * @version 1.0.0.5 25 de junho de 2010.
 */
public class Roteador extends TimerTask {

	private Servidor servidor;
	private String numeroRoteador;
	private String porta;
	private String ip;
	private TabelaRoteamento tabela;
	private Vizinhos vizinhos;
	private static int delay = 6000;
	private static int periodo = 6000;

	/**
	 * Construtor default da classe Roteador.
	 */
	public Roteador() {

	}

	/**
	 * Construtor da Classe Roteador, recebe uma string como parametro, 
	 * o numero do roteador a ser criado.<br>
	 * @param routerNumber - numero do roteador que a classe recebe como parametro. <br>
	 * @throws Exception Excecao que pode ser lancada pelo uso da funcao 
	 * getPortaEIp da classe Utilitarios.<br>
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
	 * Procedimento TabelaDeEnvio, usado para 
	 * criar a tabela de envio do roteador.<br>
	 * @throws Exception Excecao que pode ser lancada pelo uso da funcao 
	 * getPortaEIp da classe Utilitarios.<br>
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
	 * Procedimento setIdRoteador, usado para atualizar o id do roteador. <br>
	 * @param numeroRoteador - id do roteador que estamos atualizando.<br>
	 */
	public void setIdRoteador(String numeroRoteador) {
		this.numeroRoteador = numeroRoteador;
	}


	/**
	 * Funcao getIp. <br>
	 * @return o ip do roteador.<br>
	 */
	public String getIp() {
		return ip;
	}
	
	/**
	 * Procedimento setTabela, atualiza a tabela de roteamento do servidor, 
	 * usada a medida que o roteador atualiza a sua tabela no decorrer 
	 * da execucao do programa. <br>
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
	 * Procedimento setIp. Atualiza o ip do roteador. <br>
	 * @param ip - Ip do roteador, caso se deseje atualiza-lo. <br>
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Funcao getPorta. Funcao usada para retornar a porta do roteador.<br>
	 * @return A porta do roteador.<br>
	 */
	public String getPorta() {
		return porta;
	}

	/**
	 * Procedimento setPorta. Usado para atualizar a porta do roteador.<br>
	 * @param porta A porta do roteador a ser atualizada. <br>
	 */
	public void setPorta(String porta) {
		this.porta = porta;
	}


	/**
	 * Procedimento setServidor. Atualiza o servidor da rede.<br>
	 * @param servidor O servidor da rede do roteador.<br>
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
	 * Procedimento setaNoInfinito. Usado para setar a distancia ate 
	 * um vizinho para o infinito caso nao se tenha a distancia ate ele.<br>
	 * @param numeroVizinho Numero do vizinho cuja distancia 
	 * sera setada para infinito. Vizinho que nao esta ligado ou 
	 * nao se conhece distancia ate ele. <br>
	 */
	@SuppressWarnings("unchecked")
	public void setaNoInfinito(String numeroVizinho) {
		TabelaRoteamento tabelaRoteamento = getTabela();
		Saltos conjunto = new Saltos();
		conjunto.addSalto("*");
		tabelaRoteamento.adicionaNovoCaminho(numeroVizinho, Utilitarios.getValorInfinito(),
				"*", conjunto);
		Set<String> set = tabela.getTabela().keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			String nextRoteador = (String) it.next();
			SaltosEDistancia ds = tabela.getTabela().get(nextRoteador);
			Saltos conjuntoDeSaltos = ds.getConjuntoDeSaltos();
			if (conjuntoDeSaltos.contem(numeroVizinho)) {
				SaltosEDistancia novoDS = new SaltosEDistancia(Utilitarios.getValorInfinito(),
						"*", conjunto);
				getTabela().adicionaNovoCaminho(novoDS, nextRoteador);
			}
		}

	}
	
	/**
	 * Funcao getDelay. Usada para retornar o tempo de delay do roteador.<br>
	 * @return o tempo de delay do roteador.<br>
	 */
	public static int getDelay(){
		return delay;
	}
	
	/**
	 * Procedimento setDelay, usado para atualizar o tempo para delay
	 * do roteador, permite configurar o tempo usado pelo roteador
	 * para realizar a troca de mensagens com os demais roteadores
	 * da rede.<br>
	 * @param delay o tempo de delay que sera atualizado.<br>
	 */
	public static void setDelay(int d){
		delay = d;
	}
	
	/**
	 * Funcao getPeriodo, usada para retornar o tempo de periodo do roteador.<br>
	 * @return o tempo do periodo do roteador.<br>
	 */
	public static int getPeriodo(){
		return periodo;
	}
	
	/**
	 * Procedimento usado para atualizar o tempo para o periodo
	 * do roteador, permite configurar o tempo usado pelo roteador
	 * para realizar a troca de mensagens com os demais roteadores
	 * da rede.<br>
	 * @param periodo o tempo para o periodo que sera atualizado.<br>
	 */
	public static void setPeriodo(int p) {
		periodo = p;
	}
	
	/**
	 * Procedimendo criaServidorETempo. 
	 * Cria o servidor e o tempo de troca das mensagens do roteador.<br>
	 */
	private void criaServidorETempo() {
		servidor = new Servidor(this);
		servidor.start();
		Timer timer = new Timer();
		timer.schedule(this, getDelay(), getPeriodo());
	}
	
	/**
	 * Procedimento setVizinhos. Usado para atualizar
	 *  os vizinhos do roteador. <br> 
	 * @param vizinhos Vizinhos do roteador a serem atualizados.<br>
	 */
	public void setVizinhos(Vizinhos vizinhos) {
		this.vizinhos = vizinhos;
	}
	
	/**
	 * Procedimento adicionarVizinhos. 
	 * Adiciona os vizinhos do roteador 
	 * a sua tabela de vizinhos. <br>
	 * @throws Exception Lanca excecao caso 
	 * os vizinhos a serem adicionados nao facam 
	 * parte do arquivo de configuracao dos roteadores.<br>
	 */
	public void adicionarVizinhos() throws Exception {
		vizinhos = Utilitarios.retornarVizinhos(getNumeroRoteador());
	}

	/**
	 * Procedimento atualizaTabela. 
	 * Usado para atualizar a tabela de roteamento 
	 * passada como parametro. <br>
	 * @param tabelaOrigem Atualiza a tabela passada como parametro. <br>
	 */
	public void atualizaTabela(TabelaRoteamento tabelaOrigem) {
		getTabela().atualizaTabela(tabelaOrigem);
	}

	/**
	 * Funcao inicializaTabela. Incializa a tabela de 
	 * roteamento do roteador passado como parametro. <br>
	 * @param numeroRoteador O numero do roteador que sera 
	 * inicializada a tabela de roteamento.<br>
	 * @return A tabela de roteamento inicial do roteador.<br>
	 * @throws Exception - Lanca excecao caso o numero do 
	 * roteador passado como parametro nao seja encontrado 
	 * no arquivo de configuracao dos roteadores. <br>
	 */
	public TabelaRoteamento inicializaTabela(String numeroRoteador) throws Exception {
		return Utilitarios.inicializarTabela(numeroRoteador);
	}

	/**
	 * Procedimento que imprime a tabela de roteamento do roteador, 
	 * apos passados determinados segundos. <br> 
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
	 * @return a tabela de roteamento do roteador.<br>
	 */
	public TabelaRoteamento getTabela() {
		return tabela;
	}
}
