package myIngrediBox.agents.inventoryManager;

import java.util.ArrayList;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ProposeResponder;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.IngredientSendingAction;

public class HandleLeftovers extends ProposeResponder {

	private static final long serialVersionUID = 1L;

	public HandleLeftovers(Agent a, MessageTemplate mt, DataStore store) {
		super(a, mt, store);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ACLMessage prepareResponse(ACLMessage propose) throws NotUnderstoodException, RefuseException {

		ACLMessage response = propose.createReply();
		response.setPerformative(ACLMessage.REJECT_PROPOSAL);
		try {
			Action a = (Action) this.myAgent.getContentManager().extractContent(propose);
			IngredientSendingAction sendLeftovers = (IngredientSendingAction) a.getAction();

			ArrayList<Ingredient> leftovers = sendLeftovers.getIngredients();

			ArrayList<Ingredient> inventory = (ArrayList<Ingredient>) this.getDataStore().get("inventory");

			// Update inventory with leftovers
			leftovers.forEach(ingredient -> {
				if (inventory.contains(ingredient)) {
					Ingredient presentIngredient = inventory.get(inventory.indexOf(ingredient));
					presentIngredient.setQuantity(presentIngredient.getQuantity() + ingredient.getQuantity());
				} else {
					inventory.add((Ingredient) ingredient);
				}
			});

			this.printList(inventory, "Updated Inventory:");

			response.setPerformative(ACLMessage.ACCEPT_PROPOSAL);

		} catch (UngroundedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
		return response;
	}

	private void printList(ArrayList<Ingredient> list, String listname) {
		System.out.println("\n" + listname + ":");

		for (Ingredient ingredient : list) {
			System.out.print(ingredient.getName() + "\t");
			System.out.print(ingredient.getQuantity() + "\t");
			System.out.println(ingredient.getUnit());
		}

	}

}
