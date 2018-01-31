package myIngrediBox.ontologies;

import java.util.ArrayList;

import jade.content.AgentAction;
import jade.core.AID;

public class TradeIngredients implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<PurchasableIngredient> ingredients;

	private AID trader;

	public ArrayList<PurchasableIngredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<PurchasableIngredient> ingredients) {
		this.ingredients = ingredients;
	}

	public AID getTrader() {
		return trader;
	}

	public void setTrader(AID trader) {
		this.trader = trader;
	}

}
