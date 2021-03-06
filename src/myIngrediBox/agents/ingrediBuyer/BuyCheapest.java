package myIngrediBox.agents.ingrediBuyer;

import myIngrediBox.ontologies.PurchasableIngredient;

public class BuyCheapest extends BuyingController {

	@Override
	protected final Boolean buyingStrategyMatch(PurchasableIngredient ingredient,
			PurchasableIngredient ingredientToCompare) {
		return ingredient.getPrice() < ingredientToCompare.getPrice();
	}

}
