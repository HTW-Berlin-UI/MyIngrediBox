package myIngrediBox.ontologies;

import java.util.ArrayList;

import jade.content.AgentAction;

public class RequestBuyingAction implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<Ingredient> requiredIngredients;

	private BuyingPreference preference;

	public BuyingPreference getPreference() {
		return preference;
	}

	public void setPreference(BuyingPreference preference) {
		this.preference = preference;
	}

	public ArrayList<Ingredient> getRequiredIngredients() {
		return requiredIngredients;
	}

	public void setRequiredIngredients(ArrayList<Ingredient> requiredIngredients) {
		this.requiredIngredients = requiredIngredients;
	}

}
