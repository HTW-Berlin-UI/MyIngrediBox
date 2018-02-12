package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.gui.MyIngrediBoxGUI;
import myIngrediBox.gui.ResultNotification;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.Purchase;

public class UpdateGUI extends OneShotBehaviour {

	private GUIUpdateType type;
	MyIngrediBoxGUI gui;

	private static final long serialVersionUID = 1L;

	public UpdateGUI(MyIngrediBoxGUI gui, GUIUpdateType type, DataStore datastore) {
		super();
		this.setDataStore(datastore);
		this.gui = gui;
		this.type = type;
	}

	@Override
	public void action() {
		switch (type) {
		case SAMPLE_DATA:
			ArrayList<Ingredient> recipe = (ArrayList<Ingredient>) this.getDataStore().get("recipe");
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					gui.receiveRecipe(recipe);
				}
			});
			break;
		case RESULT:
			ArrayList<Ingredient> availableIngredients = (ArrayList<Ingredient>) this.getDataStore()
					.get("availableIngredients");
			ArrayList<Ingredient> leftovers = (ArrayList<Ingredient>) this.getDataStore().get("leftovers");
			ArrayList<Purchase> boughtIngredients = (ArrayList<Purchase>) this.getDataStore().get("boughtIngredients");
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					gui.updateResponse(new ResultNotification(availableIngredients, leftovers, boughtIngredients));
				}
			});
		default:
			break;
		}

	}

}
