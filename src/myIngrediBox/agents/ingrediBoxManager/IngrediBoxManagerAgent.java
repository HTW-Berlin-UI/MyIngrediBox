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
	ArrayList<Ingredient> shoppingList = new ArrayList<>();



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

		this.addBehaviour(new WakerBehaviour(this, 20000) {

			protected void onWake() {
				this.getAgent().addBehaviour(findBuyerThanBuy);
			}

		});

		// IBM-IB-Communication end

		this.addBehaviour(manageRecipe);

		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(ontology);

		DFQueryBehaviour dfQueryBehaviour = new DFQueryBehaviour(this, "Inventory-Managing-Service");

		// for testing normal way would be user-interaction or another system
		this.addBehaviour(new WakerBehaviour(this, 150) {

			private static final long serialVersionUID = 1L;

			protected void onWake() {
				this.myAgent.addBehaviour(dfQueryBehaviour);

				// register adapted AchieveREINitiator Behaviour
				ACLMessage m = new ACLMessage(ACLMessage.REQUEST);
				m.setConversationId("inventory-request");
				m.setContent("Hi, this is an InventoryRequest");

				Ingredient ingredientToRequest = new Ingredient();
				ingredientToRequest = getRecipe().get(0);
				InventoryRequest inventoryRequest = new InventoryRequest(myAgent, m);
				inventoryRequest.setDataStore(dfQueryBehaviour.getDataStore());
				this.myAgent.addBehaviour(inventoryRequest);

			}

		});

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

}