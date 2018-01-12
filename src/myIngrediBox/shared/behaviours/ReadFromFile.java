package myIngrediBox.shared.behaviours;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import jade.core.behaviours.OneShotBehaviour;

public class ReadFromFile extends OneShotBehaviour {

	private String path;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReadFromFile(String path) {
		super();
		this.path = path;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader(this.path));

			JSONObject jsonObject = (JSONObject) obj;

			this.getDataStore().put("rawData", jsonObject);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
