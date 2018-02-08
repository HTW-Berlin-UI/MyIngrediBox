package myIngrediBox.agents.inventoryManager;

import java.util.ArrayList;
import java.util.Iterator;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.IngredientSendingAction;

public class RequestResponse extends AchieveREResponder
{

	// receive AND responde with sending ingredients ??

	private static final long serialVersionUID = 1L;
	private InventoryManagerAgent inventoryManagerAgent;

	public RequestResponse(Agent a, MessageTemplate mt)
	{
		super(a, mt);
		this.inventoryManagerAgent = (InventoryManagerAgent) a;
	}

	@Override
	protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException
	{
		ACLMessage response = request.createReply();
		try // valid dateformat?
		{
			ContentElement ce = null;

			// ce will be instance of Action
			ce = inventoryManagerAgent.getContentManager().extractContent(request);

			// getContentObject() could receive Java Object, but not recommended cause not
			// FIPA
			// and if Agent not on Java platform
			if (ce instanceof Action)
			{
				Action action = (Action) ce;
				IngredientSendingAction ingredientRequestAction = (IngredientSendingAction) action.getAction();
				inventoryManagerAgent.setRequestedIngredients(ingredientRequestAction.getIngredients());
				Iterator<Ingredient> iterator = ingredientRequestAction.getIngredients().iterator();

				System.out.println("\nIM received request for: ");
				while (iterator.hasNext())
				{
					Ingredient ingredient = iterator.next();
					System.out.print(ingredient.getQuantity() + " " + ingredient.getName() + "\t");
				}
				System.out.println("\n");

				CheckAvailability checkAvailability = new CheckAvailability(myAgent);
				this.myAgent.addBehaviour(checkAvailability);
			}

		} catch (Exception e)
		{
			response.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			response.setContent("Wrong DateFormat");
			throw new NotUnderstoodException(response);
		}
		response.setPerformative(ACLMessage.AGREE);
		return response;
	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException
	{
		//for testing refuse-case
	//	inventoryManagerAgent.getAvailableRequestedIngredients().clear();
		if (request.getContent() != null)
		{
			if (!inventoryManagerAgent.getAvailableRequestedIngredients().isEmpty())
			{

				// Send available ingredients to IBM
				IngredientSendingAction sendAvailableIngredientsAction = new IngredientSendingAction();
				sendAvailableIngredientsAction
						.setIngredients(inventoryManagerAgent.getAvailableRequestedIngredients());
				sendAvailableIngredientsAction.setAgent(inventoryManagerAgent.getAID());

				//request.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
				response.setPerformative(ACLMessage.INFORM);
				response.setOntology(inventoryManagerAgent.getOntology().getName());
				response.setLanguage(inventoryManagerAgent.getCodec().getName());

				try
				{
					this.getAgent().getContentManager().fillContent(response, sendAvailableIngredientsAction);
				} catch (CodecException | OntologyException e)
				{
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
