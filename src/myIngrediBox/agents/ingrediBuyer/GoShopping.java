package myIngrediBox.agents.ingrediBuyer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREResponder;
import jade.proto.ContractNetInitiator;
import myIngrediBox.ontologies.PurchasableIngredient;
import myIngrediBox.ontologies.RequestOffer;
import myIngrediBox.ontologies.SendPurchase;
import myIngrediBox.ontologies.TradeIngredients;

public class GoShopping extends ContractNetInitiator {

	private IngrediBuyerAgent buyerAgent;
	private BuyingController buyingController;

	public GoShopping(Agent a, ACLMessage cfp) {
		super(a, cfp);
		this.buyerAgent = (IngrediBuyerAgent) a;

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void onStart() {
		super.onStart();
		this.buyingController = BuyingControllerFactory.getInstance()
				.createBuyingControllerFor(BuyingPreference.CHEAPEST);
	}

	@Override
	protected Vector prepareCfps(ACLMessage cfpTemplate) {
		Vector marketsToCall = new Vector();

		if (!this.buyerAgent.getRequiredIngredients().isEmpty()) {
			cfpTemplate.setPerformative(ACLMessage.CFP);
			cfpTemplate.setOntology(this.buyerAgent.getOntology().getName());
			cfpTemplate.setLanguage(this.buyerAgent.getCodec().getName());

			RequestOffer requestOffer = null;
			ACLMessage cfp = null;
			Action a = null;

			ArrayList<AID> markets = (ArrayList<AID>) this.getDataStore().get("Ingredient-Selling-Service");
			for (AID market : markets) {
				requestOffer = new RequestOffer();
				requestOffer.setRequiredIngredients(this.buyerAgent.getRequiredIngredients());
				requestOffer.setBuyer(this.buyerAgent.getAID());

				a = new Action(market, requestOffer);
				cfp = (ACLMessage) cfpTemplate.clone();
				cfp.addReceiver(market);
				try {
					this.myAgent.getContentManager().fillContent(cfp, a);

					marketsToCall.add(cfp);
				} catch (CodecException e) {
					e.printStackTrace();
				} catch (OntologyException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("IngrediBuyer called " + marketsToCall.size() + " Market(s)");
		return marketsToCall;
	}

	@Override
	protected void handlePropose(ACLMessage propose, Vector acceptances) {

		try {
			Action a = (Action) this.myAgent.getContentManager().extractContent(propose);
			TradeIngredients tradeIngredients = (TradeIngredients) a.getAction();

			// this is where to decide what to buy
			ArrayList<PurchasableIngredient> proposedIngredients = tradeIngredients.getIngredients();
			if (!proposedIngredients.isEmpty()) {

				this.buyingController.addOffer(a.getActor(), proposedIngredients);

			}

		} catch (UngroundedException e) {
			e.printStackTrace();
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		} catch (ClassCastException cce) {
			cce.printStackTrace();
		}

	}

	@Override
	protected void handleAllResponses(Vector proposals, Vector acceptances) {
		// All proposals are in.
		// now we buy only ingredients on our optimized shopping list

		HashMap<PurchasableIngredient, AID> shoppingList = this.buyingController.getOptimizedShoppingList();

		for (ACLMessage proposal : (Vector<ACLMessage>) proposals) {
			ACLMessage reply = proposal.createReply();
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);

			ArrayList<PurchasableIngredient> ingredientsToBuy = new ArrayList<PurchasableIngredient>();

			// if market appears on shopping list than add appropriate ingredients to order
			for (PurchasableIngredient ingredientOnList : shoppingList.keySet()) {
				if (shoppingList.get(ingredientOnList).equals(proposal.getSender()))
					ingredientsToBuy.add(ingredientOnList);
			}

			if (!ingredientsToBuy.isEmpty()) {
				try {
					reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);

					TradeIngredients tradeIngredients = new TradeIngredients();
					tradeIngredients.setIngredients(ingredientsToBuy);
					tradeIngredients.setTrader(this.buyerAgent.getAID());

					Action responseAction = new Action(this.getAgent().getAID(), tradeIngredients);

					this.buyerAgent.getContentManager().fillContent(reply, responseAction);
				} catch (CodecException | OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			acceptances.add(reply);
		}

		super.handleAllResponses(proposals, acceptances);
	}

	@Override
	protected void handleInform(ACLMessage inform) {

		// ingredients are bought at this stage
		Action a;
		try {
			a = (Action) this.myAgent.getContentManager().extractContent(inform);
			TradeIngredients tradeIngredients = (TradeIngredients) a.getAction();

			ArrayList<PurchasableIngredient> boughtIngredients = (ArrayList<PurchasableIngredient>) this.getDataStore()
					.get("boughtIngredients");

			if (boughtIngredients == null)
				boughtIngredients = new ArrayList<PurchasableIngredient>();

			boughtIngredients.addAll(tradeIngredients.getIngredients());

			System.out.println("These ingredients are bought from " + inform.getSender().getLocalName() + ":"
					+ tradeIngredients.getIngredients());

			this.getDataStore().put("boughtIngredients", boughtIngredients);

		} catch (UngroundedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void handleAllResultNotifications(Vector resultNotifications) {
		// at this stage the trades have finished.
		// override behaviour of parent AchieveREResponder method ->
		// prepareResultNotification

		DataStore ds = getDataStore();
		AchieveREResponder fsm = (AchieveREResponder) getParent();
		ACLMessage request = (ACLMessage) ds.get(fsm.REQUEST_KEY);
		ACLMessage response = (ACLMessage) ds.get(fsm.RESPONSE_KEY);

		try {

			if (response == null) {
				response = request.createReply();
			}
			response.setPerformative(ACLMessage.INFORM);

			ArrayList<PurchasableIngredient> boughtIngredients = (ArrayList<PurchasableIngredient>) this.getDataStore()
					.get("boughtIngredients");

			SendPurchase sendPurchase = new SendPurchase();
			sendPurchase.setBoughtIngredients(boughtIngredients);

			Action responseAction = new Action(this.getAgent().getAID(), sendPurchase);

			this.myAgent.getContentManager().fillContent(response, responseAction);

		} catch (Exception e) {
			// setPerformativ = Failure, setContent error-message
			try {
				throw new FailureException(response);
			} catch (FailureException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		ds.put(fsm.RESULT_NOTIFICATION_KEY, response);

		super.handleAllResultNotifications(resultNotifications);
	}

}
