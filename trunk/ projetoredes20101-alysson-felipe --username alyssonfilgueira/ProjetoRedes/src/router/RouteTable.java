package router;

import java.util.HashMap;

public class RouteTable {
	private static int INFINITY = 999;
	//<Id, custo>
	private HashMap<String, Integer> routes;
	private int size = 0; //the size of this table;
	private String routerId = ""; //the router that this table belongs to;
	
	public RouteTable(String routerId, int size) {
		routes = new HashMap<String, Integer>();
		this.routerId = routerId;
		this.size = size;
		routes.put(this.routerId, new Integer(0));
		this.completeTable();
	}
	
	private void completeTable() {
		for (int i = 1; i < this.size+1; i++) {
			if(!(Integer.toString(i).equalsIgnoreCase(this.routerId))) {
				routes.put(Integer.toString(i), new Integer(INFINITY));
			}
		}
		
	}

	public void setDistanceTo(String routerId, Integer distance) {
		routes.put(routerId, distance);
	}
	
	public Integer getDistanceTo(String routerId) {
		return routes.get(routerId);
	}
	
	public String toString() {
		String result = "Router's "+routerId+" table:\n";
		for (int i = 1; i < this.size+1; i++) {
			result += " > " + Integer.toString(i) + 
			" [" + routes.get(Integer.toString(i)).toString() +"]\n";
		}
		return result;
		
	}

	public String asString() {
		String result = "";
		for (int i = 1; i < this.size+1; i++) {
			result += routes.get(Integer.toString(i)).toString() +" ";
		}
		return result.trim();
	}
}