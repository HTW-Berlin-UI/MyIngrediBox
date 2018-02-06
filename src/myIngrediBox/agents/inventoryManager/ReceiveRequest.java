package myIngrediBox.agents.inventoryManager;

import java.util.Iterator;

import jade.content.ContentElement;
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
import myIngrediBox.ontologies.IngredientRequestAction;

public class ReceiveRequest extends AchieveREResponder
{
	
	//receive AND responde with sending ingredients ??

	private static final long serialVersionUID = 1L;

	public ReceiveRequest(Agent a, MessageTemplate mt)
	{
		super(a, mt);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException
	{
		ACLMessage response = request.createReply();
		try // valid dateformat?
		{
			ContentElement ce = null;

			// ce will be instance of Action
			ce = this.myAgent.getContentManager().extractContent(request);

			// getContentObject() could receive Java Object, but not recommended cause not
			// FIPA
			// and if Agent not on Java platform
			if (ce instanceof Action)
			{
				Action action = (Action) ce;
				IngredientRequestAction ingredientRequestAction = (IngredientRequestAction) action.getAction();
				//setRequestedIngredients(ingredientRequestAction.getRequiredIngredients());
				Iterator<Ingredient> iterator = ingredientRequestAction.getRequiredIngredients().iterator();

				System.out.println("\nIM received request for: ");
				while (iterator.hasNext())
				{
					Ingredient ingredient = iterator.next();
					System.out.print(ingredient.getName() + "\t");
				}
				System.out.println("\n");
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
	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
			throws FailureException
	{
		if (request.getContent() != null)
		{
			try
			{
				response.setPerformative(ACLMessage.INFORM);
				response.setContent("IngredientRequest received...");
			} catch (Exception e)
			// setPerformativ = Failure, setContent error-message
			{
				throw new FailureException(response);
			}
		}
		return response;
	}

}
