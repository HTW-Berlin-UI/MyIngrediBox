package myIngrediBox.agents.marketManager;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import myIngrediBox.ontologies.PurchasableIngredient;
import myIngrediBox.ontologies.Unit;

public class ParseStock extends OneShotBehaviour {

	public ParseStock(DataStore datastore) {
		super();
		this.setDataStore(datastore);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		// TODO Auto-generated method stub

		MarketManager marketManager = (MarketManager) this.getAgent();

		JSONObject rawData = (JSONObject) this.getDataStore().get("rawData");

		JSONArray inventory = (JSONArray) rawData.get("inventory");

		ArrayList<PurchasableIngredient> stock = new ArrayList<PurchasableIngredient>();

		Iterator<JSONObject> iterator = inventory.iterator();
		while (iterator.hasNext()) {

			PurchasableIngredient ingredient = new PurchasableIngredient();

			JSONObject rawIngredient = iterator.next();

			// create inventory object
			ingredient.setName(rawIngredient.get("name").toString());
			ingredient.setQuantity(Double.parseDouble(rawIngredient.get("quantity").toString()));
			ingredient.setUnit(Unit.valueOf(rawIngredient.get("unit").toString()));
			ingredient.setPrice(Double.parseDouble(rawIngredient.get("price").toString()));

			stock.add(ingredient);

		}

		this.getDataStore().put("stock", stock);

	}

}