package myIngrediBox.agents.inventoryManager;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import myIngrediBox.shared.behaviours.ReadIngredientsFromFile;

public class InventoryManagerAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void setup() {
		super.setup();

		ReadIngredientsFromFile loadInventory = new ReadIngredientsFromFile("path");
		SequentialBehaviour manageInventory = new SequentialBehaviour();

		manageInventory.addSubBehaviour(loadInventory);

		loadInventory.setDataStore(manageInventory.getDataStore());

		this.addBehaviour(manageInventory);
	}
}
