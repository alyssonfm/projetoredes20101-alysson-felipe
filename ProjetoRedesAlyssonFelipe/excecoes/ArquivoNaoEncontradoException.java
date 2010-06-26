package excecoes;

import java.io.IOException;

/**
 * Classe ArquivoNaoEncontradoException. 
 * Subclasse de IOException, usada quando o programa lanca
 *  excecoes para arquivos nao encontrados.<br>
 * @author Alysson Filgueira e Felipe Barbosa. <br>
 * @version 1.0.0.5 25 de junho de 2010.<br>
 */
@SuppressWarnings("serial")
public class ArquivoNaoEncontradoException extends IOException {
	
	/**
	 * Construtor da Classe, o qual recebe como parametro uma string para indicar ao 
	 * usuario que erro ocorreu.<br>
	 * @param mensagem A mensagem do erro ocorrido,melhora o entendimento 
	 * do erro ocorrido.<br>
	 */
	public ArquivoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

}
