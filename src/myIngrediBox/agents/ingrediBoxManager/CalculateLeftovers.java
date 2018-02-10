package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;

import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.PurchasableIngredient;

public class CalculateLeftovers extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	public CalculateLeftovers(DataStore datastore) {
		super();
		this.setDataStore(datastore);
	}

	@Override
	public void action() {
		ArrayList<PurchasableIngredient> boughtIngredients = (ArrayList<PurchasableIngredient>) this.getDataStore()
				.get("boughtIngredients");

		ArrayList<Ingredient> shoppingList = (ArrayList<Ingredient>) this.getDataStore().get("shoppingList");

		ArrayList<Ingredient> leftovers = new ArrayList<Ingredient>(boughtIngredients);

		leftovers.forEach(ingredient -> {
			if (shoppingList.contains(ingredient))
				ingredient.setQuantity(
						ingredient.getQuantity() - shoppingList.get(shoppingList.indexOf(ingredient)).getQuantity());
		});

		leftovers.removeIf(ingredient -> ingredient.getQuantity() <= 0);

		this.getDataStore().put("leftovers", leftovers);

	}

}