package myIngrediBox.agents.inventoryManager;

import java.util.ArrayList;

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
		this.inventory = inventoryManagerAgent.getInventory();
		this.requestedIngredients = inventoryManagerAgent.getRequestedIngredients();
	}

	@Override
	public void action()
	{
		
	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub
		super.reset();
	}

}
