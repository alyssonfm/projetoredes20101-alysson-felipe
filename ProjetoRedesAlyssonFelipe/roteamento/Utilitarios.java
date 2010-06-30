
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
 * Classe Utilitarios. Classe usada para fornecer algumas 
 * funcionalidades a outras classes do programa.<br>
 * @author Alysson Filgueira e Felipe Barbosa.<br>
 * @version 1.0.0.5 29 de junho de 2010.<br>
 */
public  class Utilitarios {
	
	private static final int INFINITO = 999;
	private static final String ENLACES = "arquivosConfiguracao/enlaces.config";
	private static final String ROTEADORES = "arquivosConfiguracao/roteador.config";
	
	/**
	 * Funcao getValorInfinito. Retorna o valor 
	 * infinito (um valor arbitrario para representar infinito). <br>
	 * @return INFINITO maior valor inteiro.<br>
	 */
	public static int getValorInfinito(){
		return INFINITO;
	}
	
	/**
	 * Funcao getPathEnlaces. Retorna o caminho 
	 * para acessar o arquivo de configuracao dos enlaces. <br>
	 * @return ENLACES o caminho para o arquivo de configuracao dos enlaces. <br>
	 */
	public static String getPathEnlaces(){
		return ENLACES;
	}
	
	/**
	 * Funcao getPathRoteadores. Retorna o caminho para 
	 * acessar o arquivo de configuracao dos roteadores. <br>
	 * @return ROTEADORES o caminho para o arquivo de 
	 * configuracao dos roteadores.<br>
	 */
	public static String getPathRoteadores(){
		return ROTEADORES;
	}
	
	/**
	 * Funcao getPortaEIp, retorna a porta e o ip do roteador passado como parametro.<br>
	 * @param idRoteador o numero do roteador que se deseja conhecer a porta e o ip.<br>
	 * @return um hashMap contendo a porta e o ip do roteador passado como parametro.<br>
	 * @throws Exception Pode lancar excecao na leitura do arquivo de configuracao
	 * dos roteadores.<br>
	 */
	public static HashMap<String, String> getPortaEIp(String idRoteador) throws Exception{
		try {
			HashMap<String, String> portaIp = new HashMap<String, String>();
			BufferedReader buffer = new BufferedReader(new FileReader(new File(getPathRoteadores())));
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
			throw new ArquivoNaoEncontradoException ("Arquivo " + getPathRoteadores() + " inexistente!");
		}
	}
	
	/**
	 * Funcao retornarVizinhos, retorna os vizinhos do roteador cujo id 
	 * foi passado como parametro.<br>
	 * @param idRoteador o identificador do roteador que se deseja 
	 * retornar os vizinhos.<br>
	 * @return os vizinhos do roteador passado como parametro.<br>
	 * @throws Exception Pode lancar excecao na leitura do arquivo de 
	 * configuracao dos enlaces da rede.<br>
	 */
	public static Vizinhos retornarVizinhos(String idRoteador) throws Exception{
		try {
			Vizinhos vizinhos = new Vizinhos(idRoteador);
			BufferedReader buffer = new BufferedReader(new FileReader(new File(getPathEnlaces())));
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
			throw new ArquivoNaoEncontradoException("Arquivo " + getPathEnlaces() + " inexistente!");
		}
	}
	
	/**
	 * Funcao incializarTabela, inicializa a tabela de roteamento do
	 * roteador que tem o id passado como parametro.<br>
	 * @param idRoteador o id do roteador que se deseja obter a tabela de roteamento.<br>
	 * @return a tabela de roteamento do roteador.<br>
	 * @throws Exception Pode lancar excecao na leitura do arquivo de
	 * configuracao dos roteadores da rede.<br> 
	 */
	public static TabelaRoteamento inicializarTabela(String idRoteador) throws Exception{
		TabelaRoteamento tabela = new TabelaRoteamento(idRoteador);
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(new File(getPathRoteadores())));
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
					tabela.adicionaNovoCaminho(roteador,getValorInfinito(),"*",conjunto);
				}
			} while (linha != null);
			return tabela;
		} catch ( IOException ioe ){
			throw new ArquivoNaoEncontradoException("Arquivo " + getPathRoteadores() + " inexistente!");
		}
	}
	
	/**
	 * Funcao getDistanciaEntreRoteadores, retorna a distancia 
	 * entre dois roteadores.<br>
	 * @param roteadorA um roteador da rede.<br>
	 * @param roteadorB outro roteador da rede.<br>
	 * @return a distancia entre os roteadores passados como parametro.<br>
	 * @throws Exception Pode lancar excecao na leitura do arquivo de
	 * configuracao dos enlaces da rede.<br> 
	 */
	public static int getDistanciaEntreRoteadores(String roteadorA ,String roteadorB) throws Exception{
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(new File(getPathEnlaces())));
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
			throw new ArquivoNaoEncontradoException("Arquivo " + getPathEnlaces() + " inexistente!");
		}
		return 0;
	}

	/**
	 * Funcao geraMsgRoteadorMorto, cria a mensagem que identifica que um roteador da rede 
	 * esta morto, nao esta mais enviando sua tabela de roteamento.<br>
	 * @param roteador o roteador que esta morto.<br>
	 * @param idRoteadorMorto o id do roteador morto.<br>
	 * @return uma string contendo do roteadores que devem ser avisados da morte 
	 * do roteador passado como parametro, para que possam atualizar suas
	 * tabelas de roteamento.<br>
	 */
	@SuppressWarnings("unchecked")
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
	 * Funcao pegaRoteadorMortoPelaMsg, retorna o roteador que esta morto.<br>
	 * @param msg a mensagem criada para notificar os roteadores da morte de um
	 * roteador.<br>
	 * @return a string que representa a identificacao do roteador morto.<br>
	 */
	public static String pegaRoteadorMortoPelaMsg(String msg){
		StringTokenizer st = new StringTokenizer(msg,"!");
		return st.nextToken();
	}
	
	/**
	 * Funcao geraMsgRoteadorMorto, cria a mensagem que identifica que um roteador da rede 
	 * esta morto, nao esta mais enviando sua tabela de roteamento.<br>
	 * @param list Uma lista de strings contendo os roteadores da rede.<br>
	 * @param idRoteadorMorto a identificacao do roteador morto.<br>
	 * @return a string para notificar os demais roteadores da rede.<br>
	 */
	@SuppressWarnings("unchecked")
	public static String geraMsgRoteadorMorto(LinkedList<String> list, String idRoteadorMorto) {
		String resultado = "!" + idRoteadorMorto + "!";
		Iterator it = list.iterator();
		while(it.hasNext()){
			String idRoreadorVizinho = (String) it.next();
			resultado += idRoreadorVizinho + "#";
		}
		return resultado;
	}
	
	/**
	 * Funcao pegaDonoPelaMsgTabela
	 * @param string a mensagem da tabela de roteamento de um roteador.<br>
	 * @return o dono da tabela passada como parametro.<br>
	 */
	public static String pegaDonoPelaMsgTabela(String string){
		StringTokenizer stRoteador = new StringTokenizer(string, "#");
		return stRoteador.nextToken();
	}
	
}
