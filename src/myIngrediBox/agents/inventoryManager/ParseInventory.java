package myIngrediBox.agents.inventoryManager;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.Unit;

public class ParseInventory extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {

		JSONObject rawData = (JSONObject) this.getDataStore().get("rawData");

		JSONArray jsonArrayinventory = (JSONArray) rawData.get("inventory");

		ArrayList<Ingredient> inventory = new ArrayList<Ingredient>();

		Iterator<JSONObject> iterator = jsonArrayinventory.iterator();
		while (iterator.hasNext()) {

			Ingredient ingredient = new Ingredient();

			JSONObject rawIngredient = iterator.next();

			// Create inventory object
			ingredient.setName(rawIngredient.get("name").toString());
			ingredient.setQuantity(Double.parseDouble(rawIngredient.get("quantity").toString()));
			ingredient.setUnit(Unit.valueOf(rawIngredient.get("unit").toString()));

			inventory.add(ingredient);

		}
		this.getDataStore().put("inventory", inventory);
	}

}
