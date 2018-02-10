package myIngrediBox.ontologies;

public class Concept_PurchasableIngredient extends Concept_Ingredient {

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
		return super.toString() + " -> " + this.price + "€";
	}

}
