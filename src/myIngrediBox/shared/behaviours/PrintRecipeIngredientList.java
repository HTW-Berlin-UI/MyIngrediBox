package myIngrediBox.shared.behaviours;

import java.util.ArrayList;
import java.util.Iterator;

import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.agents.ingrediBoxManager.IngrediBoxManagerAgent;
import myIngrediBox.agents.inventoryManager.InventoryManagerAgent;
import myIngrediBox.ontologies.Ingredient;

public class PrintRecipeIngredientList extends OneShotBehaviour {

    private static final long serialVersionUID = 1L;

    public PrintRecipeIngredientList() {
	super();
    }

    @Override
    public void action() {
	ArrayList<Ingredient> recipe = (ArrayList<Ingredient>) this.getDataStore().get("recipe");

	if(!recipe.isEmpty()) {
		Iterator<Ingredient> iterator = recipe.iterator();

		System.out.println("\nRecipe:");
		while (iterator.hasNext()) {
		    Ingredient ingredient = iterator.next();
		    System.out.print(ingredient.getName() + "\t");
		    System.out.print(ingredient.getQuantity() + "\t");
		    System.out.println(ingredient.getUnit());
		}
	}
    }
}
