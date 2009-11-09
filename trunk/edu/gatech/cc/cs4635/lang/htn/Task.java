package edu.gatech.cc.cs4635.lang.htn;

import javolution.util.FastMap;
import edu.gatech.cc.cs4635.lang.ActionFrame;
import edu.gatech.cc.cs4635.lang.base.Storage;

public class Task {
	
	private FastMap<String, Method> storage = new FastMap<String, Method>();
	
	public Method get(String key) {
		return storage.get(key);
	}
	
	public void add(String key, Method value) {
		storage.put(key, value);
	}
	
	public Method remove(String key) {
		return storage.remove(key);
	}
	
	private ActionFrame goal;
	
	public Task(ActionFrame frame) {
		setGoal(frame);
	}

	public void setGoal(ActionFrame frame) {
		goal = frame;
	}
	
	public ActionFrame getGoal() {
		return goal;
	}

	public void verbose() {
		System.out.println("* " + goal.getHeader());
		debug();
	}
	
	public final void debug() {
		for(String key : storage.keySet()) {
			storage.get(key).verbose();
			//System.out.print("\n");
		}
		System.out.print("\n");
	}
}
