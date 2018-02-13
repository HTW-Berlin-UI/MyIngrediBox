package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;

import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.Purchase;

public class CalculateLeftovers extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	public CalculateLeftovers(DataStore datastore) {
		super();
		this.setDataStore(datastore);
	}

	@Override
	public void action() {
		ArrayList<Purchase> purchases = (ArrayList<Purchase>) this.getDataStore().get("purchases");

		ArrayList<Ingredient> shoppingList = (ArrayList<Ingredient>) this.getDataStore().get("shoppingList");

		ArrayList<Ingredient> leftovers = new ArrayList<Ingredient>();

		purchases.forEach(
				purchase -> purchase.getBoughtIngredients().forEach(ingredient -> leftovers.add(ingredient.clone())));

		leftovers.forEach(ingredient -> {
			if (shoppingList.contains(ingredient))
				ingredient.setQuantity(
						ingredient.getQuantity() - shoppingList.get(shoppingList.indexOf(ingredient)).getQuantity());
		});

		leftovers.removeIf(ingredient -> ingredient.getQuantity() <= 0);

		this.getDataStore().put("leftovers", leftovers);

	}

}
