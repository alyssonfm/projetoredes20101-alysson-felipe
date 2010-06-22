package excecoes;

import java.io.IOException;

/**
 * 
 * @author Alysson Filgueira e Felipe 
 * @version 1.0.0.5 18 de junho de 2010.
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
