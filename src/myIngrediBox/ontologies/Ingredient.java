package myIngrediBox.ontologies;

import java.util.Objects;

import jade.content.Concept;

public class Ingredient implements Concept {

	private String name;
	private double quantity;
	private Unit unit;

	public Ingredient() {
		super();
	}

	public Ingredient(String name, double quantity, Unit unit) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.unit = unit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	// compare ingredients by name
	public boolean equals(Object object) {
		try {
			Ingredient ingredientToCompare = (Ingredient) object;
			return this.name.equalsIgnoreCase(ingredientToCompare.name)
					&& this.unit.equals(ingredientToCompare.getUnit());
		} catch (ClassCastException cce) {
		}
		return false;

	}

	// make this hashable
	public int hashCode() {
		return Objects.hash(this.name);
	}

	// make this printable
	public String toString() {
		return this.quantity + "\t " + this.unit + "\t " + this.name;
	}

}
