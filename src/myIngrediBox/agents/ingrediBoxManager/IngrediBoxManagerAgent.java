package myIngrediBox.agents.ingrediBoxManager;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import myIngrediBox.ontologies.IngrediBoxOntology;
import myIngrediBox.shared.behaviours.DFQueryBehaviour;
import myIngrediBox.shared.behaviours.DeregisterServiceBehaviour;
import myIngrediBox.shared.behaviours.PrintRecipeIngredientList;
import myIngrediBox.shared.behaviours.ReadFromFile;

public class IngrediBoxManagerAgent extends Agent {

    private static final long serialVersionUID = 1L;

    @Override
    protected void setup() {
	super.setup();

	this.getContentManager().registerLanguage(new SLCodec());
	this.getContentManager().registerOntology(IngrediBoxOntology.getInstance());

	// Load Recipe
	ReadFromFile loadRecipe = new ReadFromFile("assets/recipes/EierkuchenSpezial.json");
	ParseRecipe parseRecipe = new ParseRecipe();
	PrintRecipeIngredientList printRecipeIngredientBehaviour = new PrintRecipeIngredientList();

	SequentialBehaviour manageRecipe = new SequentialBehaviour();
	loadRecipe.setDataStore(manageRecipe.getDataStore());
	parseRecipe.setDataStore(manageRecipe.getDataStore());
	printRecipeIngredientBehaviour.setDataStore(manageRecipe.getDataStore());

	manageRecipe.addSubBehaviour(loadRecipe);
	manageRecipe.addSubBehaviour(parseRecipe);
	manageRecipe.addSubBehaviour(printRecipeIngredientBehaviour);

	// IMB-IM-Communication

	SequentialBehaviour findInventoryThanCheckAvailability = new SequentialBehaviour();
	findInventoryThanCheckAvailability.setDataStore(manageRecipe.getDataStore());

	DFQueryBehaviour findInventory = new DFQueryBehaviour(this, "Inventory-Managing-Service",
		manageRecipe.getDataStore());

	findInventoryThanCheckAvailability.addSubBehaviour(findInventory);

	// Check availability in inventory
	ACLMessage im = new ACLMessage(ACLMessage.REQUEST);
	im.setConversationId("inventory-request");
	InventoryRequest inventoryRequest = new InventoryRequest(this, im,
		manageRecipe.getDataStore());
	inventoryRequest.setDataStore(manageRecipe.getDataStore());
	findInventoryThanCheckAvailability.addSubBehaviour(inventoryRequest);

	manageRecipe.addSubBehaviour(findInventoryThanCheckAvailability);

	// IMB-IM-Communication end

	// Create shopping list
	CreateShoppingList createShoppingList = new CreateShoppingList();
	createShoppingList.setDataStore(manageRecipe.getDataStore());
	manageRecipe.addSubBehaviour(createShoppingList);

	// IBM-IB-Communication

	SequentialBehaviour findBuyerThanBuy = new SequentialBehaviour();
	findBuyerThanBuy.setDataStore(manageRecipe.getDataStore());
	DFQueryBehaviour findBuyer = new DFQueryBehaviour(this, "Ingredient-Buying-Service",
		manageRecipe.getDataStore());

	ACLMessage m = new ACLMessage(ACLMessage.REQUEST);
	m.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
	m.setConversationId("buyer-request");
	BuyerRequest buy = new BuyerRequest(this, m, manageRecipe.getDataStore());

	findBuyerThanBuy.addSubBehaviour(findBuyer);
	findBuyerThanBuy.addSubBehaviour(buy);

	manageRecipe.addSubBehaviour(findBuyerThanBuy);
	// IBM-IB-Communication end

	this.addBehaviour(new WakerBehaviour(this, 200) {

	    protected void onWake() {
		this.getAgent().addBehaviour(manageRecipe);
	    }

	});
    }

    @Override
    protected void takeDown()

    {
	this.addBehaviour(new DeregisterServiceBehaviour(this));
	super.takeDown();
    }

}