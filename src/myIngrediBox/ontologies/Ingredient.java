package myIngrediBox.ontologies;

import jade.content.*;

public class Ingredient implements Concept {

	private String name;
	private double quantity;
	private Unit unit;
	
	public Ingredient()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getQuantity()
	{
		return quantity;
	}

	public void setQuantity(double quantity)
	{
		this.quantity = quantity;
	}

	public Unit getUnit()
	{
		return unit;
	}

	public void setUnit(Unit unit)
	{
		this.unit = unit;
	}
	
	

}
