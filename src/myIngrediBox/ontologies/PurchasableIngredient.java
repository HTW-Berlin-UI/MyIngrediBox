package myIngrediBox.ontologies;

import java.text.NumberFormat;
import java.util.Locale;

public class PurchasableIngredient extends Ingredient {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double price = -1;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	// include price when printed
	public String toString() {

		Double currencyAmount = this.price;
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());

		return super.toString() + "\t" + currencyFormatter.format(currencyAmount);
	}

}
