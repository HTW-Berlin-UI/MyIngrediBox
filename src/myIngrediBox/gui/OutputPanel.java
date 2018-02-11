package myIngrediBox.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class OutputPanel extends JPanel {

	private JTextArea textArea;

	public OutputPanel() {
		textArea = new JTextArea();

		Dimension dim = this.getPreferredSize();
		dim.width = 250;
		this.setPreferredSize(dim);

		TitledBorder innerBorder = BorderFactory.createTitledBorder("Output");
		Border outerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 20);
		this.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		setLayout(new BorderLayout());

		add(new JScrollPane(textArea), BorderLayout.CENTER);

	}

	public void appendText(String text) {
		textArea.append(text);
	}

}
