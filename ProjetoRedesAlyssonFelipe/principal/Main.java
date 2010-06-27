package principal;


import java.util.Scanner;

import roteamento.Roteador;
import roteamento.Utilitarios;
import conexoes.No;

/**
 * Classe Main. Classe principal do programa, 
 * na qual inicializamos o roteador que 
 * iremos usar na simulacao da rede. Alem disso eh possivel 
 * alterar configuracoes do programa.<br>
 * @author Alysson Filgueira e Felipe Barbosa. <br>
 * @version 1.0.0.5 25 de junho de 2010.
 */
public class Main {
	
	private static Scanner scanner = new Scanner(System.in);
	
	/**
	 * Procedimento escolherRoteador. Procedimento usado para 
	 * escolha do roteador que se deseja criar na rede.<br>
	 */
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
	
	/**
	 * Procedimento atualizaConfiguracoes. Procedimento no qual eh possivel alterar as 
	 * configuracoes padrao do sistema, podendo alterar o time out
	 * o tempo de delay e do periodo de envio das mensagens dos roteadores.<br>
	 */
	public static void atualizarConfiguracoes(){
		setarTO();
		setarTempoTrocaMsgs();
		escolherRoteador();
	}
	
	/**
	 * Procedimento setarTempoTrocaMsgs. Procedimento no qual
	 * podemos alterar o tempo para a troca das mensagens entre
	 * os roteadores.<br>
	 */
	public static void setarTempoTrocaMsgs(){
		int delay, periodo;
		System.out.println("Informe o novo valor para o delay (em milisegundos): ");
		delay = scanner.nextInt();
		System.out.println("Informe o novo valor para o periodo (em milisegundos): ");
		periodo = scanner.nextInt();
		Roteador.setDelay(delay);
		Roteador.setPeriodo(periodo);
	}
	
	/**
	 * Procedimento setarTO. Procedimento que permite 
	 * atualizar o tempo para time-out dos nos da rede.<br>
	 */
	public static void setarTO(){
		int timeout;
		System.out.println("Informe o novo valor para TimeOut (em milisegundos): ");
		timeout = scanner.nextInt();
		No.setTimeOut(timeout);
	}

	/**
	 * Procedimento main. Procedimento principal do programa, 
	 * no qual ocorre a interacao com o usuario, a qual ocorre
	 * por meio de um menu de opcoes para utilizacao do programa.<br>
	 * @param args
	 */
	public static void main(String[] args) {		
		int escolha;		
		System.out.println("\t\tProjeto de Redes de Computadores 2010-1");
		System.out.println("\t\tAlysson Filgueira e Felipe Barbosa");
		System.out.println("\t\t\t=====Menu===== \n1- Alterar configuracoes\n2- Escolher roteador e usar configuracoes padrao\n3- Para encerrar o programa");
		escolha = scanner.nextInt();
		switch (escolha) {
		case 1:
			atualizarConfiguracoes();
			break;
		case 2:
			escolherRoteador();
			break;
		case 3:
			System.out.println("Programa sendo encerrado...");
			break;
		default:
			break;
		}
	}
}
