
package roteamento;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import excecoes.ArquivoNaoEncontradoException;

/**
 * 
 * @author alyssonfm
 *
 */
public  class Utilitarios {
	
	public static final int INFINITO = 50;
	public static final String ENLACES = "arquivosConfiguracao/enlaces.config";
	public static final String ROTEADORES = "arquivosConfiguracao/roteador.config";
	
	/**
	 * 
	 * @param idRoteador
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, String> getPortaEIp(String idRoteador) throws Exception{
		try {
			HashMap<String, String> portaIp = new HashMap<String, String>();
			BufferedReader buffer = new BufferedReader(new FileReader(new File(ROTEADORES)));
			String linha = null;
			do {
				linha = buffer.readLine();
				if (linha == null){
					break; 
				}
				StringTokenizer st = new StringTokenizer(linha," ");
				String roteador = st.nextToken(); 
				if(roteador.equals(idRoteador)){
					portaIp.put(st.nextToken(), st.nextToken());
					return portaIp;
				}
			} while (linha != null);
			return null;
		} catch ( ArquivoNaoEncontradoException ioe ){
			throw new ArquivoNaoEncontradoException ("Arquivo " + ROTEADORES + " inexistente!");
		}
	}
	
	/**
	 * 
	 * @param idRoteador
	 * @return
	 * @throws Exception
	 */
	public static Vizinhos retornarVizinhos(String idRoteador) throws Exception{
		try {
			Vizinhos vizinhos = new Vizinhos(idRoteador);
			BufferedReader buffer = new BufferedReader(new FileReader(new File(ENLACES)));
			String linha = null;
			do {
				linha = buffer.readLine();
				if (linha == null){
					return vizinhos; 
				}
				StringTokenizer st = new StringTokenizer(linha," ");
				String roteador1 = st.nextToken();
				String roteador2 = st.nextToken();
				if(roteador1.equals(idRoteador)){
					vizinhos.adicionarVizinho(roteador2);
				} else if (roteador2.equals(idRoteador)){
					vizinhos.adicionarVizinho(roteador1);
				}
			} while (linha != null);
			return vizinhos;
		} catch ( IOException ioe ){
			throw new Exception ("Arquivo " + ENLACES + " inexistente!");
		}
	}
	
	/**
	 * 
	 * @param idRoteador
	 * @return
	 * @throws Exception
	 */
	public static TabelaRoteamento inicializarTabela(String idRoteador) throws Exception{
		TabelaRoteamento tabela = new TabelaRoteamento(idRoteador);
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(new File(ROTEADORES)));
			String linha = null;
			do {
				linha = buffer.readLine();
				if (linha == null){
					return tabela; 
				}
				StringTokenizer st = new StringTokenizer(linha," ");
				String roteador = st.nextToken(); 
				if(roteador.equals(idRoteador)){
					Saltos conjunto = new Saltos();
					conjunto.addSalto(idRoteador);
					tabela.adicionaNovoCaminho(idRoteador, 0 , idRoteador,conjunto);
				} else {
					Saltos conjunto = new Saltos();
					conjunto.addSalto("*");
					tabela.adicionaNovoCaminho(roteador,INFINITO,"*",conjunto);
				}
			} while (linha != null);
			return tabela;
		} catch ( IOException ioe ){
			throw new Exception ("Arquivo " + ROTEADORES + " inexistente!");
		}
	}
	
	/**
	 * 
	 * @param roteadorA
	 * @param roteadorB
	 * @return
	 * @throws Exception
	 */
	public static int getDistanciaEntreRoteadores(String roteadorA ,String roteadorB) throws Exception{
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(new File(ENLACES)));
			String linha = null;
			do {
				linha = buffer.readLine();
				if (linha == null){
					return 0; 
				}
				StringTokenizer st = new StringTokenizer(linha," ");
				String roteador1 = st.nextToken();
				String roteador2 = st.nextToken();
				if ((roteador1.equals(roteadorA) && roteador2.equals(roteadorB)) || (roteador1.equals(roteadorB) && roteador2.equals(roteadorA))) {
					return Integer.parseInt(st.nextToken());
				} 
			} while (linha != null);
		} catch ( IOException ioe ){
			throw new Exception ("Arquivo " + ENLACES + " inexistente!");
		}
		return 0;
	}

	/**
	 * 
	 * @param roteador
	 * @param idRoteadorMorto
	 * @return
	 */
	public static String geraMsgRoteadorMorto(Roteador roteador, String idRoteadorMorto) {
		String resultado = "!" + idRoteadorMorto + "!" + roteador.getNumeroRoteador() + "#";
		Iterator it = roteador.getVizinhos().iterator();
		while(it.hasNext()){
			String idRoreadorVizinho = (String) it.next();
			resultado += idRoreadorVizinho + "#";
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public static String pegaRoteadorMortoPelaMsg(String msg){
		StringTokenizer st = new StringTokenizer(msg,"!");
		return st.nextToken();
	}
	
	/**
	 * 
	 * @param list
	 * @param idRoteadorMorto
	 * @return
	 */
	public static String geraMsgRoteadorMorto(LinkedList list, String idRoteadorMorto) {
		String resultado = "!" + idRoteadorMorto + "!";
		Iterator it = list.iterator();
		while(it.hasNext()){
			String idRoreadorVizinho = (String) it.next();
			resultado += idRoreadorVizinho + "#";
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String pegaDonoPelaMsgTabela(String string){
		StringTokenizer stRoteador = new StringTokenizer(string, "#");
		return stRoteador.nextToken();
	}
	

}
