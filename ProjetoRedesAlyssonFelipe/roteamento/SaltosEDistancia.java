
package roteamento;

/**
 * Classe SaltosEDistancia. Classe na qual estao presentes os 
 * procedimentos e funcoes ligados aos saltos e suas respectivas distancias entre
 * os roteadores da rede.<br>
 * @author Alysson Filgueira e Felipe Barbosa.<br>
 * @version 1.0.0.5 29 de junho de 2010.<br>
 */
public class SaltosEDistancia {
	private Saltos conjuntoDeSaltos;
	private String salto;
	private int distancia;

	/**
	 * Construtor da classe, que recebe como parametro a distancia entre os roteadores,
	 * o salto que deve ser realizado e o conjunto de saltos de um roteador.<br>
	 * @param distancia a distancia entre os roteadores.<br>
	 * @param salto o salto que deve ser realizado.<br>
	 * @param conjunto o conjunto de saltos de um roteador.<br>
	 */
	public SaltosEDistancia(int distancia, String salto, Saltos conjunto){
		this.distancia = distancia;
		this.salto = salto;
		this.conjuntoDeSaltos = conjunto;
	}


	/**
	 * Funcao getSalto, retorna o salto que sera realizado por um roteador.<br>
	 * @return o salto a ser realizado.<br>
	 */
	public String getSalto() {
		return salto;
	}
	
	/**
	 * Procedimento setSalto, permite que o salto a ser realizado entre os roteadores 
	 * seja atualizado.<br>
	 * @param salto o salto a ser atualizado.<br>
	 */
	public void setSalto(String salto) {
		this.salto = salto;
	}
	
	/**
	 * Funcao getDistancia, retorna a distancia entre dois roteadores.<br>
	 * @return a distancia entre dois roteadores.<br>
	 */
	public int getDistancia() {
		return distancia;
	}

	/**
	 * Procedimento setDistancia, permite atualizar a distancia entre os roteadores.<br>
	 * @param distancia a distancia a ser atualizada.<br>
	 */
	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}
	
	/**
	 * Funcao getConjuntoDeSaltos, retorna o conjunto de saltos de um roteador.<br>
	 * @return o conjunto de saltos.<br>
	 */
	public Saltos getConjuntoDeSaltos() {
		return conjuntoDeSaltos;
	}
	
	/**
	 * Procedimento setConjuntoDeSaltos, permite atualizar o conjunto
	 * de saltos de um roteador.<br>
	 * @param conjuntoDeSaltos o conjunto de saltos a ser atualizado.<br>
	 */
	public void setConjuntoDeSaltos(Saltos conjuntoDeSaltos) {
		this.conjuntoDeSaltos = conjuntoDeSaltos;
	}
}
