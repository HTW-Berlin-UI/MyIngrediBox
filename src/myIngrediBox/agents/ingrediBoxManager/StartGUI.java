package myIngrediBox.agents.ingrediBoxManager;

import javax.swing.SwingUtilities;

import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.gui.MyIngrediBoxGUI;

public class StartGUI extends OneShotBehaviour {

	private boolean ready = false;

	public StartGUI(DataStore datastore) {
		super();
		this.setDataStore(datastore);
	}

	@Override
	public void action() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MyIngrediBoxGUI gui = new MyIngrediBoxGUI((IngrediBoxManagerAgent) getAgent());
				getDataStore().put("gui", gui);
			}
		});
	}

}
