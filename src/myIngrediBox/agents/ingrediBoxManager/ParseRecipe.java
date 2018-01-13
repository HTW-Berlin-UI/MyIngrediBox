package myIngrediBox.agents.ingrediBoxManager;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jade.core.behaviours.OneShotBehaviour;

public class ParseRecipe extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		// TODO Auto-generated method stub

		JSONObject rawData = (JSONObject) this.getDataStore().get("rawData");

		JSONArray ingredients = (JSONArray) rawData.get("ingredients");
		Iterator<JSONObject> iterator = ingredients.iterator();

		System.out.println("\nIngredients:");		
		while (iterator.hasNext()) {
			JSONObject ingredient = iterator.next();
			System.out.print(ingredient.get("quantity") + "\t");
			System.out.print(ingredient.get("unit") + "\t");
			System.out.println(ingredient.get("name"));
		}
	}

}
