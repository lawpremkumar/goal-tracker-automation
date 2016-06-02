package com.theothermattm.goal.domain.qe.lib;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Headers;
import com.theothermattm.goal.domain.qe.tests.TestSystemHealthCheckup;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Base class for all testcase
 * @author lawkp
 *
 */
public class BaseTest implements GoalMethods{
	
	public String hostName;
	public static final int HTTP_SUCCESS = 200;
	public static final int HTTP_FOUND = 302;
	private Logger logger = Logger.getLogger(BaseTest.class.getName());
	
	public BaseTest() {
	}
	
	@Parameters("host")
	@BeforeSuite
	public void setUp(String hostName) {
		Validate.notNull("Name cannot be null", hostName);
		logger.info("BeforeSuite  host name = " + hostName);
		this.setHostName(hostName);
		
		TestSystemHealthCheckup testSystemHealthCheckupObj = new TestSystemHealthCheckup(getHostName());
		org.testng.Assert.assertTrue(testSystemHealthCheckupObj.isSystemTestable() == true, "Expected server is up, but looks like its down. Start your test, once server is up.");
	}
	
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	/*
	 * Convert Goal's response to JSONObject
	 */
	public JSONObject convertGoalResponseToJsonObject(com.jayway.restassured.response.Response response) {
		String strResponse = response.getBody().asString();
		logger.debug("strResponse = " + strResponse);
		JSONObject jsonObject =  (JSONObject) JSONSerializer.toJSON(strResponse.toString());
		return jsonObject;
	}
	
	/**
	 * Return the Goal, selected from 
	 * @param response
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public Goal getGoal(com.jayway.restassured.response.Response response , String fieldName , String fieldValue) {
		
		Headers headers = response.getHeaders();
		String redirectHeaderURL = headers.getValue("Location");
		logger.debug("redirectHeaderURL = " + redirectHeaderURL);
		RestOperations restOperationsObject = new RestOperations();
		com.jayway.restassured.response.Response getResponse = restOperationsObject.get(redirectHeaderURL);
		org.testng.Assert.assertTrue(getResponse.getStatusCode() == this.HTTP_SUCCESS, "Expected that we dn't get " + HTTP_SUCCESS + "  , but got " + getResponse.getStatusCode());
		
		Goal goal = null;
		boolean flag = false;
		JSONArray jsonArray = convertGoalResponseToJsonArray(getResponse);
		if (jsonArray.size() > 0) {
			Iterator iterator = jsonArray.iterator();
			while ( iterator.hasNext()) {
				JSONObject goalJsonObject = (JSONObject)iterator.next();
				if (goalJsonObject.containsKey(fieldName) ) {
					if (goalJsonObject.getString(fieldName).equals(fieldValue)) {
						goal = new Goal();
						goal.setId(goalJsonObject.getString("id"));
						goal.setName(goalJsonObject.getString("name"));
						goal.setNotes(goalJsonObject.getString("notes"));
						goal.setWeight(goalJsonObject.getInt("weight"));
						flag = true;
						break;
					}
				}
			}
		}
		return goal;
	}
	
	/**
	 * Convert Goal(s) response to JSONArray.
	 * @param response 
	 * @return
	 */
	public JSONArray convertGoalResponseToJsonArray(com.jayway.restassured.response.Response response) {
		String strResponse = response.getBody().asString();
		JSONArray jsonArray =  (JSONArray) JSONSerializer.toJSON(strResponse.toString());
		return jsonArray;
	}
	
	/**
	 * return a randomly generated string with the specified length
	 * @param length length of the string
	 * @return
	 */
	private String getFieldValue(int length) {
		String generatedString = RandomStringUtils.randomAlphabetic(length);
		return generatedString;
	}

	/**
	 *  Return a randomly generated string representing goal name.
	 * @param length length of the string
	 * @return
	 */
	public String getNameValue(int length) {
		return this.getFieldValue(length);
	}

	/**
	 *  Return a randomly generated string representing goal notes.
	 * @param length length of the string
	 * @return
	 */
	public String getNotesValue(int length) {
		return this.getFieldValue(length);
	}
	
	/**
	 *  Return a randomly generated string representing goal id.
	 * @param length length of the string
	 * @return
	 */
	public String getIdValue(int length) {
		return this.getFieldValue(length);
	}
	
	/**
	 * Return true of the specified field and its value matches in the given response.
	 * @param response
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public boolean checkIfValueExists(com.jayway.restassured.response.Response response , String fieldName , String fieldValue) {
		boolean flag = false;
		Headers headers = response.getHeaders();
		String redirectHeaderURL = headers.getValue("Location");
		logger.debug("redirectHeaderURL = " + redirectHeaderURL);
		 RestOperations restOperationsObject = new RestOperations();
		com.jayway.restassured.response.Response getResponse = restOperationsObject.get(redirectHeaderURL);
		org.testng.Assert.assertTrue(getResponse.getStatusCode() == this.HTTP_SUCCESS, "Expected that we dn't get " + HTTP_SUCCESS + "  , but got " + getResponse.getStatusCode());
		
		List<String> fieldList = getResponse.jsonPath().getList(fieldName);
		if (fieldList != null ) {
			for (String field : fieldList) {
				if (field != null) {
					if(field.equals(fieldValue)) {
						//System.out.println(fieldName  + " field & found its value  " + field);
						flag = true;
						break;
					}	
				}
				if (field == null && fieldValue == null) {
					flag = true;
				}
			}	
		}
		
		return flag;
	}
	
	/**
	 * Return true of the specified field and its value matches in the given response.
	 * @param response
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public boolean checkIfValueExists(com.jayway.restassured.response.Response response , String fieldName , Integer fieldValue) {
		
		boolean flag = false;
		Headers headers = response.getHeaders();
		String redirectHeaderURL = headers.getValue("Location");
		logger.debug("redirectHeaderURL = " + redirectHeaderURL);
		 RestOperations restOperationsObject = new RestOperations();
		com.jayway.restassured.response.Response getResponse = restOperationsObject.get(redirectHeaderURL);
		org.testng.Assert.assertTrue(getResponse.getStatusCode() == this.HTTP_SUCCESS, "Expected that we dn't get " + HTTP_SUCCESS + "  , but got " + getResponse.getStatusCode());
		
		List<Integer> fieldList = getResponse.jsonPath().getList(fieldName);
		if (fieldList != null ) {
			for (Integer field : fieldList) {
				if (field != null) {
					if(field.equals(fieldValue)) {
						//System.out.println(fieldName  + " field & found its value  " + field);
						flag = true;
						break;
					}	
				}
				if (field == null && fieldValue == null) {
					flag = true;
				}
			}	
		}
		return flag;
	}
	
	
	/**
	 * Return true of the specified field and its value matches in the given response.
	 * @param response
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public boolean checkIfValueExists(com.jayway.restassured.response.Response response , String fieldName , Long fieldValue) {
		
		boolean flag = false;
		Headers headers = response.getHeaders();
		String redirectHeaderURL = headers.getValue("Location");
		logger.debug("redirectHeaderURL = " + redirectHeaderURL);
		 RestOperations restOperationsObject = new RestOperations();
		com.jayway.restassured.response.Response getResponse = restOperationsObject.get(redirectHeaderURL);
		org.testng.Assert.assertTrue(getResponse.getStatusCode() == this.HTTP_SUCCESS, "Expected that we dn't get " + HTTP_SUCCESS + "  , but got " + getResponse.getStatusCode());
		
		List<Long> fieldList = getResponse.jsonPath().getList(fieldName);
		if (fieldList != null ) {
			for (Long field : fieldList) {
				if (field != null) {
					if(field.equals(fieldValue)) {
						//System.out.println(fieldName  + " field & found its value  " + field);
						flag = true;
						break;
					}	
				}
				if (field == null && fieldValue == null) {
					flag = true;
				}
			}	
		}
		return flag;
	}
	
	/**
	 * Get the number of goals 
	 * @param url
	 * @return
	 */
	public long getGoalCount() {
		String url = "http://" + this.getHostName() + "/" + this.SERVICES + "/"  + this.GET_GOALS;
		RestOperations restOperationsObject = new RestOperations();
		com.jayway.restassured.response.Response getResponse = restOperationsObject.get(url);
		List<String> goalList = getResponse.jsonPath().getList("id");
		long goalCount = goalList.stream().count();
		return goalCount;
	}
	
	/**
	 * Create a goal
	 * @return
	 */
	public boolean createGoal() {
		String nameValue = this.getNameValue(10);
		Goal goal = new GoalBuilder().name(nameValue).build();
		JSONObject goalJsonObject = goal.toJson();
		System.out.println("goalJsonObject  - " + goalJsonObject.toString());
		 RestOperations restOperationsObject = new RestOperations();
		com.jayway.restassured.response.Response postResponse = restOperationsObject.post(goalJsonObject);
		System.out.println("response code - " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(postResponse.getStatusCode() == HTTP_FOUND, "Expected that we dn't get " + HTTP_FOUND + "  , but got " + postResponse.getStatusCode());
		boolean isGoalCreated = checkIfValueExists(postResponse,"name" , nameValue);
		org.testng.Assert.assertTrue(isGoalCreated , nameValue + " dn't exists in the response ");
		return isGoalCreated;
	}
	
}
