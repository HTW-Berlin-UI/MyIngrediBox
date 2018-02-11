package myIngrediBox.gui;
import java.util.EventListener;

public interface IngredientListener extends EventListener {
	public void ingredientEventOccured(IngredientEvent e);
}
