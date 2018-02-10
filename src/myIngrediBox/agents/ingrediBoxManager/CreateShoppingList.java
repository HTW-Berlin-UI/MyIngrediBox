package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;
import java.util.Iterator;

import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.ontologies.Ingredient;

public class CreateShoppingList extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	public CreateShoppingList() {
		super();
	}

	// Creates shoppingList, which contains ingredients unavailable from inventory
	@Override
	public void action() {

		ArrayList<Ingredient> availableIngredientList = (ArrayList<Ingredient>) this.getDataStore()
				.get("availableIngredients");
		ArrayList<Ingredient> shoppingList = new ArrayList<>();
		ArrayList<Ingredient> recipe = (ArrayList<Ingredient>) this.getDataStore().get("recipe");
		ArrayList<Ingredient> requiredIngredients = new ArrayList<Ingredient>(recipe);

		Iterator<Ingredient> availableIngredientIterator = availableIngredientList.iterator();
		Iterator<Ingredient> recipeIterator = requiredIngredients.iterator();

		if (!availableIngredientList.isEmpty()) {
			// Check order necessity
			while (recipeIterator.hasNext()) {
				Ingredient recipeIngredient = (Ingredient) recipeIterator.next();

				if (availableIngredientIterator.hasNext()) {
					Ingredient availableIngredient = (Ingredient) availableIngredientIterator.next();

					if (availableIngredientList.contains(recipeIngredient)) {

						int indexRp = requiredIngredients.indexOf(availableIngredient);

						boolean haveSameUnit = recipeIngredient.getUnit().equals(availableIngredient.getUnit());

						// Set remaining quantity of request and inventory ingredient
						if (haveSameUnit) {
							if (recipeIngredient.getQuantity() > availableIngredient.getQuantity()) {
								requiredIngredients.get(indexRp).setQuantity(
										recipeIngredient.getQuantity() - availableIngredient.getQuantity());
								shoppingList.add(recipeIngredient);
							}
						}
					}
				}

				// Add ingredi's to shoppinglist, which weren't available from inventory
				else {
					shoppingList.add(recipeIngredient);
				}
			}
		}
		// Set ShoppingList
		this.getDataStore().put("shoppingList", shoppingList);

		// Printing available ingredients received
		availableIngredientIterator = availableIngredientList.iterator();
		System.out.println("\nIBM received available ingredients: ");
		while (availableIngredientIterator.hasNext()) {
			Ingredient ingredient = availableIngredientIterator.next();
			System.out.print(ingredient.getQuantity() + " " + ingredient.getName() + "\t");
		}
		System.out.println("\n");

		// Print ingredients, remaining on shoppinglist
		System.out.println("\nThis ingredients remain on shoppinglist after checking inventory: ");
		Iterator<Ingredient> shoppingListIterator = shoppingList.iterator();
		while (shoppingListIterator.hasNext()) {
			Ingredient ingredient = shoppingListIterator.next();
			System.out.print(ingredient.getQuantity() + " " + ingredient.getName() + "\t");
		}
		System.out.println("\n");

	}

}
