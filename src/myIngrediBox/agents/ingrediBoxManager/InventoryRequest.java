package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;
import java.util.Vector;

import jade.content.ContentElement;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import myIngrediBox.ontologies.HasIngredient;
import myIngrediBox.ontologies.IngrediBoxOntology;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.Unit;

//import myIngrediBox.ontologies.Ingerdient;
//... other ontologies

public class InventoryRequest extends AchieveREInitiator {

	/**
	 * message language FIPA-SL
	 */
	private Codec codec = new SLCodec();

	/**
	 * ontology used for semantic parsing
	 */
	private Ontology ontology = IngrediBoxOntology.getInstance();

	private static final long serialVersionUID = 1L;

	public InventoryRequest(Agent a, ACLMessage msg, DataStore store) {
		super(a, msg, store);
	}

	public InventoryRequest(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	/**
	 * prepares the ACLMessage(s) for Requesting Ingredients
	 */
	@Override
	protected Vector prepareRequests(ACLMessage request) {
		Vector v = new Vector();

		// test ingredient
		Ingredient mehl = new Ingredient();
		mehl.setName("Mehl");
		mehl.setQuantity(0.5);
		mehl.setUnit(Unit.Kilo);

		// test ingredient 2
		Ingredient salz = new Ingredient();
		salz.setName("Salz");
		salz.setQuantity(0.25);
		salz.setUnit(Unit.Kilo);

		HasIngredient hasIngredient = new HasIngredient();

		hasIngredient.setOwner(this.getAgent().getAID());
		hasIngredient.setIngredient(mehl);
		hasIngredient.setIngredient(salz);

		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		request.setOntology(ontology.getName()); // myAgent

		ArrayList<AID> inventoryManagers = (ArrayList<AID>) this.getDataStore().get("Inventory-Managing-Service");

		request.addReceiver(inventoryManagers.get(0));
		request.setLanguage(codec.getName());

		try {
			this.getAgent().getContentManager().fillContent(request, hasIngredient);
		} catch (CodecException | OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// // test book: known by LibA
		// Author author = new Author( "Terry Pratchett" );
		// Book book = new Book();
		// book.setTitel( "Small Gods" );
		// book.addAuthor( author );

		// construct AgentAction
		// LendBook lb = new LendBook();
		// lb.setRequester( this.myAgent.getAID() );
		//// lb.setPredicate( ic );
		// lb.setBookToLend( book );

		// in FIPA-SL AgentAction must be encapsulated by an ACTION
		// to underline that this is an action without ontology-parsing
		// Action a = new Action();
		// a.setActor( this.myAgent.getAID() );
		// a.setAction( lb );

		// try
		// {
		// // use CM for Parsing of JAVA-Action to FIPA-SL-Text
		//// this.myAgent.getContentManager().fillContent( request, a ); //a=AgentAction
		//
		// // could be X request to X Agents
		// v.add( request );
		// }
		// catch (CodecException e)
		// {
		// e.printStackTrace();
		// }
		// catch (OntologyException e)
		// {
		// e.printStackTrace();
		// }

		// TODO request Message anh�ngen...

		v.add(request);

		return v;
	}

	/**
	 * Responder role agreed => this method is automatically called
	 */
	@Override
	protected void handleAgree(ACLMessage agree) {
		System.out.println("\nAgreed: " + agree);
	}

	/**
	 * Responder role not only agreed, but now says the request effect is realized
	 * => this method is automatically called
	 */
	@Override
	protected void handleInform(ACLMessage inform) {
		System.out.println("\nSome Inform: " + inform);
	}

	/**
	 * Responder doesnt want to play with us
	 */
	@Override
	protected void handleRefuse(ACLMessage refuse) {
		ContentElement ce = null;
		try {
			ce = this.myAgent.getContentManager().extractContent(refuse);
		} catch (UngroundedException e) {
			e.printStackTrace();
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}

		if (ce != null) {
			try {
				// NOT in combination with our Predicate InCatalogue tells us
				// LibAgent does not know book => in RL stop querying
				AbsPredicate ap = (AbsPredicate) ce;
				if (ap.getTypeName().equalsIgnoreCase(SLVocabulary.NOT)) {

					// try
					// {
					// InCatalogue ic = (InCatalogue) ontology.toObject( ap.getAbsObject(
					// SLVocabulary.NOT_WHAT ) );
					// System.out.println( "UserAgent: "+ ic.getCatalogueAgent().getLocalName()
					// +" does not know: " + ic.getBook().getTitel() );
					// System.out.println("TryBlock in InventoryRequest line 170 called");
					// }
					// catch (UngroundedException e)
					// {
					// e.printStackTrace();
					// }
					// catch (OntologyException e)
					// {
					// e.printStackTrace();
					// }

				}

			} catch (ClassCastException cce2) {
				System.out.println("\nRefuse not understood: " + ce);
			}

		} else {
			System.out.println("\nRefuse with empty Content: " + refuse);
		}

	}
}