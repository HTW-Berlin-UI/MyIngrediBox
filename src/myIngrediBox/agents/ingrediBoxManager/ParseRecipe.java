package myIngrediBox.agents.ingrediBoxManager;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.agents.inventoryManager.InventoryManagerAgent;
import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.Unit;

public class ParseRecipe extends OneShotBehaviour
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void action()
	{
		
		JSONObject rawData = (JSONObject) this.getDataStore().get("rawData");

		JSONArray recipe = (JSONArray) rawData.get("ingredients");
		
		ArrayList<Ingredient> tempRecipe = new ArrayList<Ingredient>();

		Iterator<JSONObject> iterator = recipe.iterator();
		while (iterator.hasNext()) {

			Ingredient ingredient = new Ingredient();

			JSONObject rawIngredient = iterator.next();

			// create inventory object
			ingredient.setName(rawIngredient.get("name").toString());
			ingredient.setQuantity(Double.parseDouble(rawIngredient.get("quantity").toString()));
			ingredient.setUnit(Unit.valueOf(rawIngredient.get("unit").toString()));

			tempRecipe.add(ingredient);
		}

		this.getDataStore().put("recipe", tempRecipe);
	}

}
