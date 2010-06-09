import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import router.Router;
import server.UDPServer;
import managers.RoutersManager;



public class Main {
	
	public static void roda(final String test){
		final RoutersManager routersManager = new RoutersManager();
		final Router router = routersManager.getRouterById(test);
		router.setOn(true);
		router.setNeighbors(routersManager.getNeighbors(test));
		router.setRouterManager(routersManager);
		
		UDPServer udpServer = new UDPServer(router.getPort());
		udpServer.setRouter(router);
		udpServer.start();

		int delay = 10000; //milliseconds
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//...Perform a task...
//				System.out.println("Oi");
				System.out.println(router.toString());
				router.pingNeighbors(routersManager.getNeighbors(test));
			}
		};
		new Timer(delay, taskPerformer).start();
		
	}
	
	public static void main(String[] args) {
		final String test = "1";
		roda(test);
	}
}