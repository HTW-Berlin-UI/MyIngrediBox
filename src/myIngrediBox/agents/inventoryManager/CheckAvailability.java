package myIngrediBox.agents.inventoryManager;

import java.util.ArrayList;
import java.util.Iterator;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.ontologies.Ingredient;

public class CheckAvailability extends OneShotBehaviour
{

	private static final long serialVersionUID = 1L;
	private InventoryManagerAgent inventoryManagerAgent;
	private ArrayList<Ingredient> inventory;
	private ArrayList<Ingredient> requestedIngredients;

	public CheckAvailability(Agent a)
	{
		this.inventoryManagerAgent = (InventoryManagerAgent) a;

	}

	@Override
	public void action()
	{
		this.inventory = inventoryManagerAgent.getInventory();
		this.requestedIngredients = inventoryManagerAgent.getRequestedIngredients();
		
		Iterator<Ingredient> ingredientIterator = inventory.iterator();
		Iterator<Ingredient> requestIterator = requestedIngredients.iterator();
		
		while (requestIterator.hasNext())
		{	
			Ingredient requestIngredient = (Ingredient) requestIterator.next();
			boolean b = inventory.retainAll(requestedIngredients);
			if(inventory.contains(requestIngredient)) {
				System.out.println("Yes, I have " + requestIngredient.getName());
			}
			else {
				System.out.println("No, I don't have " + requestIngredient.getName());
			}
		}
	}

	@Override
	public void reset()
	{
		super.reset();
		inventoryManagerAgent = null;
		inventory = null;
		requestedIngredients = null;
	}

}
