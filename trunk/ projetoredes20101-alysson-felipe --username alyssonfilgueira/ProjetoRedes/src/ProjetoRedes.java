import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe ProjetoRedes, classe na qual sera implementada a interacao do sistema.<br>
 * @author Alysson Filgueira <br> Filipe Barbosa
 *
 */

public class ProjetoRedes {
	
	public ProjetoRedes() {

	}
	
	public static String[] openFile(String path){
		String content = "";
		File file = new File(path);
		try{
			FileInputStream fis = new FileInputStream(file);
			int ln;
			while ((ln = fis.read()) != -1) {
				content += (char) ln;				
			}
			fis.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		String[] aux = content.split("\n");
		return aux;
	}
	
	public static ArrayList<Integer> getRoteadores(String[] aux){		
		ArrayList<Integer> roteadores = new ArrayList<Integer>();
		for(int i = 0; i < aux.length; i++){
			roteadores.add(Integer.parseInt(aux[i].substring(0, 1)));
		}
		return roteadores;
	}
	
	public static ArrayList<Integer> getPortas(String[] aux){		
		ArrayList<Integer> portas = new ArrayList<Integer>();
		for(int i = 0; i < aux.length; i++){
			portas.add(Integer.parseInt(aux[i].substring(2,aux[i].indexOf(" ", 2))));
		}
		return portas;
	}
	
	public static ArrayList<String> getIps(String[] aux){		
		ArrayList<String> ips = new ArrayList<String>();
		for(int i = 0; i < aux.length; i++){
			ips.add((aux[i].substring(8, aux[i].length())));
		}
		return ips;
	}
	
	public static void main(String[] args) {
		int idRoteador;
		Scanner entrance = new Scanner(System.in);
		System.out.print("Insira o roteador: ");
		idRoteador = entrance.nextInt();
		String[] aux = openFile("roteador.config");
		if(getRoteadores(aux).contains(idRoteador)){
			System.out.print(getIps(aux).get(getRoteadores(aux).indexOf(idRoteador)) 
					+ getPortas(aux).get(getRoteadores(aux).indexOf(idRoteador)));
		}else{
			System.err.println("O roteador nao faz parte do arquivo roteador.config");
		}
	}

}
