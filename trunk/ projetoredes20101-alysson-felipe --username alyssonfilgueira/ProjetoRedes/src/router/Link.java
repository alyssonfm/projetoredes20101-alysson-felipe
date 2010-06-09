package router;

public class Link {

	private String routerA;
	private String routerB;
	private String cost;
	
	public Link(String routerA, String routerB, String cost) {
		this.routerA = routerA;
		this.routerB = routerB;
		this.cost = cost;
	}

	public String getRouterA() {
		return routerA;
	}

	public void setRouterA(String routerA) {
		this.routerA = routerA;
	}

	public String getRouterB() {
		return routerB;
	}

	public void setRouterB(String routerB) {
		this.routerB = routerB;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
	
	public String toString() {
		return this.routerA + " > " + this.routerB + " [" + this.cost + "]";
	}
	
}