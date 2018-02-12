package myIngrediBox.ontologies;

import java.util.ArrayList;

import jade.content.AgentAction;

public class SendPurchases implements AgentAction {

	private static final long serialVersionUID = 1L;

	private ArrayList<Purchase> purchases;

	public ArrayList<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(ArrayList<Purchase> purchases) {
		this.purchases = purchases;
	}

}
