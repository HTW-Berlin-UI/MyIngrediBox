package myIngrediBox.gui;

import java.util.EventObject;

import myIngrediBox.ontologies.Ingredient;

public class IngredientEvent extends EventObject {

	private Ingredient ingredient;

	public IngredientEvent(Object source) {
		super(source);
	}

	public IngredientEvent(Object source, Ingredient ingredient) {
		super(source);
		this.ingredient = ingredient;

	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

}
