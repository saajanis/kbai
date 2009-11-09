package edu.gatech.cc.cs4635.lang.htn;

import edu.gatech.cc.cs4635.lang.ActionFrame;
import edu.gatech.cc.cs4635.lang.base.Storage;

public class Plan extends Storage<ActionFrame> {
	
	private ActionFrame goal;
	
	public Plan(ActionFrame frame) {
		setGoal(frame);
	}

	public void setGoal(ActionFrame frame) {
		goal = frame;
	}
	
	public ActionFrame getGoal() {
		return goal;
	}

	public void verbose() {
		System.out.println("    * " + goal.getHeader());
		for(String key : internal().keySet()) {
			System.out.print("      ");
			internal().get(key).debug();
			System.out.print("\n");
		}
	}
	
}
