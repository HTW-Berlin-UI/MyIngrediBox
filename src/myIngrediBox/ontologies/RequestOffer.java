package myIngrediBox.ontologies;

import java.util.ArrayList;

import jade.content.AgentAction;
import jade.core.AID;

public class RequestOffer implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<Ingredient> requiredIngredients;

	private AID buyer;

	public ArrayList<Ingredient> getRequiredIngredients() {
		return requiredIngredients;
	}

	public void setRequiredIngredients(ArrayList<Ingredient> requiredIngredients) {
		this.requiredIngredients = requiredIngredients;
	}

	public AID getBuyer() {
		return buyer;
	}

	public void setBuyer(AID buyer) {
		this.buyer = buyer;
	}

}
