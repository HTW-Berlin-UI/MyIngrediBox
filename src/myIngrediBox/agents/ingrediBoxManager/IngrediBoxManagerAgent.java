package myIngrediBox.agents.ingrediBoxManager;


import myIngrediBox.shared.classes.ServiceSearcher;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class IngrediBoxManagerAgent extends Agent {

	private static final long serialVersionUID = 1L;
	
	private AID InventoryManagerAgent = null;
	
	@Override
	protected void setup()
	{
		super.setup();
		SequentialBehaviour manageIngrediBox = new SequentialBehaviour();
				
		manageIngrediBox.addSubBehaviour(new IngredientRequestBehaviour(this));
		this.addBehaviour(manageIngrediBox);

	}
	
	@Override
	protected void takeDown()
	
	{
		super.takeDown();
	}
	
	private class IngredientRequestBehaviour extends SequentialBehaviour
	{

		private static final long serialVersionUID = 1L;

		public IngredientRequestBehaviour(Agent a)
		{
			super(a);
		}

		public void onStart()
		{
			//Service-search-SubBehaviour
			this.addSubBehaviour(new SimpleBehaviour() {

				@Override
				public void action()
				{
					InventoryManagerAgent = ServiceSearcher.searchForService(getAgent(), "PassIngredientsFromInventory-Service");
				}

				@Override
				public boolean done()
				{
					boolean ret = false;

					if (InventoryManagerAgent != null)
					{
						ret = true;
					}
					return ret;
				}
					
			}); // End: this.addSubBehaviour

			ACLMessage m = new ACLMessage(ACLMessage.REQUEST);
			
			// A-RE-I-Behaviour to send, receive and handle refuse
			this.addSubBehaviour(new AchieveREInitiator(this.myAgent, m) {

				@Override
				protected Vector prepareRequests(ACLMessage request)
				{
					Vector v = super.prepareRequests(request);
					for (Object o : v)
					{
						ACLMessage m = (ACLMessage) o;
						m.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
						m.addReceiver(InventoryManagerAgent);
						m.setContent("Are the following ingredients available?");
					}
					return v;
				}

				@Override
				protected void handleAgree(ACLMessage agree)
				{
					System.out.println("IngrediBoxManager received following message: " + agree);
				}

				@Override
				protected void handleInform(ACLMessage inform)
				{
					System.out.println("Message content: " + inform.getContent());
				}

				@Override
				protected void handleRefuse(ACLMessage refuse)
				{
					System.out.println("IngrediBoxManager IngrediBoxManager received following refuse: " + refuse);
					this.reset(new ACLMessage(ACLMessage.REQUEST));
				}
			});

		} // End: onStart()

		public void reset()
		{
			super.reset();
			InventoryManagerAgent = null;
		}

	} // End:private class IngredientRequestBehaviour

}
