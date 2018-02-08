package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;
import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class BuyerRequest extends AchieveREInitiator {

	public BuyerRequest(Agent a, ACLMessage msg, DataStore store) {
		super(a, msg, store);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Vector prepareRequests(ACLMessage request) {
		Vector v = new Vector();

		// find InventoryManager(s)
		ArrayList<AID> buyerAgents = (ArrayList<AID>) this.getDataStore().get("Ingredient-Buying-Service");

		System.out.println("asdaklsjdlkas " + buyerAgents.get(0).getLocalName());

		request.addReceiver(buyerAgents.get(0));

		v.add(request);

		return v;
	}

	/**
	 * Responder role agreed => this method is automatically called
	 */
	@Override
	protected void handleAgree(ACLMessage agree) {
		// System.out.println("\nAgreed: " + agree);
	}

	/**
	 * Responder role not only agreed, but now says the request effect is realized
	 * => this method is automatically called
	 */
	@Override
	protected void handleInform(ACLMessage inform) {
		// System.out.println("\nSome Inform: " + inform);
	}

}
