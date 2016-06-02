package com.theothermattm.goal.domain.qe.tests;

import org.apache.log4j.Logger;

import com.theothermattm.goal.domain.qe.lib.BaseTest;
import com.theothermattm.goal.domain.qe.lib.GoalMethods;
import com.theothermattm.goal.domain.qe.lib.RestOperations;

/**
 * To check whether system is up or down, its a health checkup.
 * @author lawkp
 *
 */
public class TestSystemHealthCheckup implements GoalMethods {
	
	private RestOperations restOperationsObject;
	private String hostName;
	private Logger logger = Logger.getLogger(TestSystemHealthCheckup.class.getName());
	
	public TestSystemHealthCheckup(String hostName) {
		this.setHostName(hostName);
		this.restOperationsObject = new RestOperations();
	}
	
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * System Health checkup 
	 * @return true if system is up else return false
	 */
	public boolean isSystemTestable() {
		com.jayway.restassured.response.Response response = restOperationsObject.get("http://" + getHostName() + "/" + HEALTH_CHECKUP_METHOD_NAME );
		if (response != null) {
			logger.info("isSystemTestable..  " + response.getStatusCode());
			return true;
		} else 
			return false;
	}
}
