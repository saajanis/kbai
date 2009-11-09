package edu.gatech.cc.cs4635.lang.htn;

import javolution.util.FastMap;
import edu.gatech.cc.cs4635.lang.ActionFrame;
import edu.gatech.cc.cs4635.lang.htn.Plan;

public class Method {
	
	private FastMap<String, Plan> storage = new FastMap<String, Plan>();
	
	public Plan get(String key) {
		return storage.get(key);
	}
	
	public void add(String key, Plan plan) {
		storage.put(key, plan);
	}
	
	public Plan remove(String key) {
		return storage.remove(key);
	}
	
	public FastMap<String, Plan> internal() {
		return storage;
	}
	
	private ActionFrame goal;
	private ActionFrame superGoal;
	
	public Method(ActionFrame frame) {
		setGoal(frame);
	}
	
	public void setSuperGoal(ActionFrame frame) {
		superGoal = frame;
	}
	
	public ActionFrame getSuperGoal() {
		return superGoal;
	}

	public void setGoal(ActionFrame frame) {
		goal = frame;
	}
	
	public ActionFrame getGoal() {
		return goal;
	}

	public void verbose() {
		System.out.println("  * " + goal.getHeader());
		for(String key : storage.keySet()) {
			storage.get(key).verbose();
		}
	}
}
