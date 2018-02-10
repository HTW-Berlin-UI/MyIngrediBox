package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import jade.content.ContentElement;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import myIngrediBox.agents.inventoryManager.CheckAvailability;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.IngredientSendingAction;
import myIngrediBox.ontologies.RequestOffer;

public class InventoryRequest extends AchieveREInitiator {

    ArrayList<Ingredient> requestedIngredientList;
    ArrayList<Ingredient> availableIngredientList;
    ArrayList<Ingredient> shoppingList;

    private IngrediBoxManagerAgent ingrediBoxManagerAgent;

    private static final long serialVersionUID = 1L;

    public InventoryRequest(Agent a, ACLMessage msg) {
	super(a, msg);
	this.ingrediBoxManagerAgent = (IngrediBoxManagerAgent) a;
    }

    /**
     * prepares the ACLMessage(s) for Requesting Ingredients
     */
    @Override
    protected Vector prepareRequests(ACLMessage request) {
	Vector v = new Vector();

	this.requestedIngredientList = ingrediBoxManagerAgent.getRecipe();

	if (!this.requestedIngredientList.isEmpty()) {
	    // Request the ingredients from the recipe at InventoryManager
	    IngredientSendingAction ingredientRequestAction = new IngredientSendingAction();
	    ingredientRequestAction.setIngredients(requestedIngredientList);
	    ingredientRequestAction.setAgent(this.getAgent().getAID());

	    // find InventoryManager(s)
	    ArrayList<AID> inventoryManagers = (ArrayList<AID>) this.getDataStore().get("Inventory-Managing-Service");

	    Action requestIngredientsAction = new Action(inventoryManagers.get(0), ingredientRequestAction);

	    String[] ontologies = this.getAgent().getContentManager().getOntologyNames();
	    String[] languages = this.getAgent().getContentManager().getLanguageNames();

	    if (ontologies[0] != null)
		request.setOntology(ontologies[0]);
	    if (languages[0] != null)
		request.setLanguage(languages[0]);

	    request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
	    request.addReceiver(inventoryManagers.get(0));

	    try {
		this.getAgent().getContentManager().fillContent(request, requestIngredientsAction);
	    } catch (CodecException | OntologyException e) {
		e.printStackTrace();
	    }

	    v.add(request);
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

	ContentElement ce = null;

	// ce will be instance of Action
	try {

	    ce = this.myAgent.getContentManager().extractContent(inform);

	    // and if Agent not on Java platform
	    if (ce instanceof Action) {
		Action action = (Action) ce;
		IngredientSendingAction availableIngredientReceivingAction = (IngredientSendingAction) action
			.getAction();
		ingrediBoxManagerAgent.setAvailableIngredientList(availableIngredientReceivingAction.getIngredients());

		availableIngredientList = availableIngredientReceivingAction.getIngredients();

		// share availableIngredientList with DataStore
		this.getDataStore().put("availableIngredientList", availableIngredientList);

	    }
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

    /**
     * Responder doesnt want to play with us
     */
    @Override
    protected void handleRefuse(ACLMessage refuse) {
	System.out.println(refuse);

	ContentElement ce = null;
	try {
	    ce = this.myAgent.getContentManager().extractContent(refuse);
	} catch (UngroundedException e) {
	    e.printStackTrace();
	} catch (CodecException e) {
	    e.printStackTrace();
	} catch (OntologyException e) {
	    e.printStackTrace();
	}

	if (ce != null) {
	    try {
		// NOT in combination with our Predicate InCatalogue tells us
		// LibAgent does not know book => in RL stop querying
		AbsPredicate ap = (AbsPredicate) ce;
		if (ap.getTypeName().equalsIgnoreCase(SLVocabulary.NOT)) {

		    // try
		    // {
		    // InCatalogue ic = (InCatalogue) ontology.toObject( ap.getAbsObject(
		    // SLVocabulary.NOT_WHAT ) );
		    // System.out.println( "UserAgent: "+ ic.getCatalogueAgent().getLocalName()
		    // +" does not know: " + ic.getBook().getTitel() );
		    // System.out.println("TryBlock in InventoryRequest line 170 called");
		    // }
		    // catch (UngroundedException e)
		    // {
		    // e.printStackTrace();
		    // }
		    // catch (OntologyException e)
		    // {
		    // e.printStackTrace();
		    // }

		}

	    } catch (ClassCastException cce2) {
		System.out.println("\nRefuse not understood: " + ce);
	    }

	} else {
	    System.out.println("\nRefuse with empty Content: " + refuse);
	}

    }
}