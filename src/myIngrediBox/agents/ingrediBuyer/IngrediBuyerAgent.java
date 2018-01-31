package myIngrediBox.agents.ingrediBuyer;

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
import myIngrediBox.ontologies.Unit;
import myIngrediBox.shared.behaviours.DFQueryBehaviour;
import myIngrediBox.shared.behaviours.RegisterServiceBehaviour;

public class IngrediBuyerAgent extends Agent {

	/**
	 * message language FIPA-SL
	 */
	private Codec codec = new SLCodec();

	/**
	 * ontology used for semantic parsing
	 */
	private Ontology ontology = IngrediBoxOntology.getInstance();

	public Codec getCodec() {
		return codec;
	}

	public void setCodec(Codec codec) {
		this.codec = codec;
	}

	public Ontology getOntology() {
		return ontology;
	}

	public void setOntology(Ontology ontology) {
		this.ontology = ontology;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * list of ingredients to buy
	 */
	private ArrayList<Ingredient> requiredIngredients;

	@Override
	protected void setup() {
		super.setup();
		System.out.println(this.getLocalName() + " started.");

		// Register Service
		RegisterServiceBehaviour registerServiceBehaviour = new RegisterServiceBehaviour(this,
				"Ingredient-Buying-Service");
		this.addBehaviour(registerServiceBehaviour);

		// Adjust ContentManager to ontology and codec
		this.getContentManager().registerLanguage(this.codec);
		this.getContentManager().registerOntology(ontology);

		// wrap main functionality in wakerBehaviour for debugging
		this.addBehaviour(new WakerBehaviour(this, 20000) {
			protected void onWake() {
				buyRequiredIngredients();
			}
		});

	}

	private void buyRequiredIngredients() {
		// add sample list of ingredients to buy
		requiredIngredients = new ArrayList<Ingredient>();
		requiredIngredients.add(new Ingredient("Vanille", 0.2, Unit.Piece));
		requiredIngredients.add(new Ingredient("Apfelkompott", 0.1, Unit.Liter));
		requiredIngredients.add(new Ingredient("Mehl", 0.5, Unit.Kilo));

		/*
		 * first find markets than start trading
		 */
		SequentialBehaviour findMarketsThanBuy = new SequentialBehaviour();

		// find markets
		DFQueryBehaviour findMarkets = new DFQueryBehaviour(this, "Ingredient-Selling-Service");
		findMarketsThanBuy.addSubBehaviour(findMarkets);
		findMarkets.setDataStore(findMarketsThanBuy.getDataStore());

		// start trading with markets to find best buying options
		ACLMessage m = new ACLMessage(ACLMessage.CFP);
		m.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		GoShopping goShopping = new GoShopping(this, m);
		findMarketsThanBuy.addSubBehaviour(goShopping);
		goShopping.setDataStore(findMarketsThanBuy.getDataStore());

		// finally add trading behaviour to agent
		this.addBehaviour(findMarketsThanBuy);

	}

	public ArrayList<Ingredient> getRequiredIngredients() {
		return requiredIngredients;
	}

	public void setRequiredIngredients(ArrayList<Ingredient> requiredIngredients) {
		this.requiredIngredients = requiredIngredients;
	}

}
