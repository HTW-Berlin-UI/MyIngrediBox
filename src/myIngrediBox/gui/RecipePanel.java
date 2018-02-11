package myIngrediBox.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import myIngrediBox.ontologies.Ingredient;

public class RecipePanel extends JPanel {

	private JList<Ingredient> ingredientList;
	private DefaultListModel<Ingredient> ingredientModel;

	public RecipePanel() {

		Dimension dim = this.getPreferredSize();
		dim.width = 250;
		this.setPreferredSize(dim);

		Border outerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

		ingredientList = new JList<Ingredient>();
		ingredientModel = new DefaultListModel<Ingredient>();
		ingredientList.setModel(ingredientModel);
		ingredientList.setBorder(outerBorder);

		TitledBorder innerBorder = BorderFactory.createTitledBorder("Recipe");
		this.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		setLayout(new BorderLayout());

		add(new JScrollPane(ingredientList), BorderLayout.CENTER);

	}

	public void appendIngredient(Ingredient ingredient) {
		ingredientModel.addElement(ingredient);
	}

}
