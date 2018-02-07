package myIngrediBox.agents.inventoryManager;

import java.util.ArrayList;
import java.util.Iterator;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.shared.behaviours.PrintIngredientList;

public class CheckAvailability extends OneShotBehaviour
{

	private static final long serialVersionUID = 1L;
	private InventoryManagerAgent inventoryManagerAgent;
	private ArrayList<Ingredient> inventory;
	private ArrayList<Ingredient> requestedIngredients;
	private ArrayList<Ingredient> ingredientBasket = new ArrayList<>();

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

		// check availability
		while (requestIterator.hasNext())
		{
			Ingredient requestIngredient = (Ingredient) requestIterator.next();

			if (inventory.contains(requestIngredient))
			{
				System.out.println("Yes, I have " + requestIngredient.getName() + "\t Quantity: "
						+ inventory.get(inventory.indexOf(requestIngredient)).getQuantity());

				int indexI = inventory.indexOf(requestIngredient);
				int indexR = requestedIngredients.indexOf(requestIngredient);

				Ingredient inventoryIngredient = inventory.get(indexI);

				boolean haveSameUnit = inventoryIngredient.getUnit().equals(requestIngredient.getUnit());

				// set remaining quantity of request and inventory ingredient
				if (haveSameUnit)
				{
					if (inventoryIngredient.getQuantity() > requestIngredient.getQuantity())
					{
						ingredientBasket.add(requestIngredient);
						inventory.get(indexI)
								.setQuantity(inventoryIngredient.getQuantity() - requestIngredient.getQuantity());
						requestIterator.remove();
					} else if (inventoryIngredient.getQuantity() == requestIngredient.getQuantity())
					{
						ingredientBasket.add(requestIngredient);
						inventory.remove(indexI);
						requestIterator.remove();
					} else if (inventoryIngredient.getQuantity() < requestIngredient.getQuantity())
					{
						requestIngredient
								.setQuantity(requestIngredient.getQuantity() - inventoryIngredient.getQuantity());
						inventory.remove(indexI);
						ingredientBasket.add(requestIngredient);
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
		inventoryManagerAgent.setAvailableRequestedIngredients(ingredientBasket);
	
		printList(inventory, "Updated Inventory");
		printList(requestedIngredients, "Left Request (left to buy)");
		printList(ingredientBasket, "Ingredient Basket (to send to IBM)");
	}

	@Override
	public void reset()
	{
		super.reset();
		inventoryManagerAgent = null;
		inventory = null;
		requestedIngredients = null;
		ingredientBasket = null;
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
