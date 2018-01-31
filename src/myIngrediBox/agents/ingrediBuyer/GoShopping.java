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
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import myIngrediBox.ontologies.PurchasableIngredient;
import myIngrediBox.ontologies.RequestOffer;
import myIngrediBox.ontologies.TradeIngredients;

public class GoShopping extends ContractNetInitiator {

	private IngrediBuyerAgent buyerAgent;
	private HashMap<PurchasableIngredient, AID> shoppingList;

	public GoShopping(Agent a, ACLMessage cfp) {
		super(a, cfp);
		this.buyerAgent = (IngrediBuyerAgent) a;
		this.shoppingList = new HashMap<PurchasableIngredient, AID>();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	protected Vector prepareCfps(ACLMessage cfp) {
		Vector v = new Vector();

		if (!this.buyerAgent.getRequiredIngredients().isEmpty()) {
			cfp.setPerformative(ACLMessage.CFP);
			cfp.setOntology(this.buyerAgent.getOntology().getName());
			cfp.setLanguage(this.buyerAgent.getCodec().getName());

			RequestOffer requestOffer = null;
			ACLMessage cfpX = null;
			Action a = null;

			ArrayList<AID> markets = (ArrayList<AID>) this.getDataStore().get("Ingredient-Selling-Service");
			for (AID market : markets) {
				requestOffer = new RequestOffer();
				requestOffer.setRequiredIngredients(this.buyerAgent.getRequiredIngredients());
				requestOffer.setBuyer(this.buyerAgent.getAID());

				a = new Action(market, requestOffer);

				cfpX = (ACLMessage) cfp.clone();
				cfpX.addReceiver(market);
				try {
					this.myAgent.getContentManager().fillContent(cfpX, a);
					System.out.println("Call for Proposal: " + cfpX);
					v.add(cfpX);
				} catch (CodecException e) {
					e.printStackTrace();
				} catch (OntologyException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("IngrediBuyer:  CFP prepared v.size:" + v.size());
		return v;
	}

	@Override
	protected void handlePropose(ACLMessage propose, Vector acceptances) {

		try {
			Action a = (Action) this.myAgent.getContentManager().extractContent(propose);
			TradeIngredients tradeIngredients = (TradeIngredients) a.getAction();

			// this is where to decide what to buy
			ArrayList<PurchasableIngredient> proposedIngredients = tradeIngredients.getIngredients();
			if (!proposedIngredients.isEmpty()) {

				for (PurchasableIngredient ingredient : proposedIngredients) {

					// if proposed ingredient is served by another market and is cheaper than the
					// current one
					if (this.shoppingList.containsKey(ingredient)) {

						// find the comparable ingredient on shopping list
						for (PurchasableIngredient ingredientOnList : this.shoppingList.keySet()) {
							if (ingredientOnList.equals(ingredient)) {

								// now decide whether to replace the item on the list with new ingredient
								if (ingredient.getPrice() < ingredientOnList.getPrice()) {
									this.shoppingList.remove(ingredientOnList);
									this.shoppingList.put(ingredient, a.getActor());
								}

							}

						}

					} else {
						// if its a new ingredient to our list
						this.shoppingList.put(ingredient, a.getActor());
					}

				}
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
		for (ACLMessage proposal : (Vector<ACLMessage>) proposals) {
			ACLMessage reply = proposal.createReply();
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);

			ArrayList<PurchasableIngredient> ingredientsToBuy = new ArrayList<PurchasableIngredient>();

			// if market appears on shopping list than add appropriate ingredients to order
			for (PurchasableIngredient ingredientOnList : this.shoppingList.keySet()) {
				if (this.shoppingList.get(ingredientOnList).equals(proposal.getSender()))
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
	protected void handleRefuse(ACLMessage refuse) {
		// TODO Auto-generated method stub
		super.handleRefuse(refuse);
	}

	@Override
	protected void handleInform(ACLMessage inform) {

		// ingredients are bought at this stage
		Action a;
		try {
			a = (Action) this.myAgent.getContentManager().extractContent(inform);
			TradeIngredients tradeIngredients = (TradeIngredients) a.getAction();

			// TODO now send them back to IBM

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

}
