package myIngrediBox.agents.ingrediBoxManager;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import myIngrediBox.ontologies.IngrediBoxOntology;
import myIngrediBox.shared.behaviours.DFQueryBehaviour;

public class IngrediBoxManagerAgent extends Agent {

	private static final long serialVersionUID = 1L;

	/**
	 * message language FIPA-SL
	 */
	private Codec codec = new SLCodec();

	/**
	 * ontology used for semantic parsing
	 */
	private Ontology ontology = IngrediBoxOntology.getInstance();

	@Override
	protected void setup() {
		super.setup();

		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(ontology);

		DFQueryBehaviour dfQueryBehaviour = new DFQueryBehaviour(this, "Inventory-Managing-Service");

		// for testing normal way would be user-interaction or another system
		this.addBehaviour(new WakerBehaviour(this, 5000) {

			private static final long serialVersionUID = 1L;

			protected void onWake() {
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