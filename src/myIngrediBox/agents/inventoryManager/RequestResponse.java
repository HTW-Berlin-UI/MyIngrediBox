package myIngrediBox.agents.inventoryManager;

import java.util.ArrayList;
import java.util.Iterator;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.IngredientSendingAction;

// Receive ingredient request, handle it and response with sending ingredients
public class RequestResponse extends AchieveREResponder {

    private static final long serialVersionUID = 1L;
  
    public RequestResponse(Agent a, MessageTemplate mt, DataStore store) {
	super(a, mt, store);
    }

    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
	// Receive requested ingredients from IngrediBoxManager and check availability
	// in inventory
	ACLMessage response = request.createReply();
	try // Catch if data format is not correct
	{
	    ContentElement ce = null;

	    // Extract the message content
	    ce = this.myAgent.getContentManager().extractContent(request);

	    if (ce instanceof Action) {
		// Cast message content to Action
		Action action = (Action) ce;
		IngredientSendingAction ingredientRequestAction = (IngredientSendingAction) action.getAction();
		// Extract message content to 'proper' ingredients
		ArrayList<Ingredient> requestedIngredients = ingredientRequestAction.getIngredients();
		if (!requestedIngredients.isEmpty()) {
		    this.getDataStore().put("requestedIngredients", requestedIngredients);
		}

		Iterator<Ingredient> requestIterator = requestedIngredients.iterator();

		System.out.println("\nInventoryManager received request for: ");
		while (requestIterator.hasNext()) {
		    Ingredient ingredient = requestIterator.next();
		    System.out.print(ingredient.getQuantity() + " " + ingredient.getName() + "\t");
		}
		System.out.println("\n");

		CheckAvailability checkAvailability = new CheckAvailability();
		checkAvailability.setDataStore(this.getDataStore());
		this.myAgent.addBehaviour(checkAvailability);
	    }

	} catch (Exception e) {
	    response.setPerformative(ACLMessage.NOT_UNDERSTOOD);
	    response.setContent("Wrong DateFormat");
	    throw new NotUnderstoodException(response);
	}
	response.setPerformative(ACLMessage.AGREE);
	return response;
    }

    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
	// Response to IngrediBoxManager and send ingredients if available
	response = request.createReply();
	
	ArrayList<Ingredient> availableIngredients = (ArrayList<Ingredient>) this.getDataStore().get("availableIngredients");

	if (request.getContent() != null) {
	    if (!availableIngredients.isEmpty()) {

		// Send available ingredients to IBM
		IngredientSendingAction sendAvailableIngredientsAction = new IngredientSendingAction();
		sendAvailableIngredientsAction.setIngredients(availableIngredients);
		sendAvailableIngredientsAction.setAgent(this.getAgent().getAID());

		response.setPerformative(ACLMessage.INFORM);

		try {
		    Action responseAction = new Action(this.getAgent().getAID(), sendAvailableIngredientsAction);
		    this.getAgent().getContentManager().fillContent(response, responseAction);
		} catch (CodecException | OntologyException e) {
		    e.printStackTrace();
		}
	    }

	    else {
		response.setPerformative(ACLMessage.REFUSE);
		response.setContent("Non of the requested ingredients is available :(");
	    }

	}
	return response;
    }

}
