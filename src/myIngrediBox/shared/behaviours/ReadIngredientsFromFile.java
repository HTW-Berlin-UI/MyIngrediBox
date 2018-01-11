package myIngrediBox.shared.behaviours;

import jade.core.behaviours.OneShotBehaviour;

public class ReadIngredientsFromFile extends OneShotBehaviour {

	private String path;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReadIngredientsFromFile(String path) {
		super();
		this.path = path;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		System.out.println(this.path);
	}

}
