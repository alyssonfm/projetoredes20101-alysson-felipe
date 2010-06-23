
package roteamento;

/**
 * Classe SaltosEDistancia.
 * @author Alysson Filgueira e Felipe Barbosa.<br>
 * @version 1.0.0.5 22 de junho de 2010.<br>
 */
public class SaltosEDistancia {
	private Saltos conjuntoDeSaltos;
	private String salto;
	private int distancia;

	/**
	 * 
	 * @param distancia
	 * @param salto
	 * @param conjunto
	 */
	public SaltosEDistancia(int distancia, String salto, Saltos conjunto){
		this.distancia = distancia;
		this.salto = salto;
		this.conjuntoDeSaltos = conjunto;
	}


	/**
	 * 
	 * @return
	 */
	public String getSalto() {
		return salto;
	}


	/**
	 * 
	 * @param distancia
	 */
	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}
	
	/**
	 * 
	 * @param salto
	 */
	public void setSalto(String salto) {
		this.salto = salto;
	}
	
	/**
	 * 
	 * @param conjuntoDeSaltos
	 */
	public void setConjuntoDeSaltos(Saltos conjuntoDeSaltos) {
		this.conjuntoDeSaltos = conjuntoDeSaltos;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getDistancia() {
		return distancia;
	}

	/**
	 * 
	 * @return
	 */
	public Saltos getConjuntoDeSaltos() {
		return conjuntoDeSaltos;
	}
}
