package principal;


import java.util.Scanner;

import roteamento.Roteador;
import roteamento.Utilitarios;
import conexoes.No;

/**
 * Classe Main. Classe principal do programa, na qual inicializamos o roteador que iremos usar na simulacao da rede.<br>
 * @author Alysson Filgueira e Felipe Barbosa. <br>
 * @version 1.0.0.5 18 de junho de 2010.
 */
public class Main {
	
	private static Scanner scanner = new Scanner(System.in);
	
	public static void escolherRoteador(){
		String numeroRoteador;
		try {
		do{				
			System.out.println("Informe o id do roteador que voceh deseja criar: ");
			numeroRoteador = scanner.next();
			// Verificacao para garantir que nao passaremos um roteador invalido para o sistema.
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
	
	public static void setarTO(){
		int timeout;
		System.out.println("Informe o novo valor para TimeOut (em milisegundos): ");
		timeout = scanner.nextInt();
		No.setTimeOut(timeout);
		escolherRoteador();
	}

	public static void main(String[] args) {
		
		int escolha;		
		System.out.println("O que voce deseja fazer? \n1- Setar timeOut para os nos\n2- Escolher roteador e usar configuracoes padrao");
		escolha = scanner.nextInt();
		switch (escolha) {
		case 1:
			setarTO();
			break;
		case 2:
			escolherRoteador();
			break;
		default:
			break;
		}
	}
}
