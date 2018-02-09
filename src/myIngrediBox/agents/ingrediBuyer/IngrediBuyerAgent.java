package myIngrediBox.agents.ingrediBuyer;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import myIngrediBox.ontologies.IngrediBoxOntology;
import myIngrediBox.shared.behaviours.DFQueryBehaviour;
import myIngrediBox.shared.behaviours.RegisterServiceBehaviour;

public class IngrediBuyerAgent extends Agent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		super.setup();

		// Register Service
		RegisterServiceBehaviour registerServiceBehaviour = new RegisterServiceBehaviour(this,
				"Ingredient-Buying-Service");
		this.addBehaviour(registerServiceBehaviour);

		// Adjust ContentManager to ontology and codec
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(IngrediBoxOntology.getInstance());

		// initialize behaviour to find markets than start trading
		SequentialBehaviour findMarketsThanBuy = new SequentialBehaviour();

		// find markets
		DFQueryBehaviour findMarkets = new DFQueryBehaviour(this, "Ingredient-Selling-Service",
				findMarketsThanBuy.getDataStore());
		findMarketsThanBuy.addSubBehaviour(findMarkets);

		// contrac net for trading with markets to find best buying options
		ACLMessage m = new ACLMessage(ACLMessage.CFP);
		m.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		GoShopping goShopping = new GoShopping(this, m, findMarketsThanBuy.getDataStore());
		findMarketsThanBuy.addSubBehaviour(goShopping);

		// await ingrediBoxmanager request
		MessageTemplate mt = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt = MessageTemplate.and(MessageTemplate.MatchConversationId("buyer-request"),
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));
		ServeBuyerRequest serveBuyerRequest = new ServeBuyerRequest(this, mt, findMarketsThanBuy.getDataStore());
		// add contractnet as state of request handling
		serveBuyerRequest.registerPrepareResultNotification(findMarketsThanBuy);

		// finally add request handling behaviour to agent
		this.addBehaviour(serveBuyerRequest);

	}

}
