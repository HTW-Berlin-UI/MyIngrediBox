package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.gui.MyIngrediBoxGUI;
import myIngrediBox.ontologies.Ingredient;

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
			gui.updateResponse("Fertig");
		default:
			break;
		}

	}

}
