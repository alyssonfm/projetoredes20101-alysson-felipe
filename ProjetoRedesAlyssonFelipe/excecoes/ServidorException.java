package excecoes;

/**
 * Classe ServidorException, subclasse de Exception 
 * criada para indicar que ocorreu uma excecao durante a 
 * execucao do Servidor da rede (Servidor UDP).<br>
 * @author Alysson Filgueira e Felipe Barbosa.<br>
 * @version	1.0.0.5 29 de junho de 2010.<br>
 */
@SuppressWarnings("serial")
public class ServidorException extends Exception{
	
	/**
	 * Construtor da Classe, que recebe a mensagem 
	 * correspondente ao erro como parametro, para indicar
	 * ao usuario mais especificamente qual erro ocorreu durante a execucao do
	 * programa.<br>
	 * @param message A menssagem contendo o erro ocorrido.<br>
	 */
	public ServidorException(String message) {
		super(message);
	}
}
