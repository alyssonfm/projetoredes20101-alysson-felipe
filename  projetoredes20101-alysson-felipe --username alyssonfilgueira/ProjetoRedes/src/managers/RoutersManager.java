package managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

import router.Link;
import router.RouteTable;
import router.Router;

public class RoutersManager {
	private static final String FILE_TO_READ = "./roteador.config";
	private LinkedList<Router> routersList = new LinkedList<Router>();
	private LinksManager linksManager;
	
	public RoutersManager() {
		linksManager = new LinksManager();
		linksManager.readFile();
		this.readFile();
	}
	
	public void readFile() {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(FILE_TO_READ));
			String line = buffer.readLine();
			StringTokenizer tokens;
			while (line != null) {
				if (!line.startsWith("#")) {
					tokens = new StringTokenizer(line);
					String routerId = tokens.nextToken(" ");
					int port = Integer.parseInt(tokens.nextToken(" "));
					InetAddress ipAddress = InetAddress.getByName(tokens.nextToken(" "));
					routersList.add(new Router(routerId, ipAddress, port));
				}
				line = buffer.readLine();
			}
			for (int i = 1; i < routersList.size() + 1; i++) {
				RouteTable routeTable = new RouteTable(Integer.toString(i), routersList.size());
				routersList.get(i-1).setRouteTable(routeTable);
			}
		} catch (IOException ioe) {
			System.out.println("Erro na leitura do arquivo roteador.config");
		}
	}
	
	public Router getRouterById(String id) {
		for (int i = 0; i < routersList.size(); i++) {
			if (routersList.get(i).getId().equalsIgnoreCase(id)) return routersList.get(i);
		}
		return null;
	}
	
	public boolean contains(String id) {
		for (int i = 0; i < routersList.size(); i++) {
			if (routersList.get(i).getId().equalsIgnoreCase(id)) return true;
		}
		return false;
	}
	
	//Returns the # of routers
	public int getSize() {
		return routersList.size();
	}
	
	public String toString() {
		String result = "";
		for (int i = 0; i < routersList.size(); i++) {
			result += routersList.get(i).toString()+"\n";
		}
		return result;
	}

	public HashMap<String, Integer> getNeighbors(String routerId) {
		return linksManager.getNeighbors(routerId);
	}

	private String getRouterIdByPort(int port) {
		for (int i = 0; i < routersList.size(); i++) {
			if(routersList.get(i).getPort() == port) return routersList.get(i).getId(); 
		}
		return "";
	}

	public void setRouterOn(int port) {
		for (int i = 0; i < routersList.size(); i++) {
			if(routersList.get(i).getPort() == port) routersList.get(i).setOn(true);	
			
		}
		
	}

	public void setRouterOff(int port) {
		for (int i = 0; i < routersList.size(); i++) {
			if(routersList.get(i).getPort() == port) routersList.get(i).setOn(false);	
			
		}
	}

	public Integer getCost(String id, String id2) {
		return linksManager.getCost(id, id2);
	}
	
	public LinkedList<Router> getRouterList() {
		return this.routersList;
	}
  



}