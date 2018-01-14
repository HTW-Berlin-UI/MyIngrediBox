package myIngrediBox.ontologies;

import jade.content.Predicate;
import jade.core.AID;

public class HasIngredient implements Predicate
{
	private AID owner;
	private Ingredient ingredient;
	
	
	public HasIngredient()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	public AID getOwner()
	{
		return owner;
	}
	public void setOwner(AID owner)
	{
		this.owner = owner;
	}
	public Ingredient getIngredient()
	{
		return ingredient;
	}
	public void setIngredient(Ingredient ingredient)
	{
		this.ingredient = ingredient;
	}
	
	
	

}
