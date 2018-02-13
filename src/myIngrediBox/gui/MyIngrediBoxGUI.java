package myIngrediBox.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

		JPanel inputWrap = new JPanel();
		inputWrap.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		inputWrap.add(ingredientPanel);
		inputWrap.add(recipePanel);
		Dimension dim = inputWrap.getPreferredSize();
		dim.width = 250;
		inputWrap.setPreferredSize(dim);
		inputWrap.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

		add(actionBar, BorderLayout.SOUTH);
		add(inputWrap, BorderLayout.WEST);
		add(outputPanel, BorderLayout.CENTER);

		setBounds(300, 200, 650, 632);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		ingredientPanel.setIngredientListener(new IngredientListener() {
			public void ingredientEventOccured(IngredientEvent e) {
				recipePanel.appendIngredient(e.getIngredient());
			}
		});

		actionBar.setSubmitListener(new SubmitListener() {
			public void submit(BuyingPreference preference) {

				ArrayList<Ingredient> recipe = Collections.list(recipePanel.getRecipe());

				if (!recipe.isEmpty())
					agent.proceed(recipe, preference);
			}
		});

		recipePanel.setDataRequestListener(new DataRequestListener() {
			public void requestSampleData() {
				agent.getSampleData();
			}

		});

	}

	public void updateResponse(ResultNotification result) {
		this.outputPanel.setText(result.getMessage());
	}

	public void receiveRecipe(ArrayList<Ingredient> recipe) {
		this.recipePanel.setSampleDate(recipe);
	}

}
