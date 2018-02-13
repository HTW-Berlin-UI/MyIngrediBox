package myIngrediBox.agents.ingrediBuyer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

	abstract protected Boolean buyingStrategyMatch(PurchasableIngredient ingredient,
			PurchasableIngredient ingredientToCompare);

	public HashMap<PurchasableIngredient, AID> getOptimizedShoppingList() {

		HashMap<PurchasableIngredient, AID> shoppingList = new HashMap<PurchasableIngredient, AID>();

		for (AID market : this.offers.keySet()) {

			ArrayList<PurchasableIngredient> betterIngredients = new ArrayList<PurchasableIngredient>();

			for (PurchasableIngredient ingredient : this.offers.get(market)) {

				// if proposed ingredient is served by another market and is cheaper than the
				// current one
				if (shoppingList.containsKey(ingredient)) {

					// find the comparable ingredient on shopping list
					Iterator<PurchasableIngredient> iterator = shoppingList.keySet().iterator();

					while (iterator.hasNext()) {

						PurchasableIngredient ingredientOnList = iterator.next();

						if (ingredientOnList.equals(ingredient)) {

							// now decide whether to replace the item on the list with new ingredient
							if (this.buyingStrategyMatch(ingredient, ingredientOnList)) {
								iterator.remove();
								betterIngredients.add(ingredient);

							}

						}

					}

				} else {
					// if its a new ingredient to our list
					shoppingList.put(ingredient, market);
				}

			}

			betterIngredients.forEach(ingredient -> shoppingList.put(ingredient, market));

		}
		return shoppingList;
	}

}
