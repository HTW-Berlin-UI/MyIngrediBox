package myIngrediBox.agents.ingrediBuyer;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class ServeBuyerRequest extends AchieveREResponder {

	public ServeBuyerRequest(Agent a, MessageTemplate mt, DataStore store) {
		super(a, mt, store);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
		ACLMessage response = request.createReply();
		try { // test: content is valid dateformat + ...
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

}
