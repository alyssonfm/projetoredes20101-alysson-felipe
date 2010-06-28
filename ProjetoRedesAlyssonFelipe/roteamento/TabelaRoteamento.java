
package roteamento;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Classe TabelaRoteamento, classe que implementa 
 * a tabela de roteamento dos roteadores.<br>
 * @author Alysson Filgueira e Felipe Barbosa.<br>
 * @version 1.0.0.5 25 de junho de 2010.<br>
 */
public class TabelaRoteamento {

	private String numeroTabela;	
	private HashMap<String, SaltosEDistancia> tabela;
	
	/**
	 * Construtor default da classe, cria um HashMap contendo uma string que representa o 
	 * roteador e um objeto SaltosEDistancia que representa o conjunto de saltos e a 
	 * distancia de cada salto.<br>
	 */
	public TabelaRoteamento(){
		tabela = new HashMap<String, SaltosEDistancia>();
	}
	
	/**
	 *  Construtor da Classe que recebe como parametro uma string o identificador
	 *   da tabela.<br>
	 * @param idTabela o identificador da tabela.<br>
	 */
	public TabelaRoteamento(String idTabela){
		tabela = new HashMap<String, SaltosEDistancia>();
		this.numeroTabela = idTabela;
	}
	
	/**
	 * Procedimento toString, usado para criar a string 
	 * representando a tabela de roteamento de um roteador.<br>
	 */
	@SuppressWarnings("unchecked")
	public String toString(){
		Set<String> set = tabela.keySet();
		Iterator it = set.iterator();
		String resultado = "Tabela [" + getIdTabela() + "] \n";
		while(it.hasNext()){
			String next = (String) it.next();
			SaltosEDistancia distAndSalto = tabela.get(next);
			resultado += "[ " + next + " ] = " + distAndSalto.getDistancia() + " Unidades de Distancia - Proximo Salto: [" + distAndSalto.getSalto() + "]\n"; 
		}
		return resultado;
	}
	
	/**
	 * Funcao getDistancia retora a distancia entre o roteador dono da tabela e outro 
	 * roteador da rede.<br>
	 * @param roteador o roteador que se deseja conhecer a distancia.<br>
	 * @return a distancia entre o roteador da tabela e o passado como parametro.<br>
	 */
	public int getDistancia(String roteador){
		return tabela.get(roteador).getDistancia();
	}
	

	/**
	 * Funcao getIdTabela, retorna o identificador da tabela.<br>
	 * @return o numero de identificacao da tabela.<br>
	 */
	public String getIdTabela() {
		return numeroTabela;
	}


	/**
	 * Procedimento setIdTabela, permite atualizar o identificador da tabela.<br>
	 * @param idTabela o numero de identificacao da tabela que sera atualizado.<br>
	 */
	public void setIdTabela(String idTabela) {
		this.numeroTabela = idTabela;
	}


	/**
	 * Funcao getTabela, retorna a tabela de roteamento de um roteador.<br>
	 * @return a tabela de roteamento.<br>
	 */
	public HashMap<String, SaltosEDistancia> getTabela() {
		return tabela;
	}


	/**
	 * Procedimento setTabela, permite a atualizacao da tabela de roteamento 
	 * de um roteador.<br>
	 * @param tabela a nova tabela de roteamento de um roteador.<br>
	 */
	public void setTabela(HashMap<String, SaltosEDistancia> tabela) {
		this.tabela = tabela;
	}
	
	/**
	 * Funcao tabelaParaString, funcao que realiza a conversao 
	 * de uma tabela de roteamento para uma string.<br>
	 * @param tabela a tabela de roteamento a ser convertida para string.<br>
	 * @return a string que contem os valores da tabela de roteamento.<br>
	 */
	@SuppressWarnings("unchecked")
	public static String tabelaParaString(TabelaRoteamento tabela){
		Set<String> set = tabela.getTabela().keySet();
		Iterator it = set.iterator();
		String resultado = tabela.getIdTabela() + "#";
		while(it.hasNext()){
			String nextRoteador = (String) it.next();
			SaltosEDistancia ds = tabela.getTabela().get(nextRoteador);
			resultado += nextRoteador + "@" + ds.getDistancia() + "@" + ds.getSalto() + "@";
			Iterator itSaltos = ds.getConjuntoDeSaltos().iterator();
			while(itSaltos.hasNext()){
				resultado += itSaltos.next() + "-";
			}
			resultado += "#";
		}
		return resultado;
	}
	
	/**
	 * Procedimento comparaTabelas, compara duas tabelas, adicionando informacoes
	 * entre as tabelas, caso a tabela de destino nao possua determinado roteador
	 * adiciona-o, caso possua compara as distancias.<br>
	 * @param origem a tabela de origem.<br>
	 * @param destino a tabela de destino.<br>
	 */
	@SuppressWarnings("unchecked")
	public static void comparaTabelas(TabelaRoteamento origem, TabelaRoteamento destino){
		Set<String> setOrigem = origem.getTabela().keySet();
		Iterator it = setOrigem.iterator();
		while (it.hasNext()){
			String nextOrigem = (String) it.next();
			SaltosEDistancia dsOrigem = origem.getTabela().get(nextOrigem);
			int distancia1, distancia2;
			distancia1 = destino.getDistancia(nextOrigem);
			distancia2 = destino.getDistancia(origem.getIdTabela()) + dsOrigem.getDistancia();
			if (!destino.getTabela().containsKey(nextOrigem)){
				Saltos conjunto = new Saltos();
				conjunto.addSalto(nextOrigem);
				SaltosEDistancia novoDS = new SaltosEDistancia(destino.getDistancia(origem.getIdTabela()) + dsOrigem.getDistancia(), nextOrigem,conjunto);
				destino.adicionaNovoCaminho(novoDS, nextOrigem);
			} else {
				if (distancia1 > distancia2){
					Saltos conjunto = dsOrigem.getConjuntoDeSaltos();
					conjunto.addSalto(origem.getIdTabela());
					SaltosEDistancia novoDS = new SaltosEDistancia(distancia2, origem.getIdTabela(),conjunto);
					destino.adicionaNovoCaminho(novoDS, nextOrigem);
				}
			}
		}
	}
	
	/**
	 * Procedimento atualizaTabela, recebe uma tabela origem como parametro, 
	 * e atualiza as distancias e os saltos para os roteadores 
	 * contidos na mesma.<br>
	 * @param origem a tabela de roteamento que sera atualizada.<br>
	 */
	@SuppressWarnings("unchecked")
	public void atualizaTabela(TabelaRoteamento origem){
		Set<String> setOrigem = origem.getTabela().keySet();
		Iterator it = setOrigem.iterator();
		while (it.hasNext()){
			String nextOrigem = (String) it.next();
			SaltosEDistancia dsOrigem = origem.getTabela().get(nextOrigem);
			if (!this.getTabela().containsKey(nextOrigem)){
				Saltos conjunto = new Saltos();
				conjunto.addSalto(nextOrigem);
				SaltosEDistancia novoDS = new SaltosEDistancia(this.getDistancia(origem.getIdTabela()) + dsOrigem.getDistancia(), nextOrigem,conjunto);
				this.adicionaNovoCaminho(novoDS, nextOrigem);
			} else {
				if (this.getDistancia(nextOrigem) >  this.getDistancia(origem.getIdTabela()) + dsOrigem.getDistancia()){
					Saltos conjunto = dsOrigem.getConjuntoDeSaltos();
					conjunto.addSalto(nextOrigem);
					SaltosEDistancia novoDS = new SaltosEDistancia(this.getDistancia(origem.getIdTabela()) + dsOrigem.getDistancia(), origem.getIdTabela(),conjunto);
					this.adicionaNovoCaminho(novoDS, nextOrigem);
				}
			}
		}
	}
	
	/**
	 * Funcao stringParaTabela, realiza a conversao de uma 
	 * string para uma tabela de roteamento.<br>
	 * @param string a string a ser convertida.<br>
	 * @return a tabela de roteamento correspondente a string passada
	 * como parametro.<br>
	 */
	public static TabelaRoteamento stringParaTabela(String string){
		TabelaRoteamento tabela = new TabelaRoteamento();
		StringTokenizer stRoteador = new StringTokenizer(string, "#");
		tabela.setIdTabela(stRoteador.nextToken());
		while(stRoteador.hasMoreTokens()){
			String dadosRoteador = stRoteador.nextToken();
			StringTokenizer stDados = new StringTokenizer(dadosRoteador,"@");
			String destino = stDados.nextToken();
			String distancia = stDados.nextToken();
			String salto = stDados.nextToken();
			String conjuntoString = stDados.nextToken();
			StringTokenizer stSaltos = new StringTokenizer(conjuntoString,"-");
			Saltos conjunto = new Saltos();
			while(stSaltos.hasMoreTokens()){
				conjunto.addSalto(stSaltos.nextToken());
			}
			tabela.adicionaNovoCaminho(destino, Integer.parseInt(distancia), salto,conjunto);
		}
		return tabela;
	}
	
	/**
	 * Procedimento adicionaNovoCaminho, adiciona um novo caminho na tabela de roteamento
	 * de um roteador.<br>
	 * @param destino o roteador de destino.<br>
	 * @param distancia o valor da distancia entre os roteadores.<br>
	 * @param salto o salto correspondente entre os roteadores.<br>
	 * @param conjuntoSaltos o conjunto de saltos de um roteador.<br>
	 */
	public void adicionaNovoCaminho(String destino, int distancia, String salto, Saltos conjuntoSaltos){
		tabela.put(destino, new SaltosEDistancia(distancia,salto,conjuntoSaltos));
	}
	
	/**
	 * Procedimento adicionaNovoCaminho, adiciona um novo caminho na tabela 
	 * de roteamento de um roteador.<br>
	 * @param ds um objeto da classe SaltosEDistancia, que contem a distancia, o salto e o conjunto de saltos
	 * de um roteador.<br>
	 * @param destino o roteador de destino.<br>
	 */
	public void adicionaNovoCaminho(SaltosEDistancia ds, String destino){
		tabela.put(destino, ds);
	}
}
