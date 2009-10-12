package edu.gatech.cc.cs4635.lang;

import javolution.util.FastList;

public class PlanPool {

	private FastList<Plan> plans = new FastList<Plan>();
	
	public void addPlan(Plan plan) {
		plans.add(plan);
	}
	
	public FastList<Plan> getPlans() {
		return plans;
	}
}
