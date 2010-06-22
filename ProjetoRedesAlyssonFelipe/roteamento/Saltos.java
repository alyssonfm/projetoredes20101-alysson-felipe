
package roteamento;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author alyssonfm
 *
 */
public class Saltos {
	private LinkedList<String> conjuntoDeSaltos;
	
	/**
	 * 
	 */
	public String toString(){
		Iterator iterador = iterator();
		String resultado = "";
		while(iterador.hasNext()){
			resultado += iterador.next() + " - ";
		}
		return resultado;
	}
	
	/**
	 * 
	 */
	public Saltos(){
		conjuntoDeSaltos = new LinkedList<String>();
	}

	/**
	 * 
	 * @param conjuntoDeSaltos
	 */
	public void setConjuntoDeSaltos(LinkedList<String> conjuntoDeSaltos) {
		this.conjuntoDeSaltos = conjuntoDeSaltos;
	}
	
	/**
	 * 
	 * @param salto
	 * @return
	 */
	public boolean contem(String salto){
		return conjuntoDeSaltos.contains(salto);
	}
	
	/**
	 * 
	 * @param salto
	 */
	public void addSalto(String salto){
		conjuntoDeSaltos.add(salto);
	}
		
	/**
	 * 
	 * @return
	 */
	public LinkedList<String> getConjuntoDeSaltos() {
		return conjuntoDeSaltos;
	}

	/**
	 * 
	 * @return
	 */
	public Iterator iterator(){
		return conjuntoDeSaltos.iterator();
	}
	

}
