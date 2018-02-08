package myIngrediBox.ontologies;

import java.util.ArrayList;

import jade.content.AgentAction;

public class SendPurchase implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<PurchasableIngredient> boughtIngredients;

	public ArrayList<PurchasableIngredient> getBoughtIngredients() {
		return boughtIngredients;
	}

	public void setBoughtIngredients(ArrayList<PurchasableIngredient> boughtIngredients) {
		this.boughtIngredients = boughtIngredients;
	}

}
