package myIngrediBox.ontologies;

import java.util.ArrayList;

import jade.content.AgentAction;
import jade.core.AID;

public class IngredientSendingAction implements AgentAction {

	private static final long serialVersionUID = 1L;

	private ArrayList<Ingredient> ingredients;

	private AID agent;

	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public AID getAgent()
	{
		return agent;
	}

	public void setAgent(AID agent)
	{
		this.agent = agent;
	}

}

