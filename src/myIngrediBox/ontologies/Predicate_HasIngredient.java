package myIngrediBox.ontologies;

import java.util.ArrayList;

import jade.content.Predicate;
import jade.core.AID;

public class Predicate_HasIngredient implements Predicate {
	private AID owner;
	private Concept_Ingredient concept_Ingredient;

	public Predicate_HasIngredient() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AID getOwner() {
		return owner;
	}

	public void setOwner(AID owner) {
		this.owner = owner;
	}

	public Concept_Ingredient getIngredient() {
		return concept_Ingredient;
	}

	public void setIngredient(Concept_Ingredient concept_Ingredient) {
		this.concept_Ingredient = concept_Ingredient;
	}

}
