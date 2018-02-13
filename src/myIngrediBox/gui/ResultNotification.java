package myIngrediBox.gui;

import java.util.ArrayList;

import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.Purchase;

public class ResultNotification {
	private ArrayList<Ingredient> availableIngredients;
	private ArrayList<Ingredient> leftovers;
	private ArrayList<Purchase> purchases;

	public ResultNotification(ArrayList<Ingredient> availableIngredients, ArrayList<Ingredient> leftovers,
			ArrayList<Purchase> boughtIngredients) {
		this.availableIngredients = availableIngredients;
		this.leftovers = leftovers;
		this.purchases = boughtIngredients;
	}

	public String getMessage() {
		StringBuilder message = new StringBuilder();

		message.append("That's already in your inventory:\n");
		availableIngredients.forEach(ingredient -> message.append(ingredient + "\n"));

		return message.toString();
	}

}
