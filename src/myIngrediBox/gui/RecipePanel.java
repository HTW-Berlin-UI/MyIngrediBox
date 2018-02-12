package myIngrediBox.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import myIngrediBox.ontologies.Ingredient;

public class RecipePanel extends JPanel implements ActionListener {

	private JList<Ingredient> ingredientList;
	private DefaultListModel<Ingredient> ingredientModel;
	private DataRequestListener dataRequestListener;
	private JButton deleteButton;
	private JButton testDataButton;

	public RecipePanel() {

		Dimension dim = this.getPreferredSize();
		dim.width = 250;
		this.setPreferredSize(dim);

		deleteButton = new JButton("Delete");
		testDataButton = new JButton("Sample Data");

		deleteButton.addActionListener(this);
		testDataButton.addActionListener(this);

		Border outerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);

		ingredientList = new JList<Ingredient>();
		ingredientModel = new DefaultListModel<Ingredient>();
		ingredientList.setModel(ingredientModel);
		ingredientList.setBorder(outerBorder);

		TitledBorder innerBorder = BorderFactory.createTitledBorder("Recipe");
		this.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		setLayout(new BorderLayout());

		add(new JScrollPane(ingredientList), BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		buttonPanel.add(testDataButton);
		buttonPanel.add(deleteButton);

		add(buttonPanel, BorderLayout.SOUTH);

	}

	public void appendIngredient(Ingredient ingredient) {
		ingredientModel.addElement(ingredient);
	}

	public void actionPerformed(ActionEvent e) {
		JButton clicked = (JButton) e.getSource();

		if (clicked.equals(deleteButton)) {

			int[] selection = ingredientList.getSelectedIndices();

			if (selection == null || selection.length == 0) {
				ingredientModel.clear();
			} else {
				Ingredient[] copy = new Ingredient[ingredientModel.getSize()];
				ingredientModel.copyInto(copy);
				for (int i : selection)
					ingredientModel.removeElement(copy[i]);
			}

		} else if (clicked.equals(testDataButton)) {
			if (this.dataRequestListener != null)
				this.dataRequestListener.requestSampleData();
		}

	}

	public void setDataRequestListener(DataRequestListener dataRequestListener) {
		this.dataRequestListener = dataRequestListener;
	}

	public void setSampleDate(ArrayList<Ingredient> recipe) {
		this.ingredientModel.clear();
		recipe.forEach(ingredient -> this.ingredientModel.addElement(ingredient));
	}

	public Enumeration<Ingredient> getRecipe() {
		return this.ingredientModel.elements();
	}

}
