package myIngrediBox.agents.inventoryManager;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class ReceiveRequest extends AchieveREResponder
{

	private static final long serialVersionUID = 1L;

	public ReceiveRequest(Agent a, MessageTemplate mt, DataStore store)
	{
		super(a, mt, store);
		// TODO Auto-generated constructor stub
	}

}
