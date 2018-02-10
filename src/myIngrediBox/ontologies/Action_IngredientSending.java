package myIngrediBox.ontologies;

import java.util.ArrayList;

import jade.content.AgentAction;
import jade.core.AID;

public class Action_IngredientSending implements AgentAction {

	private static final long serialVersionUID = 1L;

	private ArrayList<Concept_Ingredient> concept_Ingredients;

	private AID agent;

	public ArrayList<Concept_Ingredient> getIngredients() {
		return concept_Ingredients;
	}

	public void setIngredients(ArrayList<Concept_Ingredient> concept_Ingredients) {
		this.concept_Ingredients = concept_Ingredients;
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

