package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;
import java.util.Iterator;

import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.ontologies.Ingredient;

public class CreateShoppingList extends OneShotBehaviour
{

	private static final long serialVersionUID = 1L;

	@Override
	public void action()
	{
		
		availableIngredientList = availableIngredientReceivingAction.getIngredients();
		shoppingList = ingrediBoxManagerAgent.getShoppingList();
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
							} else if (recipeIngredient.getQuantity() == availableIngredient.getQuantity())
							{
								recipeIterator.remove();
							}
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
		
		
	}

}
