package myIngrediBox.agents.inventoryManager;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import myIngrediBox.shared.behaviours.ReadFromFile;

public class InventoryManagerAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void setup() {
		super.setup();

		ReadFromFile loadInventory = new ReadFromFile("assets/inventory/inventory.json");
		ParseInventory parseInventory = new ParseInventory();
		SequentialBehaviour manageInventory = new SequentialBehaviour();

		manageInventory.addSubBehaviour(loadInventory);
		manageInventory.addSubBehaviour(parseInventory);

		loadInventory.setDataStore(manageInventory.getDataStore());
		parseInventory.setDataStore(manageInventory.getDataStore());

		this.addBehaviour(manageInventory);
	}
}
