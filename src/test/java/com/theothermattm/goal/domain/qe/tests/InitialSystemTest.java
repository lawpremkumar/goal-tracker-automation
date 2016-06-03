package com.theothermattm.goal.domain.qe.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.theothermattm.goal.domain.qe.lib.BaseTest;
import com.theothermattm.goal.domain.qe.lib.RestOperations;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/*
 * Test Scenario : Verify whether there is no goals exists when the system deployed i,e clean deployment.
 */
public class InitialSystemTest extends BaseTest {
	
	private RestOperations restOperationsObject;
	
	public InitialSystemTest() {
		super();
		System.out.println("InitialSystemTest..");
	}
	
	@BeforeClass
	public void setUp() {
		System.out.println("InitialSystemTest  before class setup");
		restOperationsObject = new RestOperations(this.getHostName());
	}
	
	@Test
	public void testInitialGoals() {
		com.jayway.restassured.response.Response response = this.restOperationsObject.get("http://" + this.getHostName() + "/" + this.SERVICES + "/"  + this.GET_GOALS);
		System.out.println(" response code "+response.getStatusCode());
		JSONArray responseJsonArray = this.convertGoalResponseToJsonArray(response);
		org.testng.Assert.assertTrue(response.getStatusCode() == HTTP_SUCCESS, "Expected that we get 200, but got " +response.getStatusCode()  );
		org.testng.Assert.assertTrue(responseJsonArray.size() >= 0, "Expected that initially we get empty goals, but got " + responseJsonArray.toString());
	}

	@AfterClass
	public void tearDown() {
		restOperationsObject = null;
	}
}
