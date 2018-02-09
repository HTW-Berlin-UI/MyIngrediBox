package myIngrediBox.agents.marketManager;

import java.util.ArrayList;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.PurchasableIngredient;
import myIngrediBox.ontologies.RequestOffer;
import myIngrediBox.ontologies.TradeIngredients;

public class ServeBuyer extends ContractNetResponder {

	private static final long serialVersionUID = 1L;

	public ServeBuyer(Agent a, MessageTemplate mt, DataStore datastore) {
		super(a, mt);
		this.setDataStore(datastore);
	}

	@Override
	protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
		ACLMessage proposal = cfp.createReply();
		try {
			// react to incoming offer
			Action proposalAction = (Action) this.getAgent().getContentManager().extractContent(cfp);
			RequestOffer requestOffer = (RequestOffer) proposalAction.getAction();

			// get the required ingredients list sent by buyer
			ArrayList<Ingredient> requiredIngredients = requestOffer.getRequiredIngredients();

			// get stock
			ArrayList<PurchasableIngredient> stock = (ArrayList<PurchasableIngredient>) this.getDataStore()
					.get("stock");

			// assuming that our stock is infinite, we don't operate on its object reference
			ArrayList<PurchasableIngredient> availableIngredients = (ArrayList<PurchasableIngredient>) stock.clone();

			// check if the required ingredients are in stock
			availableIngredients.retainAll(requiredIngredients);

			// remove ingredients if quantity is insufficient
			availableIngredients.removeIf(
					availableIngredient -> requiredIngredients.get(requiredIngredients.indexOf(availableIngredient))
							.getQuantity() > availableIngredient.getQuantity());

			if (availableIngredients.isEmpty()) {
				// no fitting ingredients in stock
				proposal.setPerformative(ACLMessage.REFUSE);
			} else {
				// we got some ingredients to offer

				proposal.setPerformative(ACLMessage.PROPOSE);

				TradeIngredients tradeIngredients = new TradeIngredients();
				tradeIngredients.setIngredients(availableIngredients);
				tradeIngredients.setTrader(this.getAgent().getAID());

				Action responseAction = new Action(this.getAgent().getAID(), tradeIngredients);

				this.myAgent.getContentManager().fillContent(proposal, responseAction);

			}

		} catch (UngroundedException e) {
			e.printStackTrace();
			proposal.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			throw new NotUnderstoodException(proposal);
		} catch (CodecException e) {
			e.printStackTrace();
			proposal.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			throw new NotUnderstoodException(proposal);
		} catch (OntologyException e) {
			e.printStackTrace();
			proposal.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			throw new NotUnderstoodException(proposal);
		}

		// return proposal or refuse; NOT-Understood leaves by Exception
		return proposal;

	}

	/**
	 * Ingredibuyer wants to buy some ingredients
	 */
	@Override
	protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
		ACLMessage response = accept.createReply();
		try {

			// now send ingredients back
			// TODO update stock
			response.setPerformative(ACLMessage.INFORM);
			response.setContent(accept.getContent());

		} catch (Exception e) {
			try {
				response.setPerformative(ACLMessage.FAILURE);
				throw new FailureException(response);
			} catch (FailureException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return response;
	}

}
