package myIngrediBox.agents.ingrediBuyer;

import myIngrediBox.ontologies.BuyingPreference;

public class BuyingControllerFactory {

	private static BuyingControllerFactory instance = new BuyingControllerFactory();

	public static BuyingControllerFactory getInstance() {
		return instance;
	}

	public BuyingController createBuyingControllerFor(BuyingPreference preference) {

		switch (preference) {
		case CHEAPEST:
			return new BuyCheapest();
		case LOW_LEFTOVERS:
			return new BuyLowLeftovers();
		default:
			return new BuyCheapest();
		}

	}

}
