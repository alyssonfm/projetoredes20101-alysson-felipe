
package roteamento;

import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import conexoes.No;
import conexoes.Servidor;

/**
 * 
 * @author alyssonfm
 *
 */
public class Roteador extends TimerTask {

	private Servidor servidor;

	private String numeroRoteador;

	private String porta;

	private String ip;

	private TabelaRoteamento tabela;

	private Vizinhos vizinhos;

	/**
	 * 
	 */
	public Roteador() {

	}

	/**
	 * 
	 * @param routerNumber
	 * @throws Exception
	 */
	public Roteador(String routerNumber) throws Exception {
		this.numeroRoteador = routerNumber;
		tabela = initializeTable(routerNumber);
		Saltos conjunto = new Saltos();
		conjunto.addSalto(routerNumber);
		tabela.adicionaNovoCaminho(routerNumber, 0, routerNumber, conjunto);
		this.porta = (String)Utilitarios.getPortaEIp(numeroRoteador).keySet().toArray()[0];
		this.ip = (String)Utilitarios.getPortaEIp(numeroRoteador).values().toArray()[0];
		adicionarVizinhos();
		criaServerAndTime();
	}

	/**
	 * 
	 * @return
	 */
	public Servidor getServidor() {
		return servidor;
	}


	/**
	 * 
	 * @return
	 */
	public String getNumeroRoteador() {
		return numeroRoteador;
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void sendTable() throws Exception {
		Iterator it = getVizinhos().iterator();
		while (it.hasNext()) {
			String idVizinho = (String) it.next();
			new No(this, idVizinho).start();
		}
	}

	/**
	 * 
	 * @param numeroRoteador
	 */
	public void setIdRoteador(String numeroRoteador) {
		this.numeroRoteador = numeroRoteador;
	}


	/**
	 * 
	 * @return
	 */
	public String getIp() {
		return ip;
	}
	
	/**
	 * 
	 * @param tabela
	 */
	public void setTabela(TabelaRoteamento tabela) {
		this.tabela = tabela;
	}

	/**
	 * 
	 * @param numeroVizinho
	 * @return
	 */
	public int getDistancia(String numeroVizinho) {
		return this.tabela.getTabela().get(numeroVizinho).getDistancia();
	}

	/**
	 * 
	 * @param ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
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
	 * @param servidor
	 */
	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}

	/**
	 * 
	 * @return
	 */
	public Vizinhos getVizinhos() {
		return vizinhos;
	}

	/**
	 * 
	 * @param numeroVizinho
	 */
	public void setaNoInfinito(String numeroVizinho) {
		TabelaRoteamento tabelaRoteamento = getTabela();
		Saltos conjunto = new Saltos();
		conjunto.addSalto("*");
		tabelaRoteamento.adicionaNovoCaminho(numeroVizinho, Utilitarios.INFINITO,
				"*", conjunto);
		Set set = tabela.getTabela().keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			String nextRoteador = (String) it.next();
			SaltosEDistancia ds = tabela.getTabela().get(nextRoteador);
			Saltos conjuntoDeSaltos = ds.getConjuntoDeSaltos();
			if (conjuntoDeSaltos.contem(numeroVizinho)) {
				SaltosEDistancia novoDS = new SaltosEDistancia(Utilitarios.INFINITO,
						"*", conjunto);
				getTabela().adicionaNovoCaminho(novoDS, nextRoteador);
			}
		}
	

	}
	
	/**
	 * 
	 */
	private void criaServerAndTime() {
		servidor = new Servidor(this);
		servidor.start();
		Timer timer = new Timer();
		timer.schedule(this, 10000, 10000);
	}
	
	/**
	 * 
	 * @param vizinhos
	 */
	public void setVizinhos(Vizinhos vizinhos) {
		this.vizinhos = vizinhos;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void adicionarVizinhos() throws Exception {
		vizinhos = Utilitarios.retornarVizinhos(getNumeroRoteador());
	}

	/**
	 * 
	 * @param tabelaOrigem
	 */
	public void atualizaTabela(TabelaRoteamento tabelaOrigem) {
		getTabela().atualizaTabela(tabelaOrigem);
	}

	/**
	 * 
	 * @param numeroRoteador
	 * @return
	 * @throws Exception
	 */
	public TabelaRoteamento initializeTable(String numeroRoteador) throws Exception {
		return Utilitarios.inicializarTabela(numeroRoteador);
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		System.out.println(getTabela());
		try {
			sendTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	public TabelaRoteamento getTabela() {
		return tabela;
	}
}
