package com.theothermattm.goal.domain.qe.lib;

import static com.jayway.restassured.RestAssured.given;

import org.apache.log4j.Logger;

import com.jayway.restassured.response.Response;

import net.sf.json.JSONObject;

/**
 * Class to execute REST API
 * @author lawkp
 *
 */
public class RestOperations implements GoalMethods {
	
	private String testURL;
	private Logger logger = Logger.getLogger(RestOperations.class.getName());
	
	public RestOperations() {
	}
	
	public RestOperations(String testURL) {
		this.setTestURL(testURL);
	}
	
	public String getTestURL() {
		return testURL;
	}

	public void setTestURL(String testURL) {
		this.testURL = testURL;
	}	

	/**
	 * Execute a post requeset
	 * @param goalJsonObject  - json object representing a GOAL
	 * @return
	 */
	public Response post(JSONObject goalJsonObject) {
		String jsonString = goalJsonObject.toString();
		logger.debug("Post json Object -  " + jsonString);
		com.jayway.restassured.response.Response response = null;
		response = given().contentType("application/json\r\n").body(goalJsonObject.toString()).post("http://" + this.getTestURL() + "/" + SERVICES + "/" + ADD_GOAL );
		return response;
	}
	
	/**
	 * Execute a get request
	 * @param url url for the get method
	 * @return
	 */
	public Response get(String url) {
		logger.debug("test url =  " + url);
		com.jayway.restassured.response.Response response = null;
		try {
			response = given().get(url);	
		}catch(Exception e) {
			logger.error("Failed to get respone for request - " + url);
			logger.error("Reason  : " + e);
		}
		return response;
	}
}
