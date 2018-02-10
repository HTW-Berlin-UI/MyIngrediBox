package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;
import java.util.Vector;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import myIngrediBox.ontologies.BuyingPreference;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.PurchasableIngredient;
import myIngrediBox.ontologies.RequestBuyingAction;
import myIngrediBox.ontologies.SendPurchase;
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

	// find IngrediBuyer(s)
	ArrayList<AID> buyerAgents = (ArrayList<AID>) this.getDataStore().get("Ingredient-Buying-Service");
	AID buyerAgent = buyerAgents.get(0);

	String[] ontologies = this.getAgent().getContentManager().getOntologyNames();
	String[] languages = this.getAgent().getContentManager().getLanguageNames();

	if (ontologies[0] != null)
	    request.setOntology(ontologies[0]);
	if (languages[0] != null)
	    request.setLanguage(languages[0]);

	RequestBuyingAction requestBuyingAction = new RequestBuyingAction();

	if (!ibm.getShoppingList().isEmpty()) {
	    requestBuyingAction.setRequiredIngredients(ibm.getShoppingList());
	} else {
	    // Set sample data
	    requestBuyingAction.setRequiredIngredients(requiredIngredients);
	}

	// set buying preference
	requestBuyingAction.setPreference(BuyingPreference.LOW_LEFTOVERS);

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
	// ingredients are bought at this stage
	Action a;
	try {
	    a = (Action) this.myAgent.getContentManager().extractContent(inform);
	    SendPurchase sendPurchase = (SendPurchase) a.getAction();
	    ArrayList<PurchasableIngredient> boughtIngredients = sendPurchase.getBoughtIngredients();

	    System.out.println(
		    "These ingredients are bought from " + inform.getSender().getLocalName() + ":" + boughtIngredients);

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

}
