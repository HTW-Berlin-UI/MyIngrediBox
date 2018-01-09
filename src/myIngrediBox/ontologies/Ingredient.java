package myIngrediBox.ontologies;

import jade.content.Concept;

public class Ingredient implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
