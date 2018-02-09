package myIngrediBox.agents.ingrediBuyer;

import myIngrediBox.ontologies.PurchasableIngredient;

public class BuyLowLeftovers extends BuyingController {
	@Override
	protected final Boolean buyingStrategyMatch(PurchasableIngredient ingredient,
			PurchasableIngredient ingredientToCompare) {
		return ingredient.getQuantity() < ingredientToCompare.getQuantity();
	}
}
