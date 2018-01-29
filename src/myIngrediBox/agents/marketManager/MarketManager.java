package myIngrediBox.agents.marketManager;

import java.util.ArrayList;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import myIngrediBox.ontologies.IngrediBoxOntology;
import myIngrediBox.ontologies.PurchasableIngredient;
import myIngrediBox.shared.behaviours.ReadFromFile;
import myIngrediBox.shared.behaviours.RegisterServiceBehaviour;

public class MarketManager extends Agent {
	/**
	 * message language FIPA-SL
	 */
	private Codec codec = new SLCodec();

	/**
	 * ontology used for semantic parsing
	 */
	private Ontology ontology = IngrediBoxOntology.getInstance();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ontology used for semantic parsing
	 */
	private ArrayList<PurchasableIngredient> stock;

	public ArrayList<PurchasableIngredient> getStock() {
		return stock;
	}

	public void setStock(ArrayList<PurchasableIngredient> stock) {
		this.stock = stock;
	}

	@Override
	protected void setup() {
		super.setup();
		System.out.println(this.getLocalName() + " started.");

		// Register Service
		RegisterServiceBehaviour registerServiceBehaviour = new RegisterServiceBehaviour(this,
				"Ingredient-Selling-Service");
		this.addBehaviour(registerServiceBehaviour);

		// Adjust ContentManager to ontology and codec
		this.getContentManager().registerLanguage(this.codec);
		this.getContentManager().registerOntology(ontology);

		/*
		 * first read json -> than parse stock -> than register service -> than listen
		 * for queries
		 */
		SequentialBehaviour manageStockThanTrade = new SequentialBehaviour();

		// read stock json
		// agent local name must match json filename
		ReadFromFile loadStock = new ReadFromFile("assets/stocks/" + this.getLocalName() + ".json");
		manageStockThanTrade.addSubBehaviour(loadStock);
		loadStock.setDataStore(manageStockThanTrade.getDataStore());

		// parse stock
		ParseStock parseStock = new ParseStock();
		manageStockThanTrade.addSubBehaviour(parseStock);
		parseStock.setDataStore(manageStockThanTrade.getDataStore());

		// add stock trading behaviour to agent
		this.addBehaviour(manageStockThanTrade);

	}

}
