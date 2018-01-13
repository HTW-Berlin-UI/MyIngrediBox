package myIngrediBox.agents.ingrediBoxManager;

import myIngrediBox.agents.inventoryManager.ParseInventory;
import myIngrediBox.shared.behaviours.ReadFromFile;
import myIngrediBox.shared.behaviours.RequestServiceBehaviour;
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

		
		ReadFromFile loadRecipe = new ReadFromFile("assets/recipes/Eierkuchen.json");
		ParseInventory parseRecipe = new ParseInventory();
		
		//RequestServiceBehaviour requestServiceBehaviour = new RequestServiceBehaviour(this, "PassIngredientsFromInventory-Service");
		//requestServiceBehaviour.setDataStore(manageIngrediBox.getDataStore());
		
		//manageIngrediBox.setDataStore(requestServiceBehaviour.getDataStore());
		
		//InventoryManagerAgent = (AID) requestServiceBehaviour.getDataStore().get("ServiceProviderAgent");
		
		manageIngrediBox.addSubBehaviour(new IngredientRequestBehaviour(this));
		this.addBehaviour(manageIngrediBox);

		//this.addBehaviour(new IngredientRequestBehaviour(this));
	}
	
	@Override
	protected void takeDown()
	
	{
		super.takeDown();
	}
	
	private class IngredientRequestBehaviour extends SequentialBehaviour
	{
		public IngredientRequestBehaviour(Agent a)
		{
			super(a);
		}

		public void onStart()
		{
			//Servie-suchen-SubBehaviour (DF anfragen):  (adding DF querying for ClockAgent)
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
					//Kurzschreibweise: de.wikibooks.org/wiki/Java_Standard:_Kontrollstrukturen#Ablauf_einer_for-Schleife
					for (Object o : v) //= 'iteriere über alle Objekte o in Vector v'
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

	} // End:private class TimeRequestBehaviour

}
