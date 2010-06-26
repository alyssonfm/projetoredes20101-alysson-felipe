package excecoes;

/**
 * Classe ClientException, subclasse de Exception 
 * criada para indicar que ocorreu uma excecao durante a 
 * execucao de um Clinete UDP (um No da rede).<br> 
 * @author Alysson Filgueira e Felipe Barbosa.<br>
 * @version 1.0.0.5 25 de junho de 2010.<br>
 */
@SuppressWarnings("serial")
public class ClientException extends Exception{
	
	/**
	 * Construtor da Classe, que recebe como parametro uma 
	 * string que representa uma mensagem para o usuario indicando
	 * qual o erro que ocorreu durante a execucao do No.<br>
	 * @param message A menssagem contendo o erro ocorrido.<br>
	 */
	public ClientException(String message) {
		super(message);
	}

}
