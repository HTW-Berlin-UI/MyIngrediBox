package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;
import java.util.Vector;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.proto.ProposeInitiator;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.IngredientSendingAction;

public class LeftoversPropose extends ProposeInitiator {

	private static final long serialVersionUID = 1L;

	public LeftoversPropose(Agent a, ACLMessage initiation, DataStore store) {
		super(a, initiation, store);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Vector prepareInitiations(ACLMessage propose) {

		// Get Leftovers
		ArrayList<Ingredient> leftovers = (ArrayList<Ingredient>) this.getDataStore().get("leftovers");

		if (!leftovers.isEmpty()) {

			try {
				// Set proper langue and ontology
				String[] ontologies = this.getAgent().getContentManager().getOntologyNames();
				String[] languages = this.getAgent().getContentManager().getLanguageNames();

				if (ontologies[0] != null)
					propose.setOntology(ontologies[0]);
				if (languages[0] != null)
					propose.setLanguage(languages[0]);

				// Add InventoryManager as receiver
				ArrayList<AID> inventoryManagers = (ArrayList<AID>) this.getDataStore()
						.get("Inventory-Managing-Service");
				propose.addReceiver(inventoryManagers.get(0));

				// Create action that holds leftovers
				IngredientSendingAction sendLeftovers = new IngredientSendingAction();
				sendLeftovers.setIngredients(leftovers);

				Action sendLeftoversAction = new Action(this.getAgent().getAID(), sendLeftovers);

				this.getAgent().getContentManager().fillContent(propose, sendLeftoversAction);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return super.prepareInitiations(propose);
	}

}
