package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import myIngrediBox.ontologies.IngrediBoxOntology;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.shared.behaviours.DFQueryBehaviour;
import myIngrediBox.shared.behaviours.PrintRecipeIngredientList;
import myIngrediBox.shared.behaviours.ReadFromFile;

public class IngrediBoxManagerAgent extends Agent {

	private static final long serialVersionUID = 1L;

	private ArrayList<Ingredient> recipe;
	private ArrayList<Ingredient> shoppingList = new ArrayList<>();
	private ArrayList<Ingredient> availableIngredientList = new ArrayList<>();


	/**
	 * message language FIPA-SL
	 */
	private Codec codec = new SLCodec();

	/**
	 * ontology used for semantic parsing
	 */
	private Ontology ontology = IngrediBoxOntology.getInstance();

	@Override
	protected void setup() {
		super.setup();

		this.recipe = new ArrayList<Ingredient>();

		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(ontology);

		// Load Recipe
		ReadFromFile loadRecipe = new ReadFromFile("assets/recipes/EierkuchenSpezial.json");
		ParseRecipe parseRecipe = new ParseRecipe();
		PrintRecipeIngredientList printRecipeIngredientBehaviour = new PrintRecipeIngredientList(this.recipe);

		SequentialBehaviour manageRecipe = new SequentialBehaviour();
		loadRecipe.setDataStore(manageRecipe.getDataStore());
		parseRecipe.setDataStore(manageRecipe.getDataStore());

		manageRecipe.addSubBehaviour(loadRecipe);
		manageRecipe.addSubBehaviour(parseRecipe);
		manageRecipe.addSubBehaviour(printRecipeIngredientBehaviour);

		// IMB-IM-Communication

		SequentialBehaviour findInventoryThanCheckAvailability = new SequentialBehaviour();
		DFQueryBehaviour findInventory = new DFQueryBehaviour(this, "Inventory-Managing-Service",
				findInventoryThanCheckAvailability.getDataStore());

		findInventoryThanCheckAvailability.addSubBehaviour(findInventory);

		// register adapted AchieveREINitiator Behaviour
		ACLMessage im = new ACLMessage(ACLMessage.REQUEST);
		im.setConversationId("inventory-request");
		InventoryRequest inventoryRequest = new InventoryRequest(this, im);
		inventoryRequest.setDataStore(findInventoryThanCheckAvailability.getDataStore());
		findInventoryThanCheckAvailability.addSubBehaviour(inventoryRequest);

		findInventoryThanCheckAvailability.setDataStore(manageRecipe.getDataStore());
		manageRecipe.addSubBehaviour(findInventoryThanCheckAvailability);

		// IMB-IM-Communication end
		
		// Create shopping list 
		CreateShoppingList createShoppingList = new CreateShoppingList(this);
		manageRecipe.addSubBehaviour(createShoppingList);
		
		// IBM-IB-Communication

		SequentialBehaviour findBuyerThanBuy = new SequentialBehaviour();
		DFQueryBehaviour findBuyer = new DFQueryBehaviour(this, "Ingredient-Buying-Service",
				findBuyerThanBuy.getDataStore());

		ACLMessage m = new ACLMessage(ACLMessage.REQUEST);
		m.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		m.setConversationId("buyer-request");
		BuyerRequest buy = new BuyerRequest(this, m, findBuyerThanBuy.getDataStore());

		findBuyerThanBuy.addSubBehaviour(findBuyer);
		findBuyerThanBuy.addSubBehaviour(buy);

		findBuyerThanBuy.setDataStore(manageRecipe.getDataStore());
		manageRecipe.addSubBehaviour(findBuyerThanBuy);
		// IBM-IB-Communication end

		this.addBehaviour(new WakerBehaviour(this, 200) {

			protected void onWake() {
				this.getAgent().addBehaviour(manageRecipe);
			}

		});

		// TODO send Ingredis as ArrayList!! (like in RequestOffer Class and GoShoppin
		// Class)
		// oder inventoryRequest.setIngredientToRequest(itreq);...bauen

	}

	@Override
	protected void takeDown()

	{
		super.takeDown();
	}

	public void setRecipe(ArrayList<Ingredient> recipe) {
		this.recipe = recipe;
	}

	public ArrayList<Ingredient> getRecipe() {
		return recipe;
	}

	public Ontology getOntology() {
		return ontology;
	}

	public void setOntology(Ontology ontology) {
		this.ontology = ontology;
	}

	public Codec getCodec() {
		return codec;
	}

	public void setCodec(Codec codec) {
		this.codec = codec;
	}
	
	public ArrayList<Ingredient> getShoppingList()
	{
		return shoppingList;
	}

	public void setShoppingList(ArrayList<Ingredient> shoppingList)
	{
		this.shoppingList = shoppingList;
	}

	public ArrayList<Ingredient> getAvailableIngredientList()
	{
		return availableIngredientList;
	}

	public void setAvailableIngredientList(ArrayList<Ingredient> availableIngredientList)
	{
		this.availableIngredientList = availableIngredientList;
	}

}