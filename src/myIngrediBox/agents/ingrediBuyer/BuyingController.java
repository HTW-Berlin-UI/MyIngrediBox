package myIngrediBox.agents.ingrediBuyer;

import java.util.ArrayList;
import java.util.HashMap;

import jade.core.AID;
import myIngrediBox.ontologies.PurchasableIngredient;

public abstract class BuyingController {

	protected HashMap<AID, ArrayList<PurchasableIngredient>> offers;

	public BuyingController() {
		this.offers = new HashMap<AID, ArrayList<PurchasableIngredient>>();
	}

	public final void addOffer(AID market, ArrayList<PurchasableIngredient> buyableIngredients) {
		this.offers.put(market, buyableIngredients);
	}

	abstract HashMap<PurchasableIngredient, AID> getOptimizedShoppingList();

}
