package myIngrediBox.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import myIngrediBox.ontologies.BuyingPreference;

public class ActionBar extends JPanel implements ActionListener {

	private JButton submitButton;
	private SubmitListener submitListener;
	private JRadioButton buyLowLeftoversRadio;
	private JRadioButton buyChepeastRadio;
	private ButtonGroup buyingPreferenceGroup;

	public ActionBar() {

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		submitButton = new JButton("Start Shopping");
		submitButton.addActionListener(this);

		buyLowLeftoversRadio = new JRadioButton("Reduce Leftovers");
		buyChepeastRadio = new JRadioButton("Get cheapest");
		buyLowLeftoversRadio.setActionCommand(BuyingPreference.LOW_LEFTOVERS.toString());
		buyChepeastRadio.setActionCommand(BuyingPreference.CHEAPEST.toString());

		buyChepeastRadio.setSelected(true);

		buyingPreferenceGroup = new ButtonGroup();

		buyingPreferenceGroup.add(buyLowLeftoversRadio);
		buyingPreferenceGroup.add(buyChepeastRadio);

		setLayout(new FlowLayout(FlowLayout.RIGHT));
		add(buyChepeastRadio);
		add(buyLowLeftoversRadio);
		add(submitButton);

	}

	public void setSubmitListener(SubmitListener listener) {
		this.submitListener = listener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (this.submitListener != null)
			this.submitListener
					.submit(BuyingPreference.valueOf(this.buyingPreferenceGroup.getSelection().getActionCommand()));

	}

}
