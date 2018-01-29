package myIngrediBox.agents.marketManager;

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

			// TODO collect fitting item and send them back
			proposal.setPerformative(ACLMessage.PROPOSE);

			TradeIngredients tradeIngredients = new TradeIngredients();
			tradeIngredients.setIngredients(this.marketManager.getStock());
			tradeIngredients.setTrader(this.marketManager.getAID());

			Action responseAction = new Action(requestOffer.getBuyer(), tradeIngredients);

			this.myAgent.getContentManager().fillContent(proposal, responseAction);

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

		System.out.println("BSAgent.handleCFP prop: " + proposal);
		// return proposal or refuse; NOT-Understood leaves by Exception
		return proposal;

	}

	/**
	 * the LibAgent wants to buy our too expensive old books, jippi
	 */
	@Override
	protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
			throws FailureException {
		ACLMessage response = accept.createReply();

		// check whether price and infos are equal in prop&accept
		// send the book to the library (RealLive)

		response.setPerformative(ACLMessage.INFORM);
		response.setContent(propose.getContent());

		System.out.println("BSA#handleAcceptProposal accept:" + response);

		return response;
	}

	/**
	 * price to high? no deal today
	 */
	@Override
	protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
		// TODO Auto-generated method stub
		super.handleRejectProposal(cfp, propose, reject);
	}

}
