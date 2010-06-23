package excecoes;

import java.io.IOException;

/**
 * Classe ArquivoNaoEncontradoException.<br>
 * @author Alysson Filgueira e Felipe Barbosa. <br>
 * @version 1.0.0.5 18 de junho de 2010.<br>
 */
public class ArquivoNaoEncontradoException extends IOException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArquivoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

}
