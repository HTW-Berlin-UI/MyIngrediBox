package myIngrediBox.agents.inventoryManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.ontologies.Ingredient;

public class CheckAvailability extends OneShotBehaviour {
    private static final long serialVersionUID = 1L;

    public CheckAvailability() {
	super();
    }


    @Override
    public void action() {
	ArrayList<Ingredient> inventory = (ArrayList<Ingredient>) this.getDataStore().get("inventory");
	ArrayList<Ingredient> requestedIngredients = (ArrayList<Ingredient>) this.getDataStore().get("requestedIngredients");
	ArrayList<Ingredient> availableIngredients = new ArrayList<>();
	
	Iterator<Ingredient> requestIterator = requestedIngredients.iterator();

	// Check availability of requested ingredients
	if (!Collections.disjoint(inventory, requestedIngredients)) {
	    System.out.println("Yes, I have");
	}
	while (requestIterator.hasNext()) {
	    Ingredient requestIngredient = (Ingredient) requestIterator.next();

	    if (inventory.contains(requestIngredient)) {

		System.out.println("- " + requestIngredient.getName() + "\tQuantity: "
			+ inventory.get(inventory.indexOf(requestIngredient)).getQuantity()
			+ inventory.get(inventory.indexOf(requestIngredient)).getUnit());

		int indexI = inventory.indexOf(requestIngredient);

		Ingredient inventoryIngredient = inventory.get(indexI);

		boolean haveSameUnit = inventoryIngredient.getUnit().equals(requestIngredient.getUnit());

		// Set remaining quantity of request and inventory ingredient
		if (haveSameUnit) {
		    if (inventoryIngredient.getQuantity() > requestIngredient.getQuantity()) {
			availableIngredients.add(requestIngredient);
			inventory.get(indexI)
				.setQuantity(inventoryIngredient.getQuantity() - requestIngredient.getQuantity());
		    } else if (inventoryIngredient.getQuantity() == requestIngredient.getQuantity()) {
			availableIngredients.add(requestIngredient);
			inventory.remove(indexI);
		    } else if (inventoryIngredient.getQuantity() < requestIngredient.getQuantity()) {
			inventory.remove(indexI);
			availableIngredients.add(inventoryIngredient);
		    }
		}
	    } else // Case if requested ingredient isn't available
	    {
		System.out.println("No, I don't have " + requestIngredient.getName());
		// requestIngredient-quantity remains the same
	    }
	}

	// Update IM's inventory
	this.getDataStore().put("inventory", inventory);

	// Set ingredients to pass to IBM
	this.getDataStore().put("availableIngredients", availableIngredients);

	printList(inventory, "Updated Inventory");
    }

    @Override
    public void reset() {
	super.reset();
    }

    public void printList(ArrayList<Ingredient> list, String listname) {
	System.out.println("\n" + listname + ":");

	for (Ingredient ingredient : list) {
	    System.out.print(ingredient.getName() + "\t");
	    System.out.print(ingredient.getQuantity() + "\t");
	    System.out.println(ingredient.getUnit());
	}

    }

}
