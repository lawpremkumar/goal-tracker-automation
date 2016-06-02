package com.theothermattm.goal.domain.qe.lib;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

public class Goal {
	private String id;
	private String name;
	private Date dueDate;
	private String notes;
	private int weight;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getDueDate() {
		return dueDate;
	}
	
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + weight;
		return result;
	}
	
	public JSONObject toJson() {
		JSONObject jsonObject = new JSONObject();
		if (this.getId() != null) {
			jsonObject.put("id", this.getId());
		}
		if( this.getName() != null) {
			jsonObject.put("name", this.getName());
		}
		if (this.getNotes() != null) {
			jsonObject.put("notes", this.getNotes());
		}
		if (this.getDueDate() != null) {
			jsonObject.put("dueDate",this.getDueDate().getTime());
		}
		if ( this.getWeight() != 0) {
			jsonObject.put("weight", this.getWeight());
		}
		return jsonObject;
	}
	
	@Override
	public String toString() {
		return "Goal [ " + JSONObject.fromObject(this)  +" ]";
	}
}