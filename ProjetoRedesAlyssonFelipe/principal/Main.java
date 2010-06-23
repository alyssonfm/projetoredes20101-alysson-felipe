package principal;


import java.util.Scanner;

import roteamento.Roteador;
import roteamento.Utilitarios;

/**
 * Classe Main. Classe principal do programa, na qual todo o processo eh inicializado.<br>
 * @author Alysson Filgueira e Felipe Barbosa. <br>
 * @version 1.0.0.5 18 de junho de 2010.
 */
public class Main {

	public static void main(String[] args) {
		String numeroRoteador;
		Scanner scanner = new Scanner(System.in);
		try {
			do{
				System.out.println("Informe o id do roteador que voceh deseja criar: ");
				numeroRoteador = scanner.next();
				if (Utilitarios.getPortaEIp(numeroRoteador) == null) {
					System.out.println(">>>Roteador nao encontrado<<<");
				}
			}
			while(Utilitarios.getPortaEIp(numeroRoteador) == null);
			@SuppressWarnings("unused")
			Roteador roteador = new Roteador(numeroRoteador) ;
			
		} catch (Exception ex ) {
			System.err.println("Roteador Invalido!");
			System.exit(1);
		}


	}
}
