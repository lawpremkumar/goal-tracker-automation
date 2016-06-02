package com.theothermattm.goal.domain.qe.tests;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.theothermattm.goal.domain.qe.lib.BaseTest;
import com.theothermattm.goal.domain.qe.lib.Goal;
import com.theothermattm.goal.domain.qe.lib.GoalBuilder;
import com.theothermattm.goal.domain.qe.lib.GoalMethods;
import com.theothermattm.goal.domain.qe.lib.RestOperations;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Test Scenario : Test goals api
 * @author lawkp
 *
 */
public class TestGetGoals extends BaseTest {  

	private String hostName;
	private Logger logger = Logger.getLogger(TestGetGoals.class.getName());

	public TestGetGoals() {
		super();
	}

	@Parameters("host")
	@BeforeClass
	public void setUp(String hostName) {
		this.setHostName(hostName);
		
		// Initially check whether there is any goals, if there is no any goals create one for testing.
		if (this.getGoalCount() == 0 ){
			createGoal();
		}
	}
	
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * Test Scenario : Verify whether goal contains all the fields.
	 */
	@Test
	public void testForAllFields() {
		RestOperations restOperationsObject = new RestOperations();
		com.jayway.restassured.response.Response getResponse = restOperationsObject.get("http://" + this.getHostName() + "/" + this.SERVICES + "/"  + GoalMethods.GET_GOALS);
		JSONArray jsonArray = this.convertGoalResponseToJsonArray(getResponse);
		if (jsonArray.size() > 0 ){
			JSONObject goalJSONObject =  (JSONObject)jsonArray.get(0);
			org.testng.Assert.assertTrue(goalJSONObject.containsKey("id") == true, "Expected that id field exists in goal.");
			org.testng.Assert.assertTrue(goalJSONObject.containsKey("name") == true, "Expected that name field exists in goal.");
			org.testng.Assert.assertTrue(goalJSONObject.containsKey("dueDate") == true, "Expected that dueDate field exists in goal.");
			org.testng.Assert.assertTrue(goalJSONObject.containsKey("notes") == true, "Expected that notes field exists in goal.");
			org.testng.Assert.assertTrue(goalJSONObject.containsKey("weight") == true, "Expected that weight field exists in goal.");
		}
	}
}
