package myIngrediBox.agents.inventoryManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.ontologies.Ingredient;

public class CheckAvailability extends OneShotBehaviour
{
	private static final long serialVersionUID = 1L;
	
	private InventoryManagerAgent inventoryManagerAgent;
	private ArrayList<Ingredient> inventory= new ArrayList<>();
	private ArrayList<Ingredient> requestedIngredients = new ArrayList<>();
	private ArrayList<Ingredient> availableIngredients = new ArrayList<>();

	public CheckAvailability(Agent a)
	{
		this.inventoryManagerAgent = (InventoryManagerAgent) a;
	}

	@Override
	public void action()
	{
		this.inventory = inventoryManagerAgent.getInventory();
		this.requestedIngredients = inventoryManagerAgent.getRequestedIngredients();

		Iterator<Ingredient> requestIterator = requestedIngredients.iterator();
		
		// check availability in inventory of requested ingredients
		if (!Collections.disjoint(inventory, requestedIngredients)) {
			System.out.println("Yes, I have");
		}
		while (requestIterator.hasNext())
		{
			Ingredient requestIngredient = (Ingredient) requestIterator.next();

			if (inventory.contains(requestIngredient))
			{
				
				System.out.println("- " + requestIngredient.getName() + "\tQuantity: "
						+ inventory.get(inventory.indexOf(requestIngredient)).getQuantity()
						+ inventory.get(inventory.indexOf(requestIngredient)).getUnit());

				int indexI = inventory.indexOf(requestIngredient);

				Ingredient inventoryIngredient = inventory.get(indexI);

				boolean haveSameUnit = inventoryIngredient.getUnit().equals(requestIngredient.getUnit());

				// set remaining quantity of request and inventory ingredient
				if (haveSameUnit)
				{
					if (inventoryIngredient.getQuantity() > requestIngredient.getQuantity())
					{
						availableIngredients.add(requestIngredient);
						inventory.get(indexI)
								.setQuantity(inventoryIngredient.getQuantity() - requestIngredient.getQuantity());
					} else if (inventoryIngredient.getQuantity() == requestIngredient.getQuantity())
					{
						availableIngredients.add(requestIngredient);
						inventory.remove(indexI);
					} else if (inventoryIngredient.getQuantity() < requestIngredient.getQuantity())
					{
						requestIngredient
								.setQuantity(requestIngredient.getQuantity() - inventoryIngredient.getQuantity());
						inventory.remove(indexI);
						availableIngredients.add(inventoryIngredient);
					}
				}
			} else
			{
				System.out.println("No, I don't have " + requestIngredient.getName());
				// requestIngredient-quantity remains the same
			}
		}
		
		//Update IM's inventory
		inventoryManagerAgent.setInventory(this.inventory);
		//Set ingredients to pass to IBM
		inventoryManagerAgent.setAvailableRequestedIngredients(availableIngredients);
	
		printList(inventory, "Updated Inventory");
	}

	@Override
	public void reset()
	{
		super.reset();
		inventoryManagerAgent = null;
		inventory = null;
		requestedIngredients = null;
		availableIngredients = null;
	}

	public void printList(ArrayList<Ingredient> list, String listname)
	{
		System.out.println("\n" + listname + ":");

		for (Ingredient ingredient : list)
		{
			System.out.print(ingredient.getName() + "\t");
			System.out.print(ingredient.getQuantity() + "\t");
			System.out.println(ingredient.getUnit());
		}

	}

}
