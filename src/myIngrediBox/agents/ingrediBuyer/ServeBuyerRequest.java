package myIngrediBox.agents.ingrediBuyer;

import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import myIngrediBox.ontologies.RequestBuyingAction;

public class ServeBuyerRequest extends AchieveREResponder {

	public ServeBuyerRequest(Agent a, MessageTemplate mt, DataStore store) {
		super(a, mt, store);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
		ACLMessage response = request.createReply();
		try {

			Action a = (Action) this.myAgent.getContentManager().extractContent(request);
			RequestBuyingAction requestBuyingAction = (RequestBuyingAction) a.getAction();

			this.getDataStore().put("requiredIngredients", requestBuyingAction.getRequiredIngredients());

		} catch (Exception e) {
			response.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			throw new NotUnderstoodException(response);
		}

		response.setPerformative(ACLMessage.AGREE);
		return response;
	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {

		try {

			if (response == null) {
				response = request.createReply();
			}
			response.setPerformative(ACLMessage.INFORM);

		} catch (Exception e) {
			// setPerformativ = Failure, setContent error-message
			throw new FailureException(response);
		}

		return response;
	}

	@Override
	public void registerPrepareResultNotification(Behaviour b) {
		// TODO Auto-generated method stub
		super.registerPrepareResultNotification(b);
	}

}
