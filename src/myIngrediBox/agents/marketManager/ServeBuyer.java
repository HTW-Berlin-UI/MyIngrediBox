package myIngrediBox.agents.marketManager;

import java.util.ArrayList;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
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

	private MarketManager marketManager;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServeBuyer(Agent a, MessageTemplate mt) {
		super(a, mt);
		this.marketManager = (MarketManager) a;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
		ACLMessage proposal = cfp.createReply();
		try {
			// react to incoming offer
			Action proposalAction = (Action) this.myAgent.getContentManager().extractContent(cfp);
			RequestOffer requestOffer = (RequestOffer) proposalAction.getAction();

			// get the required ingredients list sent by buyer
			ArrayList<Ingredient> requiredIngredients = requestOffer.getRequiredIngredients();

			// assuming that we have an infinite stock, we don't operate on the markets
			// stock object
			ArrayList<PurchasableIngredient> availableIngredients = (ArrayList<PurchasableIngredient>) this.marketManager
					.getStock().clone();

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
				tradeIngredients.setTrader(this.marketManager.getAID());

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
	protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
			throws FailureException {
		ACLMessage response = accept.createReply();

		// now send ingredients back
		// TODO update stock
		response.setPerformative(ACLMessage.INFORM);
		response.setContent(accept.getContent());

		return response;
	}

}
