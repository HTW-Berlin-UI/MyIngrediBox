package myIngrediBox.ontologies;

import java.util.ArrayList;

import jade.content.AgentAction;

public class Action_RequestBuying implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<Concept_Ingredient> requiredIngredients;

	private BuyingPreference preference;

	public BuyingPreference getPreference() {
		return preference;
	}

	public void setPreference(BuyingPreference preference) {
		this.preference = preference;
	}

	public ArrayList<Concept_Ingredient> getRequiredIngredients() {
		return requiredIngredients;
	}

	public void setRequiredIngredients(ArrayList<Concept_Ingredient> requiredIngredients) {
		this.requiredIngredients = requiredIngredients;
	}

}
