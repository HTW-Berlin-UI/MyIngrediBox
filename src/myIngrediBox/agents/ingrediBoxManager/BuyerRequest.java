package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;
import java.util.Vector;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.RequestBuyingAction;
import myIngrediBox.ontologies.Unit;

public class BuyerRequest extends AchieveREInitiator {

	private IngrediBoxManagerAgent ibm;

	public BuyerRequest(Agent a, ACLMessage msg, DataStore store) {
		super(a, msg, store);
		this.ibm = (IngrediBoxManagerAgent) a;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Vector prepareRequests(ACLMessage request) {
		Vector v = new Vector();

		// sample data
		ArrayList<Ingredient> requiredIngredients = new ArrayList<Ingredient>();
		requiredIngredients.add(new Ingredient("Vanille", 1, Unit.Piece));
		requiredIngredients.add(new Ingredient("Apfelkompott", 0.1, Unit.Liter));
		requiredIngredients.add(new Ingredient("Mehl", 0.5, Unit.Kilo));

		// find InventoryManager(s)
		ArrayList<AID> buyerAgents = (ArrayList<AID>) this.getDataStore().get("Ingredient-Buying-Service");
		AID buyerAgent = buyerAgents.get(0);

		request.setOntology(this.ibm.getOntology().getName());
		request.setLanguage(this.ibm.getCodec().getName());
		RequestBuyingAction requestBuyingAction = new RequestBuyingAction();

		requestBuyingAction.setRequiredIngredients(requiredIngredients);
		Action a = new Action(buyerAgent, requestBuyingAction);

		request.addReceiver(buyerAgent);

		try {
			this.getAgent().getContentManager().fillContent(request, a);
			v.add(request);
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}

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
		System.out.println("-> ibm hat antwort von buyer");
	}

}
