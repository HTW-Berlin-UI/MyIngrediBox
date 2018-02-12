package myIngrediBox.ontologies;

import java.util.ArrayList;

import jade.content.Concept;

public class Purchase implements Concept {

	private String market;
	private ArrayList<PurchasableIngredient> boughtIngredients;

	public Purchase() {
	}

	public Purchase(String market, ArrayList<PurchasableIngredient> boughtIngredients) {
		this.market = market;
		this.boughtIngredients = boughtIngredients;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public ArrayList<PurchasableIngredient> getBoughtIngredients() {
		return boughtIngredients;
	}

	public void setBoughtIngredients(ArrayList<PurchasableIngredient> boughtIngredients) {
		this.boughtIngredients = boughtIngredients;
	}

}
