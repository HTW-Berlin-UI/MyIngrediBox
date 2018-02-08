package myIngrediBox.agents.ingrediBuyer;

public class BuyingControllerFactory {

	private static BuyingControllerFactory instance = new BuyingControllerFactory();

	public static BuyingControllerFactory getInstance() {
		return instance;
	}

	public BuyingController createBuyingControllerFor(BuyingPreference preference) {

		switch (preference) {
		case CHEAPEST:
			return new BuyCheapest();
		default:
			throw new IllegalArgumentException("What exactly do you want to buy?");
		}

	}

}
