package myIngrediBox.agents.inventoryManager;

import java.util.ArrayList;
import java.util.Iterator;

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
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
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.IngredientRequestAction;
import myIngrediBox.shared.behaviours.DeregisterServiceBehaviour;
import myIngrediBox.shared.behaviours.PrintIngredientList;
import myIngrediBox.shared.behaviours.ReadFromFile;
import myIngrediBox.shared.behaviours.RegisterServiceBehaviour;

public class InventoryManagerAgent extends Agent {

	private ArrayList<Ingredient> inventory;
	private ArrayList<Ingredient> requestedIngredients;

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

		// this.inventory = new ArrayList<Ingredient>();

		// Load Inventory
		ReadFromFile loadInventory = new ReadFromFile("assets/inventory/inventory.json");
		ParseInventory parseInventory = new ParseInventory();
		PrintIngredientList printIngredientBehaviour = new PrintIngredientList(this.inventory);
		SequentialBehaviour manageInventory = new SequentialBehaviour();

		manageInventory.addSubBehaviour(loadInventory);
		manageInventory.addSubBehaviour(parseInventory);
		manageInventory.addSubBehaviour(registerServiceBehaviour);
		manageInventory.addSubBehaviour(printIngredientBehaviour);

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
				ACLMessage response = request.createReply();
				try // valid dateformat?
				{
					ContentElement ce = null;
					
					//ce will be instance of Action
					ce = getContentManager().extractContent(request);

					//getContentObject() could receive Java Object, but not recommended cause not FIPA
					//and if Agent not on Java platform
					if (ce instanceof Action) {
						Action action = (Action) ce;
						IngredientRequestAction ingredientRequestAction = (IngredientRequestAction) action.getAction();
						setRequestedIngredients(ingredientRequestAction.getRequiredIngredients());
						Iterator<Ingredient> iterator = ingredientRequestAction.getRequiredIngredients().iterator();
						
						System.out.println("\nIM received request for: ");
						while (iterator.hasNext())
						{
							Ingredient ingredient = iterator.next();
							System.out.print(ingredient.getName() + "\t");
						}								
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
	
	public ArrayList<Ingredient> getInventory() {
		return inventory;
	}

	public void setInventory(ArrayList<Ingredient> inventory) {
		this.inventory = inventory;
	}

	public ArrayList<Ingredient> getRequestedIngredients()
	{
		return requestedIngredients;
	}

	public void setRequestedIngredients(ArrayList<Ingredient> requestedIngredients)
	{
		this.requestedIngredients = requestedIngredients;
	}

}
