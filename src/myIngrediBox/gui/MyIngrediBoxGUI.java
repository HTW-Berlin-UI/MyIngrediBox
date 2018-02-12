package myIngrediBox.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;

import myIngrediBox.agents.ingrediBoxManager.IngrediBoxManagerAgent;
import myIngrediBox.ontologies.BuyingPreference;
import myIngrediBox.ontologies.Ingredient;

public class MyIngrediBoxGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private IngrediBoxManagerAgent agent;
	private ActionBar actionBar;
	private IngredientPanel ingredientPanel;
	private RecipePanel recipePanel;
	private OutputPanel outputPanel;

	public MyIngrediBoxGUI(IngrediBoxManagerAgent agent) {
		super("MyIngrediBox");

		this.agent = agent;
		actionBar = new ActionBar();
		ingredientPanel = new IngredientPanel();
		recipePanel = new RecipePanel();
		outputPanel = new OutputPanel();

		setLayout(new BorderLayout());

		add(actionBar, BorderLayout.SOUTH);
		add(ingredientPanel, BorderLayout.WEST);
		add(recipePanel, BorderLayout.CENTER);
		add(outputPanel, BorderLayout.EAST);

		setBounds(300, 200, 750, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		ingredientPanel.setIngredientListener(new IngredientListener() {
			public void ingredientEventOccured(IngredientEvent e) {
				recipePanel.appendIngredient(e.getIngredient());
			}
		});

		actionBar.setSubmitListener(new SubmitListener() {
			public void submit(BuyingPreference preference) {
				System.out.println(preference);
				agent.proceed();
			}
		});

		recipePanel.setDataRequestListener(new DataRequestListener() {
			public void requestSampleData() {
				agent.getSampleData();
			}

		});

	}

	public void updateResponse(String text) {
		this.outputPanel.appendText(text);
	}

	public void receiveRecipe(ArrayList<Ingredient> recipe) {
		this.recipePanel.setSampleDate(recipe);
	}

}
