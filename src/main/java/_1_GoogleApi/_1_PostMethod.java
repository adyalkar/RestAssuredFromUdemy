package _1_GoogleApi;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import org.testng.Assert;

import _Files_._1_PlayLoads;
import _Files_._2_ReusableMethods;


public class _1_PostMethod {

	public static void main(String[] args) {
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		String postResponse = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(_1_PlayLoads.addPlace())
		.when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
		
		System.out.println("postResponse Body : "+ postResponse );
		
		JsonPath js = new JsonPath(postResponse); // parsing the json
		String place_id =js.getString("place_id");
		
		System.out.println("place_id : "+ place_id );
		
		//Update place
		String newAddress = "Manish Nagar, Nagpur";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+place_id+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "")
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		// get updated Address and validate
		
		String getResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id",place_id)
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js1 = _2_ReusableMethods.rawTojson(getResponse);
		String actualAddress = js1.getString("address");
		System.out.println("actualAddress :"+actualAddress);
		
		// TestNg Validation
		Assert.assertEquals(actualAddress, newAddress);

	}

}
