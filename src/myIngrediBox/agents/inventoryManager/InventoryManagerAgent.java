package myIngrediBox.agents.inventoryManager;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.MessageTemplate;
import myIngrediBox.ontologies.IngrediBoxOntology;
import myIngrediBox.shared.behaviours.DeregisterServiceBehaviour;
import myIngrediBox.shared.behaviours.PrintIngredientList;
import myIngrediBox.shared.behaviours.ReadFromFile;
import myIngrediBox.shared.behaviours.RegisterServiceBehaviour;

public class InventoryManagerAgent extends Agent {

	private static final long serialVersionUID = 1L;

	protected void setup() {
		super.setup();
		
		
		// initialize behaviour to manage inventory
		SequentialBehaviour manageInventory = new SequentialBehaviour();
		
		// Register Service
		RegisterServiceBehaviour registerServiceBehaviour = new RegisterServiceBehaviour(this,
				"Inventory-Managing-Service");

		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(IngrediBoxOntology.getInstance());
		
		// Load Inventory
		ReadFromFile loadInventory = new ReadFromFile("assets/inventory/inventory.json");
		ParseInventory parseInventory = new ParseInventory();
		PrintIngredientList printInventoryBehaviour = new PrintIngredientList(this);

		manageInventory.addSubBehaviour(loadInventory);
		manageInventory.addSubBehaviour(parseInventory);
		manageInventory.addSubBehaviour(registerServiceBehaviour);
		manageInventory.addSubBehaviour(printInventoryBehaviour);		

		// Share DataStore with sequential behaviour 'magageInventory'
		loadInventory.setDataStore(manageInventory.getDataStore());
		parseInventory.setDataStore(manageInventory.getDataStore());
		printInventoryBehaviour.setDataStore(manageInventory.getDataStore());

		// React to message matching the template
		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchConversationId("inventory-request"),
				MessageTemplate.MatchOntology(IngrediBoxOntology.getInstance().getName()));
		// Receive ingredient request and response with sending available ingredients
		RequestResponse requestResponse = new RequestResponse(this, mt, manageInventory.getDataStore());
		
		manageInventory.addSubBehaviour(requestResponse);
				
		this.addBehaviour(manageInventory);

	} // End setup()

	@Override
	protected void takeDown() {
		this.addBehaviour(new DeregisterServiceBehaviour(this));
		super.takeDown();
	}
	

}
