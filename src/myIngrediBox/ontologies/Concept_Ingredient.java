package myIngrediBox.ontologies;

import java.util.Objects;

import jade.content.Concept;

public class Concept_Ingredient implements Concept {

	private String name;
	private double quantity;
	private Unit unit;

	public Concept_Ingredient() {
		super();
	}

	public Concept_Ingredient(String name, double quantity, Unit unit) {
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
			Concept_Ingredient ingredientToCompare = (Concept_Ingredient) object;
			return this.name.equalsIgnoreCase(ingredientToCompare.name);
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
		return this.name + ": " + this.quantity + " " + this.unit;
	}

}
