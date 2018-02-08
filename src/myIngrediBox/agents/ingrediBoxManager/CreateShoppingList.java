package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;
import java.util.Iterator;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.ontologies.Ingredient;

public class CreateShoppingList extends OneShotBehaviour
{

	private static final long serialVersionUID = 1L;
	private IngrediBoxManagerAgent ingrediBoxManagerAgent;

	public CreateShoppingList(Agent a)
	{
		super();
		this.ingrediBoxManagerAgent = (IngrediBoxManagerAgent) a;
	}

	@Override
	public void action()
	{
			
		ArrayList<Ingredient> availableIngredientList = ingrediBoxManagerAgent.getAvailableIngredientList();
		ArrayList<Ingredient> shoppingList = ingrediBoxManagerAgent.getShoppingList();
		ArrayList<Ingredient> recipe = ingrediBoxManagerAgent.getRecipe();

		Iterator<Ingredient> availableIngredientIterator = availableIngredientList.iterator();
		Iterator<Ingredient> recipeIterator = recipe.iterator();

		if (!availableIngredientList.isEmpty())
		{
			// check order necessity
			while (recipeIterator.hasNext())
			{
				Ingredient recipeIngredient = (Ingredient) recipeIterator.next();

				if (availableIngredientIterator.hasNext())
				{
					Ingredient availableIngredient = (Ingredient) availableIngredientIterator.next();

					if (availableIngredientList.contains(recipeIngredient))
					{

						int indexRp = recipe.indexOf(availableIngredient);

						boolean haveSameUnit = recipeIngredient.getUnit().equals(availableIngredient.getUnit());

						// set remaining quantity of request and inventory ingredient
						if (haveSameUnit)
						{
							if (recipeIngredient.getQuantity() > availableIngredient.getQuantity())
							{
								recipe.get(indexRp).setQuantity(
										recipeIngredient.getQuantity() - availableIngredient.getQuantity());
								shoppingList.add(recipeIngredient);
							} 
//							else if (recipeIngredient.getQuantity() == availableIngredient.getQuantity())
//							{
//								recipeIterator.remove();
//							}
						}
					}
				}

				// add ingredi's to shoppinglist, which weren't available from inventory
				else
				{
					shoppingList.add(recipeIngredient);
				}
			}
		}
		// Set ShoppingList
		ingrediBoxManagerAgent.setShoppingList(shoppingList);
		
		//Printing available ingredients received
		availableIngredientIterator = availableIngredientList.iterator();
		System.out.println("\nIBM received available ingredients: ");
		while (availableIngredientIterator.hasNext())
		{
			Ingredient ingredient = availableIngredientIterator.next();
			System.out.print(ingredient.getQuantity() + " " + ingredient.getName() + "\t");
		}
		System.out.println("\n");
		
		// Print ingredients, remaining on shoppinglist
		System.out.println("\nThis ingredients remain on shoppinglist after checking inventory: ");
		Iterator<Ingredient> shoppingListIterator = shoppingList.iterator();
		while (shoppingListIterator.hasNext())
		{
			Ingredient ingredient = shoppingListIterator.next();
			System.out.print(ingredient.getQuantity() + " " + ingredient.getName() + "\t");
		}
		System.out.println("\n");
		
	}

}
