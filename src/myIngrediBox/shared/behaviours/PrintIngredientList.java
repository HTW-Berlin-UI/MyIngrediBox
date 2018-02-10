package myIngrediBox.shared.behaviours;

import java.util.ArrayList;
import java.util.Iterator;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.agents.inventoryManager.InventoryManagerAgent;
import myIngrediBox.ontologies.Ingredient;

public class PrintIngredientList extends OneShotBehaviour {

    private static final long serialVersionUID = 1L;

    public PrintIngredientList(Agent a) {
	super(a);
    }


    @Override
    public void action() {

	ArrayList<Ingredient> inventory = (ArrayList<Ingredient>) this.getDataStore().get("inventory");

	if (!inventory.isEmpty()) {

	    Iterator<Ingredient> iterator = inventory.iterator();

	    System.out.println("\nInventory:");
	    while (iterator.hasNext()) {
		Ingredient ingredient = iterator.next();
		System.out.print(ingredient.getName() + "\t");
		System.out.print(ingredient.getQuantity() + "\t");
		System.out.println(ingredient.getUnit());
	    }
	}

    }

}
