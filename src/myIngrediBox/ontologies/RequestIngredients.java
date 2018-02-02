package myIngrediBox.ontologies;

import java.util.ArrayList;

import jade.content.AgentAction;
import jade.core.AID;

public class RequestIngredients implements AgentAction {

	private static final long serialVersionUID = 1L;

	private ArrayList<Ingredient> requiredIngredients;

	private AID ibm;

	public ArrayList<Ingredient> getRequiredIngredients() {
		return requiredIngredients;
	}

	public void setRequiredIngredients(ArrayList<Ingredient> requiredIngredients) {
		this.requiredIngredients = requiredIngredients;
	}

	public AID getBuyer() {
		return ibm;
	}

	public void setBuyer(AID ibm) {
		this.ibm = ibm;
	}

}

