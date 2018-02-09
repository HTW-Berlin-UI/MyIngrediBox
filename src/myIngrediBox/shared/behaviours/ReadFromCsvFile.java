package myIngrediBox.shared.behaviours;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import jade.core.behaviours.OneShotBehaviour;

public class ReadFromCsvFile extends OneShotBehaviour {

	private String path;

	private static final long serialVersionUID = 1L;

	//difference to readfromfile is, that it reads directly from jsonarray
	public ReadFromCsvFile(String path) {
		super();
		this.path = path;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader(this.path));

			JSONArray jsonArray = (JSONArray) obj;

			this.getDataStore().put("rawData", jsonArray);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}