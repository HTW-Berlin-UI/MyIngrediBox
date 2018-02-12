package myIngrediBox.gui;

import java.util.ArrayList;

import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.Purchase;

public class ResultNotification {
	private ArrayList<Ingredient> availableIngredients;
	private ArrayList<Ingredient> leftovers;
	private ArrayList<Purchase> boughtIngredients;

	public ResultNotification(ArrayList<Ingredient> availableIngredients, ArrayList<Ingredient> leftovers,
			ArrayList<Purchase> boughtIngredients) {
		this.availableIngredients = availableIngredients;
		this.leftovers = leftovers;
		this.boughtIngredients = boughtIngredients;
	}

}
