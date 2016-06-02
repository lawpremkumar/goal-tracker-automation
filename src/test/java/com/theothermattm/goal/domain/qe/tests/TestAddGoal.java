package com.theothermattm.goal.domain.qe.tests;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.theothermattm.goal.domain.qe.lib.BaseTest;
import com.theothermattm.goal.domain.qe.lib.Goal;
import com.theothermattm.goal.domain.qe.lib.GoalBuilder;
import com.theothermattm.goal.domain.qe.lib.RestOperations;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Test Case : Test addGoal. After creating a goal successfully, user is redirected to goals api.
 * @author lawkp
 *
 */
public class TestAddGoal  extends BaseTest {
	
	private RestOperations restOperationsObject;
	private String GET_GOAL_URL;
	public static final int INTERNAL_SERVER_ERROR = 500;
	private Logger logger = Logger.getLogger(TestAddGoal.class.getName());
	private String hostName;
	
	public TestAddGoal() {
		super();
		 this.restOperationsObject = new RestOperations(this.getHostName());
		 
	}
	
	@Parameters("host")
	@BeforeClass
	public void setUp(String hostName) {
		this.setHostName(hostName);
		this. restOperationsObject = new RestOperations(this.getHostName());
	}
	
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * Test Scenarion : Verify whether goal is created with the specified name.
	 */
	@Test
	public void testCreateGoalWithSpecifiedName() {
		String goalName = this.getNameValue(50);
		Goal goal = new GoalBuilder().name(goalName).build(); 
		JSONObject goalJsonObject = goal.toJson();
		com.jayway.restassured.response.Response postResponse = restOperationsObject.post(goalJsonObject);
		logger.debug("response code - " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(postResponse.getStatusCode() == HTTP_FOUND, "Expected that we dn't get " + HTTP_FOUND + "  , but got " + postResponse.getStatusCode());
		boolean flag = checkIfValueExists(postResponse,"name" , goalName);
		org.testng.Assert.assertTrue(flag , goalName + " dn't exists in the response ");
	}
	
	/**
	 * Test Scenario : Verify whether goal  is created with a big name.
	 * Bug : Name should have a limit or return a right error message, example name length should be 200. 
	 */
	@Test
	public void testCreateGoalWithBigName() {
		String goalName = this.getNameValue(255);
		logger.debug("goalName  = " + goalName);
		Goal goal = new GoalBuilder().name(goalName).build(); 
		JSONObject goalJsonObject = goal.toJson();
		com.jayway.restassured.response.Response postResponse = restOperationsObject.post(goalJsonObject);
		logger.debug("response code - " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(postResponse.getStatusCode() == HTTP_FOUND, "Expected that we dn't get " + HTTP_FOUND + "  , but got " + postResponse.getStatusCode());
		boolean flag = checkIfValueExists(postResponse,"name" , goalName);
		org.testng.Assert.assertTrue(flag , goalName + " dn't exists in the response ");
	}
	
	/**
	 * Test Scenario : Verify whether goal is created with notes
	 * Expected Result : goal should be created
	 */
	@Test
	public void testCreatingGoalWithNotesValue() {
		String notesValue = this.getNotesValue(20);
		Goal goal = new GoalBuilder().notes(notesValue).build(); 
		JSONObject goalJsonObject = goal.toJson();
		com.jayway.restassured.response.Response postResponse = restOperationsObject.post(goalJsonObject);
		logger.debug("response code - " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(postResponse.getStatusCode() == HTTP_FOUND, "Expected that we dn't get " + HTTP_FOUND + "  , but got " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(checkIfValueExists(postResponse,"notes" , notesValue) , notesValue + " dn't exists in the response ");
	}
	
	/**
	 * Test Scenario :  Verify whether goal is created when id is specified.
	 * Expected Result : Since id is system generated, trying to create a goal with id should fail.
	 */
	@Test
	public void testCreatingGoalWithId() {
		String idValue = this.getIdValue(10);
		String name = this.getNameValue(10);
		logger.debug("idValue = " + idValue  +"   name = " + name);
		Goal goal = new GoalBuilder().id(idValue).name(name).build(); 
		JSONObject goalJsonObject = goal.toJson();
		logger.debug("goalJsonObject  - " + goalJsonObject.toString());
		com.jayway.restassured.response.Response postResponse = restOperationsObject.post(goalJsonObject);
		logger.debug("response code - " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(postResponse.getStatusCode() == INTERNAL_SERVER_ERROR, "Expected that we dn't get " + INTERNAL_SERVER_ERROR + "  , but got " + postResponse.getStatusCode());		
	}
	
	/**
	 * Test Scenario : Verify whether goal can be created when name and notes values are set.
	 * Expected Result : Goal should be created.
	 */
	@Test
	public void testCreatingGoalWithNameAndNotes() {
		String nameValue = this.getNameValue(10);
		String notesValue = this.getNotesValue(50);
		Goal goal = new GoalBuilder().name(nameValue).notes(notesValue).build();
		JSONObject goalJsonObject = goal.toJson();
		logger.debug("goalJsonObject  - " + goalJsonObject.toString());
		com.jayway.restassured.response.Response postResponse = restOperationsObject.post(goalJsonObject);
		logger.debug("response code - " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(postResponse.getStatusCode() == HTTP_FOUND, "Expected that we dn't get " + HTTP_FOUND + "  , but got " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(checkIfValueExists(postResponse,"name" , nameValue) , nameValue + " dn't exists in the response ");
		org.testng.Assert.assertTrue(checkIfValueExists(postResponse,"notes" , notesValue) , notesValue + " dn't exists in the response ");
	}
	
	/**
	 * Test Scenario : Verify whether goal is not created when id and notes values are set.
	 * Expected Result : Since id is system generated, trying to create a goal with id should fail.
	 */
	@Test
	public void testCreatingGoalWithIdAndNotes() {
		String idValue = this.getIdValue(10);
		String notesValue = this.getNotesValue(50);
		logger.debug("idValue = " + idValue  +"   notesValue = " + notesValue);
		Goal goal = new GoalBuilder().id(idValue).notes(notesValue).build(); 
		JSONObject goalJsonObject = goal.toJson();
		logger.debug("goalJsonObject  - " + goalJsonObject.toString());
		com.jayway.restassured.response.Response postResponse = restOperationsObject.post(goalJsonObject);
		logger.debug("response code - " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(postResponse.getStatusCode() == INTERNAL_SERVER_ERROR, "Expected that we dn't get " + INTERNAL_SERVER_ERROR + "  , but got " + postResponse.getStatusCode());
	}
	
	/**
	 * Test Scenario : Verify whether goal is created when name and wight are specified
	 * Expected Result : Goal should be created.
	 * 
	 */
	@Test
	public void testCreatingGoalWithNameAndWeight() {
		String nameValue = this.getNameValue(10);
		int weightValue = 5;
		Goal goal = new GoalBuilder().name(nameValue).weight(weightValue).build();
		JSONObject goalJsonObject = goal.toJson();
		logger.debug("goalJsonObject  - " + goalJsonObject.toString());
		com.jayway.restassured.response.Response postResponse = restOperationsObject.post(goalJsonObject);
		logger.debug("response code - " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(postResponse.getStatusCode() == HTTP_FOUND, "Expected that we dn't get " + HTTP_FOUND + "  , but got " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(checkIfValueExists(postResponse,"name" , nameValue) , nameValue + " dn't exists in the response ");
		org.testng.Assert.assertTrue(checkIfValueExists(postResponse,"weight" , weightValue) , weightValue + " dn't exists in the response ");
	}
	
	/**
	 * Test Scenario : Verify whethre goal is created when duedate and weight are specified.
	 * Expected Result : Goal should be created.
	 */
	@Test
	public void testCreatingGoalWithDateAndWeight() {
		int weightValue = 5;
		Date dt = new Date();
		Goal goal = new GoalBuilder().weight(weightValue).dueDate(dt).build();
		JSONObject goalJsonObject = goal.toJson();
		logger.debug("goalJsonObject  - " + goalJsonObject.toString());
		com.jayway.restassured.response.Response postResponse = restOperationsObject.post(goalJsonObject);
		logger.debug("response code - " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(postResponse.getStatusCode() == HTTP_FOUND, "Expected that we dn't get " + HTTP_FOUND + "  , but got " + postResponse.getStatusCode());
		String time = toString().valueOf(dt.getTime());
		org.testng.Assert.assertTrue(checkIfValueExists(postResponse,"dueDate" , dt.getTime()) , dt.getTime() + " dn't exists in the response ");
		org.testng.Assert.assertTrue(checkIfValueExists(postResponse,"weight" , weightValue) , weightValue + " dn't exists in the response ");
	}
	
	/**
	 * Test Scenario : Verify goal is not created when id and weight are specified.
	 * Expected Result : Since id is system generated, trying to create a goal with id should fail.
	 */
	@Test
	public void testCreatingGoalWithIdAndWeight() {
		 
		String idValue = this.getIdValue(10);
		int weightValue = 10;
		Date dt = new Date();
		Goal goal = new GoalBuilder().weight(weightValue).id(idValue).build();
		JSONObject goalJsonObject = goal.toJson();
		logger.debug("goalJsonObject  - " + goalJsonObject.toString());
		com.jayway.restassured.response.Response postResponse = restOperationsObject.post(goalJsonObject);
		logger.debug("response code - " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(postResponse.getStatusCode() == INTERNAL_SERVER_ERROR, "Expected that we dn't get " + INTERNAL_SERVER_ERROR + "  , but got " + postResponse.getStatusCode());
	}
	
	/**
	 * Test Scenario : Verify whether goal is created when duedate and notes are specified
	 * Expected Result : Goal should be created.
	 */
	@Test
	public void testCreatingGoalWithDateAndNotes() {
		Date dt = new Date();
		String notesValue = this.getNotesValue(100);
		Goal goal = new GoalBuilder().dueDate(dt).notes(notesValue).build();
		JSONObject goalJsonObject = goal.toJson();
		logger.debug("goalJsonObject  - " + goalJsonObject.toString());
		com.jayway.restassured.response.Response postResponse = this.restOperationsObject.post(goalJsonObject);
		logger.debug("response code - " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(postResponse.getStatusCode() == HTTP_FOUND, "Expected that we dn't get " + HTTP_FOUND + "  , but got " + postResponse.getStatusCode());
		String time = toString().valueOf(dt.getTime());
		org.testng.Assert.assertTrue(checkIfValueExists(postResponse,"dueDate" , dt.getTime()) , dt.getTime() + " dn't exists in the response ");
		org.testng.Assert.assertTrue(checkIfValueExists(postResponse,"notes" , notesValue) , notesValue + " dn't exists in the response ");
	}
	
	/**
	 * Test Scenario : Verify whether goal is not created when id and due date are specified
	 * Expected Result : Since id is system generated, trying to create a goal with id should fail.
	 */
	@Test
	public void testCreatingGoalWithIdAndDate() {
		Date dt = new Date();
		String idValue = this.getIdValue(10);
		Goal goal = new GoalBuilder().dueDate(dt).id(idValue).build();
		JSONObject goalJsonObject = goal.toJson();
		logger.debug("goalJsonObject  - " + goalJsonObject.toString());
		com.jayway.restassured.response.Response postResponse = this.restOperationsObject.post(goalJsonObject);
		logger.debug("response code - " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(postResponse.getStatusCode() == INTERNAL_SERVER_ERROR, "Expected that we dn't get " + INTERNAL_SERVER_ERROR + "  , but got " + postResponse.getStatusCode());
	}
	
	/**
	 * Test Scenario :  Verify whether goal is created when id and name is specified
	 * Expected Result : Since id is system generated, trying to create a goal with id should fail.
	 */
	//@Test
	public void testCreatingGoalWithIdAndName() {
		String idValue = this.getIdValue(10);
		String nameValue = this.getNameValue(20);
		Goal goal = new GoalBuilder().id(idValue).name(nameValue).build();
		JSONObject goalJsonObject = goal.toJson();
		logger.debug("goalJsonObject  - " + goalJsonObject.toString());
		com.jayway.restassured.response.Response postResponse = this.restOperationsObject.post(goalJsonObject);
		logger.debug("response code - " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(postResponse.getStatusCode() == INTERNAL_SERVER_ERROR, "Expected that we dn't get " + INTERNAL_SERVER_ERROR + "  , but got " + postResponse.getStatusCode());
	}
	
	/**
	 * Test Scenario : Verify whether goal is created when notes and weight are specified.
	 * Expected Result : Goal should be created.
	 */
	//@Test
	public void testCreateGoalWithNotesAndWeight() {
		String notesValue = this.getNotesValue(100);
		int weightValue = 20;
		Goal goal = new GoalBuilder().notes(notesValue).weight(weightValue).build();
		JSONObject goalJsonObject = goal.toJson();
		logger.debug("goalJsonObject  - " + goalJsonObject.toString());
		com.jayway.restassured.response.Response postResponse = restOperationsObject.post(goalJsonObject);
		logger.debug("response code - " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(postResponse.getStatusCode() == HTTP_FOUND, "Expected that we dn't get " + HTTP_FOUND + "  , but got " + postResponse.getStatusCode());
		org.testng.Assert.assertTrue(checkIfValueExists(postResponse,"notes" , notesValue) , notesValue + " dn't exists in the response ");
		org.testng.Assert.assertTrue(checkIfValueExists(postResponse,"weight" , weightValue) , weightValue + " dn't exists in the response ");
	}
	
	/**
	 * Test Scenario : Verify whether goal is created when name and duedate are specified.
	 * Expected Result : Goal should be created.
	 */
	@Test
	public void testCreateGoalWithNameAndDate() {
		Date dt = new Date();
		String nameValue = this.getNameValue(30);
		Goal goal = new GoalBuilder().dueDate(dt).name(nameValue).build();
		JSONObject goalJsonObject = goal.toJson();
		logger.debug("goalJsonObject  - " + goalJsonObject.toString());
		com.jayway.restassured.response.Response postResponse = restOperationsObject.post(goalJsonObject);
		logger.debug("response code - " + postResponse.getStatusCode());
		String time = toString().valueOf(dt.getTime());
		org.testng.Assert.assertTrue(checkIfValueExists(postResponse,"dueDate" , dt.getTime()) , dt.getTime() + " dn't exists in the response ");
		org.testng.Assert.assertTrue(checkIfValueExists(postResponse,"name" , nameValue) , nameValue + " dn't exists in the response ");
	}
}
