package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import myIngrediBox.gui.MyIngrediBoxGUI;
import myIngrediBox.ontologies.BuyingPreference;
import myIngrediBox.ontologies.IngrediBoxOntology;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.shared.behaviours.DFQueryBehaviour;
import myIngrediBox.shared.behaviours.DeregisterServiceBehaviour;
import myIngrediBox.shared.behaviours.PrintRecipeIngredientList;
import myIngrediBox.shared.behaviours.ReadFromFile;

public class IngrediBoxManagerAgent extends Agent {

	private static final long serialVersionUID = 1L;
	private MyIngrediBoxGUI gui;

	@Override
	protected void setup() {
		super.setup();

		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(IngrediBoxOntology.getInstance());

		// Initialize GUI
		IngrediBoxManagerAgent agentReference = this;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gui = new MyIngrediBoxGUI((agentReference));

			}
		});

	}

	public void proceed(ArrayList<Ingredient> recipe, BuyingPreference preference) {

		// Initialize behaviour to manage recipe
		SequentialBehaviour manageRecipe = new SequentialBehaviour();
		manageRecipe.getDataStore().put("recipe", recipe);
		manageRecipe.getDataStore().put("preference", preference);

		// IMB-IM-Communication

		SequentialBehaviour findInventoryThanCheckAvailability = new SequentialBehaviour();
		findInventoryThanCheckAvailability.setDataStore(manageRecipe.getDataStore());

		DFQueryBehaviour findInventory = new DFQueryBehaviour(this, "Inventory-Managing-Service",
				manageRecipe.getDataStore());

		findInventoryThanCheckAvailability.addSubBehaviour(findInventory);

		// Check availability in inventory
		ACLMessage im = new ACLMessage(ACLMessage.REQUEST);
		im.setConversationId("inventory-request");
		InventoryRequest inventoryRequest = new InventoryRequest(this, im, manageRecipe.getDataStore());
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

		// Calculate leftovers and send them to inventory
		CalculateLeftovers calculateLeftovers = new CalculateLeftovers(manageRecipe.getDataStore());
		manageRecipe.addSubBehaviour(calculateLeftovers);
		ACLMessage leftoversMessage = new ACLMessage(ACLMessage.PROPOSE);
		leftoversMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
		LeftoversPropose leftOversPropose = new LeftoversPropose(this, leftoversMessage, manageRecipe.getDataStore());
		manageRecipe.addSubBehaviour(leftOversPropose);

		// Update GUI
		UpdateGUI updateGUI = new UpdateGUI(gui, GUIUpdateType.RESULT, manageRecipe.getDataStore());
		manageRecipe.addSubBehaviour(updateGUI);

		this.addBehaviour(manageRecipe);
	}

	public void getSampleData() {

		// Load Recipe
		SequentialBehaviour getSampleData = new SequentialBehaviour();
		ReadFromFile loadRecipe = new ReadFromFile("assets/recipes/EierkuchenSpezial.json");
		ParseRecipe parseRecipe = new ParseRecipe();
		PrintRecipeIngredientList printRecipeIngredientBehaviour = new PrintRecipeIngredientList();
		UpdateGUI updateGUI = new UpdateGUI(gui, GUIUpdateType.SAMPLE_DATA, getSampleData.getDataStore());

		loadRecipe.setDataStore(getSampleData.getDataStore());
		parseRecipe.setDataStore(getSampleData.getDataStore());
		printRecipeIngredientBehaviour.setDataStore(getSampleData.getDataStore());

		getSampleData.addSubBehaviour(loadRecipe);
		getSampleData.addSubBehaviour(parseRecipe);
		getSampleData.addSubBehaviour(printRecipeIngredientBehaviour);
		getSampleData.addSubBehaviour(updateGUI);

		this.addBehaviour(getSampleData);

	}

	@Override
	protected void takeDown()

	{
		this.addBehaviour(new DeregisterServiceBehaviour(this));
		super.takeDown();
	}

}