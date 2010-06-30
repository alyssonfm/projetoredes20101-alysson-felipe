
package roteamento;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Classe Saltos. Classe que implementa os "saltos" dos roteadores. <br>
 * @author Alysson Filgueira e Felipe Barbosa. <br>
 * @version 1.0.0.5 29 de junho de 2010.<br>
 */
public class Saltos {
	private LinkedList<String> conjuntoDeSaltos;
	
	/**
	 * Construtor da Classe, cria uma lista de strings que representarao os saltos.<br>
	 */
	public Saltos(){
		conjuntoDeSaltos = new LinkedList<String>();
	}
	
	/**
	 * Procedimento toString, usado para criar a representacao textual dos saltos.<br>
	 */
	@SuppressWarnings("unchecked")
	public String toString(){
		Iterator iterador = iterator();
		String resultado = "";
		while(iterador.hasNext()){
			resultado += iterador.next() + " - ";
		}
		return resultado;
	}

	/**
	 * Procedimento setConjuntoSaltos, recebe como parametro 
	 * um linked list de strings que representam o conjunto de saltos
	 * a serem atualizados.<br>
	 * @param conjuntoDeSaltos o conjunto de saltos a serem atualizados.<br>
	 */
	public void setConjuntoDeSaltos(LinkedList<String> conjuntoDeSaltos) {
		this.conjuntoDeSaltos = conjuntoDeSaltos;
	}
	
	/**
	 * Funcao contem, verifica se o conjunto de saltos 
	 * contem um salto em particular, ou seja, se um roteador 
	 * pode realizar determinado salto.<br>
	 * @param salto o salto a ser verificado se faz parte dos saltos de um roteador.<br>
	 * @return um booleano que indica se um roteador possui ou nao o salto passado como 
	 * parametro.<br>
	 */
	public boolean contem(String salto){
		return conjuntoDeSaltos.contains(salto);
	}
	
	/**
	 * Procedimento addSalto, adiciona um salto ao 
	 * conjunto de saltos do roteador.<br>
	 * @param salto o salto a ser adicionado ao conjunto de um roteador.<br>
	 */
	public void addSalto(String salto){
		conjuntoDeSaltos.add(salto);
	}
		
	/**
	 * Funcao getConjuntoDeSaltos, retorna o 
	 * conjunto de saltos de um roteador.<br>
	 * @return o conjunto de saltos de um roteador.<br>
	 */
	public LinkedList<String> getConjuntoDeSaltos() {
		return conjuntoDeSaltos;
	}

	/**
	 * Funcao iterator, retorna um iterador sobre o 
	 * conjunto de saltos de um roteador.<br>
	 * @return o iterador sobre o conjunto de saltos.<br>
	 */
	@SuppressWarnings("unchecked")
	public Iterator iterator(){
		return conjuntoDeSaltos.iterator();
	}
	

}
