package myIngrediBox.agents.marketManager;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import myIngrediBox.ontologies.IngrediBoxOntology;
import myIngrediBox.shared.behaviours.ReadFromFile;
import myIngrediBox.shared.behaviours.RegisterServiceBehaviour;

public class MarketManager extends Agent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		super.setup();

		// agent local name must match json filename
		String stockFilePath = "assets/stocks/" + this.getLocalName() + ".json";

		// Register Service
		RegisterServiceBehaviour registerServiceBehaviour = new RegisterServiceBehaviour(this,
				"Ingredient-Selling-Service");
		this.addBehaviour(registerServiceBehaviour);

		// Adjust ContentManager to ontology and codec
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(IngrediBoxOntology.getInstance());

		/*
		 * first read json -> than parse stock -> than register service -> than listen
		 * for queries
		 */
		SequentialBehaviour manageStockThanTrade = new SequentialBehaviour();

		// read stock json
		ReadFromFile loadStock = new ReadFromFile(stockFilePath, manageStockThanTrade.getDataStore());
		manageStockThanTrade.addSubBehaviour(loadStock);

		// parse stock
		ParseStock parseStock = new ParseStock(manageStockThanTrade.getDataStore());
		manageStockThanTrade.addSubBehaviour(parseStock);

		// serve buyer request
		MessageTemplate mt = ContractNetResponder
				.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		ServeBuyer serveBuyer = new ServeBuyer(this, mt, manageStockThanTrade.getDataStore());
		manageStockThanTrade.addSubBehaviour(serveBuyer);

		// add stock trading behaviour to agent
		this.addBehaviour(manageStockThanTrade);

	}

}
