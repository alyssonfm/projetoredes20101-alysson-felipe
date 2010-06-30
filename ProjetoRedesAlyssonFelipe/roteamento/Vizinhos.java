
package roteamento;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Classe Vizinhos. Eh a classe que implementa as 
 * funcionalidades relativas aos vizinhos de cada roteador. <br>
 * @author Alysson Filgueira e Felipe Barbosa.<br>
 * @version 1.0.0.5 29 de junho de 2010.<br>
 */
public class Vizinhos {

	private LinkedList<String> vizinhos;
	private String numeroRoteador; 
	

	/**
	 * Construtor da classe, recebe como parametro o numero do roteador que se deseja criar os 
	 * vizinhos.<br>
	 * @param numeroRoteador o numero de identificacao do roteador.<br>
	 */
	public Vizinhos(String numeroRoteador){
		vizinhos = new LinkedList<String>();
		this.numeroRoteador = numeroRoteador;
	}

	/**
	 * Procedimento adicionarVizinho, adiciona um vizinho na lista de vizinhos 
	 * do roteador que tem o id passado como parametro.<br>
	 * @param idRoteador o identificador do roteador que se deseja adicionar como
	 * vizinho na lista de vizinhos de um roteador.<br>
	 */
	public void adicionarVizinho(String idRoteador){
		vizinhos.add(idRoteador);
	}
	
	/**
	 * Funcao getVizinhos, retorna a lista de vizinhos de um roteador.<br>
	 * @return a lista de vizinhos.<br>
	 */
	public LinkedList<String> getVizinhos() {
		return vizinhos;
	}


	/**
	 * Procedimento setIdRoteador, permite atualizar o roteador dono da lista de 
	 * vizinhos.<br>
	 * @param idRoteador o id do roteador que sera atualizado.<br>
	 */
	public void setIdRoteador(String idRoteador) {
		this.numeroRoteador = idRoteador;
	}

	/**
	 * Funcao toString, retorna a string correspondente 
	 * a lista de vizinhos de um roteador.<br>
	 */
	@SuppressWarnings("unchecked")
	public String toString(){
		String resultado = "Os vizinhos de " + getIdRoteador() + " sao : "; 
		Iterator it = vizinhos.iterator();
		while (it.hasNext()){
			resultado += it.next() + ", ";
		}
		if (resultado.endsWith(", ")){
			resultado = (String) resultado.subSequence(0, resultado.length()-2) + ".";
		}
		return resultado;
	}
	
	/**
	 * Procedimento setVizinhos, permite atualizar a lista de vizinhos de um roteador.<br>
	 * @param vizinhos a lista de vizinhos a ser atualizada.<br>
	 */
	public void setVizinhos(LinkedList<String> vizinhos) {
		this.vizinhos = vizinhos;
	}

	/**
	 * Funcao getIdRoteador, retorna o numero de identificacao de um roteador.<br>
	 * @return o identificador de um roteador.<br>
	 */
	public String getIdRoteador() {
		return numeroRoteador;
	}
	
	/**
	 * Funcao iterator, retorna um iterador sobre a lista de vizinhos de um roteador.<br>
	 * @return o iterador da lista de vizinhos.<br>
	 */
	@SuppressWarnings("unchecked")
	public Iterator iterator(){
		return vizinhos.iterator();
	}
	
}
