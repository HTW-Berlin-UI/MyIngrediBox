package myIngrediBox.agents.ingrediBuyer;

import java.util.ArrayList;
import java.util.Vector;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import myIngrediBox.ontologies.RequestOffer;
import myIngrediBox.ontologies.TradeIngredients;

public class GoShopping extends ContractNetInitiator {

	private IngrediBuyerAgent buyerAgent;

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
		ACLMessage reply = propose.createReply();
		reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
		try {
			Action a = (Action) this.myAgent.getContentManager().extractContent(propose);
			TradeIngredients tradeIngredients = (TradeIngredients) a.getAction();

			// if (sb.getPrice() >= 0 && (this.getDataStore().get("bestOffer") == null
			// || ((SellBook) this.getDataStore().get("bestOffer")).getPrice() >
			// sb.getPrice()))
			if (!tradeIngredients.getIngredients().isEmpty()) {
				for (ACLMessage prop : (Vector<ACLMessage>) acceptances) {
					prop.setPerformative(ACLMessage.REJECT_PROPOSAL);
				}
				reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
				this.getDataStore().put("boughtIngredients", tradeIngredients.getIngredients());
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

		acceptances.add(reply);
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
