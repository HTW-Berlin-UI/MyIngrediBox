package myIngrediBox.agents.inventoryManager;

import java.util.ArrayList;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import myIngrediBox.ontologies.IngrediBoxOntology;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.IngredientSendingAction;
import myIngrediBox.ontologies.Unit;
import myIngrediBox.shared.behaviours.DeregisterServiceBehaviour;
import myIngrediBox.shared.behaviours.PrintIngredientList;
import myIngrediBox.shared.behaviours.ReadFromFile;
import myIngrediBox.shared.behaviours.RegisterServiceBehaviour;

public class InventoryManagerAgent extends Agent {

	private static final long serialVersionUID = 1L;

	/**
	 * message language FIPA-SL
	 */
	private Codec codec = new SLCodec();

	/**
	 * ontology used for semantic parsing
	 */
	private Ontology ontology = IngrediBoxOntology.getInstance();

	protected void setup() {
		super.setup();
		
		
		// initialize behaviour to manage inventory
		SequentialBehaviour manageInventory = new SequentialBehaviour();
		
		// Register Service
		RegisterServiceBehaviour registerServiceBehaviour = new RegisterServiceBehaviour(this,
				"Inventory-Managing-Service");

		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(ontology);
		
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
				MessageTemplate.MatchOntology(ontology.getName()));
		// Receive ingredient request and response with sending available ingredients
		RequestResponse requestResponse = new RequestResponse(this, mt, manageInventory.getDataStore());
		
		manageInventory.addSubBehaviour(requestResponse);
				
		this.addBehaviour(manageInventory);

	} // End setup()

	@Override
	protected void takeDown() {
		super.takeDown();
		this.addBehaviour(new DeregisterServiceBehaviour(this));
	}
	

	public Codec getCodec()
	{
		return codec;
	}

	public void setCodec(Codec codec)
	{
		this.codec = codec;
	}

	public Ontology getOntology()
	{
		return ontology;
	}

	public void setOntology(Ontology ontology)
	{
		this.ontology = ontology;
	}

}
