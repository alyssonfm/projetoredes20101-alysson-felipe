
package roteamento;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 
 * @author Alysson Filgueira e Felipe Barbosa.<br>
 * @version 1.0.0.5 21 de junho de 2010.<br>
 */
public class TabelaRoteamento {

	private String numeroTabela;
	
	private HashMap<String, SaltosEDistancia> tabela;
	
	/**
	 * 
	 */
	public TabelaRoteamento(){
		tabela = new HashMap<String, SaltosEDistancia>();
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String toString(){
		Set<String> set = tabela.keySet();
		Iterator it = set.iterator();
		String resultado = "Tabela [" + getIdTabela() + "] \n";
		while(it.hasNext()){
			String next = (String) it.next();
			SaltosEDistancia distAndSalto = tabela.get(next);
			resultado += "[ " + next + " ] = " + distAndSalto.getDistancia() + " UD - Proximo Salto: " + distAndSalto.getSalto() + "\n"; 
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param idTabela
	 */
	public TabelaRoteamento(String idTabela){
		tabela = new HashMap<String, SaltosEDistancia>();
		this.numeroTabela = idTabela;
	}
	

	/**
	 * 
	 * @param roteador
	 * @return
	 */
	public int getDistancia(String roteador){
		return tabela.get(roteador).getDistancia();
	}
	

	/**
	 * 
	 * @return
	 */
	public String getIdTabela() {
		return numeroTabela;
	}


	/**
	 * 
	 * @param idTabela
	 */
	public void setIdTabela(String idTabela) {
		this.numeroTabela = idTabela;
	}


	/**
	 * 
	 * @return
	 */
	public HashMap<String, SaltosEDistancia> getTabela() {
		return tabela;
	}


	/**
	 * 
	 * @param tabela
	 */
	public void setTabela(HashMap<String, SaltosEDistancia> tabela) {
		this.tabela = tabela;
	}
	
	/**
	 * 
	 * @param tabela
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String transformaTabelaParaString(TabelaRoteamento tabela){
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
	 * 
	 * @param origem
	 * @param destino
	 */
	@SuppressWarnings("unchecked")
	public static void comparaTabelas(TabelaRoteamento origem, TabelaRoteamento destino){
		Set<String> setOrigem = origem.getTabela().keySet();
		Iterator it = setOrigem.iterator();
		while (it.hasNext()){
			String nextOrigem = (String) it.next();
			SaltosEDistancia dsOrigem = origem.getTabela().get(nextOrigem);
			if (!destino.getTabela().containsKey(nextOrigem)){
				Saltos conjunto = new Saltos();
				conjunto.addSalto(nextOrigem);
				SaltosEDistancia novoDS = new SaltosEDistancia(destino.getDistancia(origem.getIdTabela()) + dsOrigem.getDistancia(), nextOrigem,conjunto);
				destino.adicionaNovoCaminho(novoDS, nextOrigem);
			} else {
				if (destino.getDistancia(nextOrigem) >  destino.getDistancia(origem.getIdTabela()) + dsOrigem.getDistancia()){
					Saltos conjunto = dsOrigem.getConjuntoDeSaltos();
					conjunto.addSalto(origem.getIdTabela());
					SaltosEDistancia novoDS = new SaltosEDistancia(destino.getDistancia(origem.getIdTabela()) + dsOrigem.getDistancia(), origem.getIdTabela(),conjunto);
					destino.adicionaNovoCaminho(novoDS, nextOrigem);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param origem
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
	 * 
	 * @param string
	 * @return
	 */
	public static TabelaRoteamento transformaStringParaTabela(String string){
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
	 * 
	 * @param destino
	 * @param distancia
	 * @param salto
	 * @param conjunto
	 */
	public void adicionaNovoCaminho(String destino, int distancia, String salto, Saltos conjunto){
		tabela.put(destino, new SaltosEDistancia(distancia,salto,conjunto));
	}
	
	/**
	 * 
	 * @param ds
	 * @param destino
	 */
	public void adicionaNovoCaminho(SaltosEDistancia ds, String destino){
		tabela.put(destino, ds);
	}
}
