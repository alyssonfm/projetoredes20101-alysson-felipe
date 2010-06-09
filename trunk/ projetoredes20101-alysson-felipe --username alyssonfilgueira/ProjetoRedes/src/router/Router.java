package router;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import managers.RoutersManager;

import server.UDPServer;

import client.UDPClient;


public class Router {
	
	private String id;
	private InetAddress ipAddress;
	private int port;
	private RouteTable routeTable;
	//<Id, custo>
	private HashMap<String, Integer> neighbors = new HashMap<String, Integer>();
	private UDPClient client;
	private UDPServer server;
	private boolean isOn = false;
	private RoutersManager routerManager;
	
	public Router(String id, InetAddress ipAddress, int port)  {
		this.id = id;
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public InetAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public RouteTable getRouteTable() {
		return routeTable;
	}

	public void setRouteTable(RouteTable routeTable) {
		this.routeTable = routeTable;
	}
	
	public HashMap<String, Integer> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(HashMap<String, Integer> neighbors) {
		this.neighbors = neighbors;
	}

	public UDPClient getClient() {
		return client;
	}

	public void setClient(UDPClient client) {
		this.client = client;
	}

	public UDPServer getServer() {
		return server;
	}

	public void setServer(UDPServer server) {
		this.server = server;
	}
	
	public boolean isOn() {
		return this.isOn;
	}
	
	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}

	public void setRouterManager(RoutersManager routerManager) {
		this.routerManager = routerManager;
	}
	public String toString() {
		return this.id + " " + this.ipAddress.toString() + " " + Integer.toString(this.port) + "\n" + routeTable.toString();
	}
	
	@SuppressWarnings("unchecked")
	public void printNeighbors() {
		Set keySet = neighbors.keySet();
		Iterator iterator = keySet.iterator();
		System.out.println("Vizinhos do roteador");
		while(iterator.hasNext()) {
			System.out.println(iterator.next().toString());
		}
	}

	public void setTable(RouteTable routerTable) {
		this.routeTable = routerTable;
		
	}

	@SuppressWarnings("unchecked")
	public void pingNeighbors(HashMap<String, Integer> neighbors) {
//		System.out.println("Avisando vizinhos que fui ligado...");
		Set keySet = neighbors.keySet();
		Iterator iterator = keySet.iterator();
		while(iterator.hasNext()) {
//			System.out.println("a");
			Router tempRouter = routerManager.getRouterById(iterator.next().toString());
			UDPClient udpClient = new UDPClient(tempRouter.getIpAddress(), tempRouter.getPort());
			udpClient.setRouter(this);
			udpClient.start();
		}
		
	}


	public void updateTable(String neighborID/*, String nextToken*/) {
//		int distanceToNeighbor = neighbors.get(neighborID);
//		routeTable.setDistanceTo(neighborID, distanceToNeighbor);
        
		Router router = this.routerManager.getRouterById(neighborID);
//		RouteTable neighborTable = router.getRouteTable();
		boolean updated = bellmanFord(router);
		
		//preciso avisar a meus vizinhos que to com uma tabela nova, senao, sem esse IF
		//ele manda uma tabela antiga que nao permite o calculo correto.
		//por exemplo, ele nunca encontra um caminho do roteador 6 pro 2.
		//porém, contudo, todavia, quando o conteúdo deste IF não está comentado
		//ele, de fato, manda a tabela nova bonitinha e seu vizinho calcula tudo lindamente.
		//mas, por algum motivo, quando ele chama esse "next.updateTable(this.getId());"
		//ele mata o processo atual. Nao consegui descobrir o porque.
		if (updated){
			
			/*Set keySet = this.neighbors.keySet();
	        Iterator iterator = keySet.iterator();
	        while(iterator.hasNext()) {
	        	Router next = this.routerManager.getRouterById(iterator.next().toString());
	        	if (next.isOn){
	        		System.out.println("vou atualizar agora o " + next.id);
	                next.updateTable(this.getId());
	            }
	        }*/
			
		}
		
	}
    
    /**
     * Executa o algoritmo do Bellman Ford distribuido
     * @param neighbor o roteador vizinho que foi recentemente atualizado
     * @param router o roteador atual
     * @return true, caso as modificaÃ§Ãµes do vizinho tenham causado alguma alteraÃ§Ã£o na tabela do
     * roteador atual; false, caso contrÃ¡rio.
     */
    private boolean bellmanFord(Router neighbor/*, Router router*/){
        boolean wasUpdated = false;
        RouteTable table = this.routeTable;
        RouteTable neighborTable = neighbor.getRouteTable();
        
        //atualiza a minha table com a custo do meu vizinho ate mim
        if (table.getDistanceTo(neighbor.getId())==999){
                table.setDistanceTo(neighbor.getId(), routerManager.getCost(neighbor.getId(), this.getId()));
                wasUpdated = true;
        }
        
        //verifica se a minha distancia ate todos os roteadores Ã© realmente a menor, calculando atraves da tabela do vizinho 
        for (int i = 0; i < routerManager.getSize(); i++) {
        	int distanceToNeighbor = neighbors.get(neighbor.getId());
//        	System.out.println("Distancia do "+neighbor.getId()+" ate "+routerManager.getRouterList().get(i).getId());
//        	System.out.println(neighborTable.getDistanceTo(routerManager.getRouterList().get(i).getId()));
        	int newDistance = distanceToNeighbor + neighborTable.getDistanceTo(routerManager.getRouterList().get(i).getId());
//                int newDistance = neighborTable.getDistanceTo(this.getId()) + neighborTable.getDistanceTo(routerManager.getRouterList().get(i).getId());
//                System.out.println(newDistance +" "+table.getDistanceTo(routerManager.getRouterList().get(i).getId()) );
                if (table.getDistanceTo(routerManager.getRouterList().get(i).getId()) > newDistance){
                	System.out.println("cheguei alguma vez");
                        table.setDistanceTo(routerManager.getRouterList().get(i).getId(), newDistance);
                        wasUpdated = true;
                }
        }
            
            return wasUpdated;
    }
	

	public void setNeighborOn(int port) {
		this.routerManager.setRouterOn(port);
		
	}

	public void setNeighborOff(int serverPort) {
		this.routerManager.setRouterOff(port);
		
	}

}