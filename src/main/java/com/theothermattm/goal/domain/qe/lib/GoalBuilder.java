package com.theothermattm.goal.domain.qe.lib;

import java.util.Date;

public class GoalBuilder {
	
	private String id;
	private String name;
	private Date dueDate;
	private String notes;
	private int weight;
	
	public GoalBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	public GoalBuilder id(String id) {
		this.id = id;
		return this;
	}
	
	public GoalBuilder dueDate(Date dueDate) {
		this.dueDate = dueDate;
		return this;
	}
	
	public GoalBuilder notes(String notes) {
		this.notes = notes;
		return this;
	}
	
	public GoalBuilder weight(int weight) {
		this.weight = weight;
		return this;
	}
	
	public Goal build() {
		Goal goal = new Goal();
		goal.setId(id);
		goal.setName(name);
		goal.setDueDate(dueDate);
		goal.setNotes(notes);
		goal.setWeight(weight);
		return goal;
	}
}
