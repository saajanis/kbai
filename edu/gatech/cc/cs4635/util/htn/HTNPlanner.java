package edu.gatech.cc.cs4635.util.htn;

import javolution.util.FastList;
import javolution.util.FastMap;

import edu.gatech.cc.cs4635.core.Application;
import edu.gatech.cc.cs4635.core.Internals;
import edu.gatech.cc.cs4635.lang.ActionFrame;
import edu.gatech.cc.cs4635.lang.Logbook;
import edu.gatech.cc.cs4635.lang.Plan;
import edu.gatech.cc.cs4635.lang.htn.Method;
import edu.gatech.cc.cs4635.lang.htn.Task;

public class HTNPlanner {
	
	private FastMap<Logbook, FastList<Method>> associations = new FastMap<Logbook, FastList<Method>>();
	
	public void associate(FastList<Logbook> chains) {
		for(Logbook l : chains) {
			associations.put(l, new FastList<Method>());
			for(Task t : Internals.HTNS.getHTNs()) {
				for(String m : t.internal().keySet()) {
					int hits=0;
					for(String p : t.get(m).internal().keySet()) {
						for(String f : l.entries()) {
							ActionFrame b = l.get(f);
							
							for(String e : t.get(m).get(p).entries()) {
								ActionFrame a = t.get(m).get(p).get(e);
								if(b.getAction().equals(a.getAction())) {
									hits++;
								}
							}
						}
					}
					if(hits > Application.sensitivity) {
						associations.get(l).add(t.get(m));
					}
				}
			}
		}
	}
	
	public void displayAssociations() {
		for(Logbook l : associations.keySet()) {
			System.out.println("<<Chain>>");
			l.debug();
			System.out.println("Possible Plans: ");
			for(Method m : associations.get(l)) {
				System.out.print(m.getSuperGoal().getAction() + "->" + m.getGoal().getAction() + "->");
				String output = "";
				for(String p : m.internal().keySet()) {
					output += m.get(p).getGoal().getAction() + ",";
				}
				System.out.print(output.substring(0, output.length()-1) + "\n");
			}
			System.out.print("\n");
		}
	}
	
	public FastMap<Logbook, FastList<Method>> getAssociations() {
		return associations;
	}

}
