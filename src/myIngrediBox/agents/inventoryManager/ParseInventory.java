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

		InventoryManagerAgent inventoryManager = (InventoryManagerAgent) this.getAgent();

		JSONObject rawData = (JSONObject) this.getDataStore().get("rawData");

		JSONArray inventory = (JSONArray) rawData.get("inventory");

		ArrayList<Ingredient> tempIngredients = new ArrayList<Ingredient>();

		Iterator<JSONObject> iterator = inventory.iterator();
		while (iterator.hasNext()) {

			Ingredient ingredient = new Ingredient();

			JSONObject rawIngredient = iterator.next();

			// Create inventory object
			ingredient.setName(rawIngredient.get("name").toString());
			ingredient.setQuantity(Double.parseDouble(rawIngredient.get("quantity").toString()));
			ingredient.setUnit(Unit.valueOf(rawIngredient.get("unit").toString()));

			tempIngredients.add(ingredient);

		}

		inventoryManager.setInventory(tempIngredients);

	}

}
