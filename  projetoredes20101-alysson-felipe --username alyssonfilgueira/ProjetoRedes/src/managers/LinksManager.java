package managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

import router.Link;



public class LinksManager {
	private static final String FILE_TO_READ = "./enlaces.config";
	private LinkedList<Link> linksList = new LinkedList<Link>();
	
	public void readFile() {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(FILE_TO_READ));
			String line = buffer.readLine();
			StringTokenizer tokens;
			while (line != null) {
				if (!line.startsWith("#")) {
					tokens = new StringTokenizer(line);
					String routerA = tokens.nextToken(" ");
					String routerB = tokens.nextToken(" ");
					String cost = tokens.nextToken(" ");
					linksList.add(new Link(routerA, routerB, cost));
				}
				line = buffer.readLine();
			}
		} catch (IOException ioe) {
			System.out.println("Erro na leitura do arquivo enlaces.config");
		}
	}
	
	public HashMap<String, Integer> getNeighbors(String routerId) {
		HashMap<String, Integer> neighbors = new HashMap<String, Integer>();
		for (int i = 0; i < linksList.size(); i++) {
			if (routerId.equalsIgnoreCase(linksList.get(i).getRouterA())) {
				neighbors.put(linksList.get(i).getRouterB(), new Integer(linksList.get(i).getCost()));
			} else if (routerId.equalsIgnoreCase(linksList.get(i).getRouterB())) {
				neighbors.put(linksList.get(i).getRouterA(), new Integer(linksList.get(i).getCost()));
			}
		}
		return neighbors;
	}
	
	
	public String toString() {
		String result = "";
		for (int i = 0; i < linksList.size(); i++) {
			result += linksList.get(i).toString()+"\n";
		}
		return result;
	}

	
    /**
     * Retorna o custo de um link
     * @param routerIdFrom a ID do roteador de onde a mensagem sairá
     * @param routerIdTo a ID do roteador para onde a mensagem deverá chegar
     * @return o custo do enlace
     */
    public int getCost(String routerIdFrom, String routerIdTo){
            for (int i = 0; i < linksList.size(); i++) {
                    Link linkAtual = linksList.get(i);
                    if ((linkAtual.getRouterA().equals(routerIdFrom) && linkAtual.getRouterB().equals(routerIdTo))
                                    || linkAtual.getRouterB().equals(routerIdFrom) && linkAtual.getRouterA().equals(routerIdTo)){
                            return Integer.parseInt(linkAtual.getCost());
                    }
            }
            return 999;
    }

}