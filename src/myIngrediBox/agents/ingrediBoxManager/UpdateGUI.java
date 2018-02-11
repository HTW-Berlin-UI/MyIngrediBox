package myIngrediBox.agents.ingrediBoxManager;

import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.gui.MyIngrediBoxGUI;

public class UpdateGUI extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	public UpdateGUI(DataStore datastore) {
		super();
		this.setDataStore(datastore);
	}

	@Override
	public void action() {
		MyIngrediBoxGUI gui = (MyIngrediBoxGUI) this.getDataStore().get("gui");
		gui.updateResponse("Fertig");
	}

}
