package myIngrediBox.agents.ingrediBuyer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
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
import myIngrediBox.ontologies.BuyingPreference;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.PurchasableIngredient;
import myIngrediBox.ontologies.Purchase;
import myIngrediBox.ontologies.RequestOffer;
import myIngrediBox.ontologies.SendPurchases;
import myIngrediBox.ontologies.TradeIngredients;

public class GoShopping extends ContractNetInitiator {

	private BuyingController buyingController;

	public GoShopping(Agent a, ACLMessage cfp, DataStore datastore) {
		super(a, cfp);
		this.setDataStore(datastore);

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void onStart() {
		super.onStart();

		BuyingPreference preference = (BuyingPreference) this.getDataStore().get("buyingPreference");

		this.buyingController = BuyingControllerFactory.getInstance().createBuyingControllerFor(preference);
	}

	@Override
	protected Vector prepareCfps(ACLMessage cfpTemplate) {
		Vector marketsToCall = new Vector();

		ArrayList<Ingredient> requiredIngredients = (ArrayList<Ingredient>) this.getDataStore()
				.get("requiredIngredients");

		if (!requiredIngredients.isEmpty()) {

			String[] ontologies = this.getAgent().getContentManager().getOntologyNames();
			String[] languages = this.getAgent().getContentManager().getLanguageNames();

			if (ontologies[0] != null)
				cfpTemplate.setOntology(ontologies[0]);
			if (languages[0] != null)
				cfpTemplate.setLanguage(languages[0]);

			RequestOffer requestOffer = null;
			ACLMessage cfp = null;
			Action a = null;

			ArrayList<AID> markets = (ArrayList<AID>) this.getDataStore().get("Ingredient-Selling-Service");
			for (AID market : markets) {
				requestOffer = new RequestOffer();
				requestOffer.setRequiredIngredients(requiredIngredients);
				requestOffer.setBuyer(this.getAgent().getAID());

				a = new Action(market, requestOffer);
				cfp = (ACLMessage) cfpTemplate.clone();
				cfp.addReceiver(market);
				try {
					this.getAgent().getContentManager().fillContent(cfp, a);

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

			// inform buying controller about this offer
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
			Set<PurchasableIngredient> ingredientsOnList = shoppingList.keySet();
			for (PurchasableIngredient ingredientOnList : ingredientsOnList) {
				if (shoppingList.get(ingredientOnList).equals(proposal.getSender()))
					ingredientsToBuy.add(ingredientOnList);
			}

			if (!ingredientsToBuy.isEmpty()) {
				try {
					reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);

					TradeIngredients tradeIngredients = new TradeIngredients();
					tradeIngredients.setIngredients(ingredientsToBuy);
					tradeIngredients.setTrader(this.getAgent().getAID());

					Action responseAction = new Action(this.getAgent().getAID(), tradeIngredients);

					this.getAgent().getContentManager().fillContent(reply, responseAction);
				} catch (CodecException | OntologyException e) {

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

			ArrayList<Purchase> purchases = (ArrayList<Purchase>) this.getDataStore().get("purchases");

			if (purchases == null)
				purchases = new ArrayList<Purchase>();

			purchases.add(new Purchase(inform.getSender().getLocalName(), tradeIngredients.getIngredients()));

			System.out.println("These ingredients are bought from " + inform.getSender().getLocalName() + ":"
					+ tradeIngredients.getIngredients());

			this.getDataStore().put("purchases", purchases);

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
		// at this stage the trading has finished.
		// override behaviour of parent AchieveREResponder method ->
		// prepareResultNotification

		DataStore ds = getDataStore();
		// assuming that serveBuyerRequest behaviour is our root behaviour here
		AchieveREResponder fsm = (AchieveREResponder) root();
		ACLMessage request = (ACLMessage) ds.get(fsm.REQUEST_KEY);
		ACLMessage response = (ACLMessage) ds.get(fsm.RESPONSE_KEY);

		try {

			if (response == null) {
				response = request.createReply();
			}
			response.setPerformative(ACLMessage.INFORM);

			ArrayList<Purchase> purchases = (ArrayList<Purchase>) this.getDataStore().get("purchases");

			SendPurchases sendPurchases = new SendPurchases();

			sendPurchases.setPurchases(purchases);

			Action responseAction = new Action(this.getAgent().getAID(), sendPurchases);

			this.getAgent().getContentManager().fillContent(response, responseAction);

		} catch (Exception e) {
			try {
				response.setPerformative(ACLMessage.FAILURE);
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
