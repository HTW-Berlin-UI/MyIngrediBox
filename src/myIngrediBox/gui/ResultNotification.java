package myIngrediBox.gui;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.Purchase;

public class ResultNotification {
	private ArrayList<Ingredient> availableIngredients;
	private ArrayList<Ingredient> leftovers;
	private ArrayList<Purchase> purchases;

	public ResultNotification(ArrayList<Ingredient> availableIngredients, ArrayList<Ingredient> leftovers,
			ArrayList<Purchase> purchases) {
		this.availableIngredients = availableIngredients;
		this.leftovers = leftovers;
		this.purchases = purchases;
	}

	public String getMessage() {
		StringBuilder message = new StringBuilder();

		message.append("That's already in your inventory:\n\n");
		availableIngredients.forEach(ingredient -> message.append(ingredient + "\n"));

		purchases.forEach(purchase -> {
			message.append("\nGot these from " + purchase.getMarket() + ":\n\n");
			purchase.getBoughtIngredients().forEach(ingredient -> message.append(ingredient + "\n"));
		});

		message.append("\nThis goes back to your inventory:\n\n");
		leftovers.forEach(leftover -> message.append(leftover + "\n"));

		Double currencyAmount = this.getTotalPrice();
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());

		message.append("\nTotal Price:\t" + currencyFormatter.format(currencyAmount));
		message.append("\n-------------------");

		return message.toString();
	}

	private double getTotalPrice() {

		double totalPrice = 0;

		for (Purchase purchase : this.purchases) {
			totalPrice += purchase.getBoughtIngredients().stream().collect(Collectors.summingDouble(f -> f.getPrice()));
		}

		return totalPrice;
	}

}
