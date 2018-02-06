package myIngrediBox.shared.behaviours;

import java.util.ArrayList;
import java.util.Iterator;

import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.agents.ingrediBoxManager.IngrediBoxManagerAgent;
import myIngrediBox.agents.inventoryManager.InventoryManagerAgent;
import myIngrediBox.ontologies.Ingredient;

public class PrintRecipeIngredientList extends OneShotBehaviour
{
	private ArrayList<Ingredient> ingredients;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PrintRecipeIngredientList(ArrayList<Ingredient> ingredients) {
		super();
		this.ingredients = ingredients;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		IngrediBoxManagerAgent ingrediBoxManagerAgent = (IngrediBoxManagerAgent) this.getAgent();

		Iterator<Ingredient> iterator = ingrediBoxManagerAgent.getRecipe().iterator();

		System.out.println("\nRecipe:");
		while (iterator.hasNext()) {
			Ingredient ingredient = iterator.next();
			System.out.print(ingredient.getName() + "\t");
			System.out.print(ingredient.getQuantity() + "\t");
			System.out.println(ingredient.getUnit());
		}

	}
}
