package myIngrediBox.agents.ingrediBoxManager;

import myIngrediBox.shared.behaviours.DFQueryBehaviour;
import java.util.ArrayList;
import java.util.Vector;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;

public class IngrediBoxManagerAgent extends Agent
{

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup()
	{
		super.setup();

		DFQueryBehaviour dfQueryBehaviour = new DFQueryBehaviour(this, "Inventory-Managing-Service");

		// for testing normal way would be user-interaction or another system
		this.addBehaviour(new WakerBehaviour(this, 5000) {

			private static final long serialVersionUID = 1L;

			protected void onWake()
			{
				this.myAgent.addBehaviour(dfQueryBehaviour);

				// register adapted AchieveREINitiator Behaviour
				ACLMessage m = new ACLMessage(ACLMessage.REQUEST);
				m.setContent("Hi, this is an InventoryRequest");

				InventoryRequest inventoryRequest = new InventoryRequest(myAgent, m);
				inventoryRequest.setDataStore(dfQueryBehaviour.getDataStore());

				this.myAgent.addBehaviour(inventoryRequest);
			}

		});

	}

	@Override
	protected void takeDown()

	{
		super.takeDown();
	}

}