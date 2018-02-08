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
import myIngrediBox.shared.behaviours.DeregisterServiceBehaviour;
import myIngrediBox.shared.behaviours.PrintIngredientList;
import myIngrediBox.shared.behaviours.ReadFromFile;
import myIngrediBox.shared.behaviours.RegisterServiceBehaviour;

public class InventoryManagerAgent extends Agent {

	private ArrayList<Ingredient> inventory;
	private ArrayList<Ingredient> requestedIngredients;

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
		// Register Service
		RegisterServiceBehaviour registerServiceBehaviour = new RegisterServiceBehaviour(this,
				"Inventory-Managing-Service");

		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(ontology);

		// this.inventory = new ArrayList<Ingredient>();

		// Load Inventory
		ReadFromFile loadInventory = new ReadFromFile("assets/inventory/inventory.json");
		ParseInventory parseInventory = new ParseInventory();
		PrintIngredientList printIngredientBehaviour = new PrintIngredientList(this.inventory);
		// CheckAvailability checkAvailability = new CheckAvailability(this);
		SequentialBehaviour manageInventory = new SequentialBehaviour();

		manageInventory.addSubBehaviour(loadInventory);
		manageInventory.addSubBehaviour(parseInventory);
		manageInventory.addSubBehaviour(registerServiceBehaviour);
		manageInventory.addSubBehaviour(printIngredientBehaviour);
		// manageInventory.addSubBehaviour(checkAvailability);

		loadInventory.setDataStore(manageInventory.getDataStore());
		parseInventory.setDataStore(manageInventory.getDataStore());

		AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		// react to message matching the template
		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchConversationId("inventory-request"),
				MessageTemplate.MatchOntology(ontology.getName()));

		ReceiveRequest receiveRequest = new ReceiveRequest(this, mt);

		this.addBehaviour(receiveRequest);

		this.addBehaviour(manageInventory);

	} // End setup()

	@Override
	protected void takeDown() {
		super.takeDown();
		this.addBehaviour(new DeregisterServiceBehaviour(this));
	}

	public ArrayList<Ingredient> getInventory() {
		return inventory;
	}

	public void setInventory(ArrayList<Ingredient> inventory) {
		this.inventory = inventory;
	}

	public ArrayList<Ingredient> getRequestedIngredients() {
		return requestedIngredients;
	}

	public void setRequestedIngredients(ArrayList<Ingredient> requestedIngredients) {
		this.requestedIngredients = requestedIngredients;
	}

}
