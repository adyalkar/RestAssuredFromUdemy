package _Files_;

import io.restassured.path.json.JsonPath;

public class _2_ReusableMethods {
	
	public static JsonPath rawTojson(String response) {
		JsonPath js = new JsonPath(response);
		return js;
	}
}
