package myIngrediBox.agents.ingrediBoxManager;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jade.core.behaviours.SimpleBehaviour;

//former "ReduceRecipeBehaviour"
public class TrimPurchaseListBehaviour extends SimpleBehaviour
{

	private static final long serialVersionUID = 1L;

	@Override
	public void action()
	{
		JSONObject rawData = (JSONObject) this.getDataStore().get("rawData");
		JSONObject rawData2 = (JSONObject) this.getDataStore().get("rawData");


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

	@Override
	public boolean done()
	{
		// TODO Auto-generated method stub
		return false;
	}


}
