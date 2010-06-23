
package roteamento;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Classe Vizinhos. E a classe que implementa as funcionalidades relativas aos vizinhos de cada roteador. <br>
 * @author Alysson Filgueira e Felipe Barbosa.<br>
 * @version 1.0.0.5 22 de junho de 2010.<br>
 */
public class Vizinhos {

	private LinkedList<String> vizinhos;
	private String numeroRoteador; 
	

	/**
	 * 
	 * @param numeroRoteador
	 */
	public Vizinhos(String numeroRoteador){
		vizinhos = new LinkedList<String>();
		this.numeroRoteador = numeroRoteador;
	}

	/**
	 * 
	 * @param idRoteador
	 */
	public void adicionarVizinho(String idRoteador){
		vizinhos.add(idRoteador);
	}
	
	/**
	 * 
	 * @return
	 */
	public LinkedList<String> getVizinhos() {
		return vizinhos;
	}


	/**
	 * 
	 * @param idRoteador
	 */
	public void setIdRoteador(String idRoteador) {
		this.numeroRoteador = idRoteador;
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String toString(){
		String resultado = "Vizinhos de " + getIdRoteador() + ": "; 
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
	 * 
	 * @param vizinhos
	 */
	public void setVizinhos(LinkedList<String> vizinhos) {
		this.vizinhos = vizinhos;
	}

	/**
	 * 
	 * @return
	 */
	public String getIdRoteador() {
		return numeroRoteador;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Iterator iterator(){
		return vizinhos.iterator();
	}
	
}
