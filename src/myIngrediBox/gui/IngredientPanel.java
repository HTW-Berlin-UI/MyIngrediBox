package myIngrediBox.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import myIngrediBox.ontologies.Ingredient;
import myIngrediBox.ontologies.Unit;

public class IngredientPanel extends JPanel {

	private JTextField nameField;
	private JTextField quantityField;
	private JButton addButton;
	private IngredientListener ingredientListener;
	private JComboBox unitComboBox;

	public IngredientPanel() {

		nameField = new JTextField(10);
		quantityField = new JTextField(10);

		addButton = new JButton("Add");

		unitComboBox = new JComboBox(Unit.values());
		unitComboBox.setSelectedIndex(-1);

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
				Double quantity = Double.parseDouble(quantityField.getText());
				Unit unit = (Unit) unitComboBox.getSelectedItem();

				if (ingredientListener != null) {
					IngredientEvent iv = new IngredientEvent(this, new Ingredient(name, quantity, unit));
					ingredientListener.ingredientEventOccured(iv);
				}
				nameField.setText("");
				quantityField.setText("");
				unitComboBox.setSelectedIndex(-1);
			}
		});

		layoutComponents();

	}

	public void layoutComponents() {

		Dimension dim = this.getPreferredSize();
		dim.width = 250;
		this.setPreferredSize(dim);

		TitledBorder innerBorder = BorderFactory.createTitledBorder("Add Ingredient");
		Border outerBorder = BorderFactory.createEmptyBorder(10, 20, 10, 10);

		this.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		this.setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();

		gc.weightx = 1;

		///////////// First Row///////////////////////
		gc.gridy = 0;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 0, 0, 5);
		this.add(new JLabel("Name: "), gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		this.add(nameField, gc);

		///////////// Next Row///////////////////////
		gc.gridy++;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		this.add(new JLabel("Quantity: "), gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		this.add(quantityField, gc);

		///////////// Next Row///////////////////////

		gc.gridy++;
		gc.weighty = 0.1;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		gc.insets = new Insets(0, 0, 0, 5);
		this.add(new JLabel("Unit: "), gc);

		gc.gridx = 1;
		gc.insets = new Insets(0, 0, 0, 0);
		gc.anchor = GridBagConstraints.LINE_START;
		this.add(unitComboBox, gc);

		///////////// Next Row///////////////////////

		gc.gridy++;
		gc.weighty = 1;

		gc.gridx = 1;
		gc.insets = new Insets(10, 0, 0, 0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		this.add(addButton, gc);

	}

	public void setIngredientListener(IngredientListener ingredientListener) {
		this.ingredientListener = ingredientListener;
	}

}
