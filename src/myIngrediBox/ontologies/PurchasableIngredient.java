package myIngrediBox.ontologies;

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

}
