package myIngrediBox.agents.inventoryManager;

import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import myIngrediBox.ontologies.HasIngredient;
import myIngrediBox.ontologies.IngrediBoxOntology;
import myIngrediBox.shared.behaviours.DeregisterServiceBehaviour;
import myIngrediBox.shared.behaviours.ReadFromFile;
import myIngrediBox.shared.behaviours.RegisterServiceBehaviour;

public class InventoryManagerAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * message language FIPA-SL
	 */
	private Codec codec = new SLCodec();

	/**
	 * ontology used for semantic parsing
	 */
	private Ontology ontology = IngrediBoxOntology.getInstance();

	protected void setup() {
		super.setup();
		// Register Service
		RegisterServiceBehaviour registerServiceBehaviour = new RegisterServiceBehaviour(this,
				"Inventory-Managing-Service");

		this.getContentManager().registerLanguage(codec);
		this.getContentManager().registerOntology(ontology);

		// Load Inventory
		ReadFromFile loadInventory = new ReadFromFile("assets/inventory/inventory.json");
		ParseInventory parseInventory = new ParseInventory();
		SequentialBehaviour manageInventory = new SequentialBehaviour();

		manageInventory.addSubBehaviour(loadInventory);
		manageInventory.addSubBehaviour(parseInventory);
		manageInventory.addSubBehaviour(registerServiceBehaviour);

		loadInventory.setDataStore(manageInventory.getDataStore());
		parseInventory.setDataStore(manageInventory.getDataStore());

		this.addBehaviour(manageInventory);

		AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		// react to message matching the template
		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchLanguage(codec.getName()),
				MessageTemplate.MatchOntology(ontology.getName()));
		;

		// add A-RE-R-Behaviour to receive and respond
		this.addBehaviour(new AchieveREResponder(this, mt) {

			@Override
			protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
				System.out.println("Request message content: " + request.getContent());
				ACLMessage response = request.createReply();
				try // valid dateformat?
				{

					ContentElement ce = null;
					ce = getContentManager().extractContent(request);
					if (ce instanceof HasIngredient) {
						HasIngredient hasIngredient = (HasIngredient) ce; // now do something
						System.out.println("ich habe anfrage für: " + hasIngredient.getIngredient().getName());
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
			protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
					throws FailureException {
				if (request.getContent() != null) {
					try {
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
		}); // End addBehaviour(new AchieveREResponder

	}

	@Override
	protected void takeDown() {
		super.takeDown();
		this.addBehaviour(new DeregisterServiceBehaviour(this));
	}

}
