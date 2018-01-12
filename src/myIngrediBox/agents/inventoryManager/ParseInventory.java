package myIngrediBox.agents.inventoryManager;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jade.core.behaviours.OneShotBehaviour;

public class ParseInventory extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		// TODO Auto-generated method stub

		JSONObject rawData = (JSONObject) this.getDataStore().get("rawData");

		JSONArray inventory = (JSONArray) rawData.get("inventory");

		System.out.println("\nInventory:");
		Iterator<JSONObject> iterator = inventory.iterator();
		while (iterator.hasNext()) {
			JSONObject ingredient = iterator.next();
			System.out.print(ingredient.get("quantity") + "\t");
			System.out.print(ingredient.get("unit") + "\t");
			System.out.println(ingredient.get("name"));
		}
	}

}
