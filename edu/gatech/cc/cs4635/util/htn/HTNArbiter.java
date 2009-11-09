package edu.gatech.cc.cs4635.util.htn;

import javolution.util.FastList;
import javolution.util.FastMap;
import edu.gatech.cc.cs4635.lang.ActionFrame;
import edu.gatech.cc.cs4635.lang.ActionFrameSlot;
import edu.gatech.cc.cs4635.lang.Logbook;
import edu.gatech.cc.cs4635.lang.htn.Method;

public class HTNArbiter {

	public void evaluate(FastMap<Logbook, FastList<Method>> associations) {
		for(Logbook l : associations.keySet()) {
			FastList<Integer> hits = new FastList<Integer>(associations.get(l).size());
			for(Method m : associations.get(l)) {
				Integer hit = 0;
				for(String p : m.internal().keySet()) {
					for(String f : l.entries()) {
						ActionFrame b = l.get(f);
					
						for(String e : m.get(p).entries()) {
							ActionFrame a = m.get(p).get(e);
							if(b.getAction().equals(a.getAction())) {
								hit++;
							}
						}
					}
				}
				hits.add(hit);
			}
			System.out.print("\n"); // newline
			System.out.println("<<Chain>>");
			l.debug();
			System.out.println("<<Plan>>");
			System.out.println("* " + associations.get(l).get(indexOfMax(hits)).getSuperGoal().getAction());
			associations.get(l).get(indexOfMax(hits)).verbose();
			System.out.print("Goal: ");
			if(associations.get(l).size() > 1) {
				System.out.print(findActor(l) + " most likely plan(s) to " + associations.get(l).get(indexOfMax(hits)).getSuperGoal().getAction() + " by ");
			} else {
				System.out.print(findActor(l) + " plan(s) to " + associations.get(l).get(indexOfMax(hits)).getSuperGoal().getAction() + " by ");
			}
			System.out.print(associations.get(l).get(indexOfMax(hits)).getGoal().getAction() + ". ");
			if(hits.get(indexOfMax(hits)) < 2) {
				System.out.println(" (Weak)");
			} else if (hits.get(indexOfMax(hits)) < 3) {
				System.out.println(" (Medium)");
			} else {
				System.out.println(" (Strong)");
			}
			System.out.print("Planned steps: ");
			String output = "";
			for(String p : associations.get(l).get(indexOfMax(hits)).internal().keySet()) {
				output += associations.get(l).get(indexOfMax(hits)).get(p).getGoal().getAction() + ", ";
			}
			System.out.print(output.substring(0, output.length()-2) + "\n");
		}
	}
	
	public String findActor(Logbook l) {
		String actor = null;
		
		FastList<String> agents = new FastList<String>();
		for(String e : l.entries()) {
			ActionFrame a = l.get(e);
			if(a.getFiller(ActionFrameSlot.AGENT) != null && !a.getFiller(ActionFrameSlot.AGENT).getHeader().equals("")) {
				if(!agents.contains(a.getFiller(ActionFrameSlot.AGENT).getHeader()))
					agents.add(a.getFiller(ActionFrameSlot.AGENT).getHeader());
			}
		}
		
		int[] count = new int[agents.size()];
		
		for(int i = 0; i<count.length; i++) {
			count[i] = 0;
		}
		
		for(String e : l.entries()) {
			ActionFrame a = l.get(e);
			if(a.getFiller(ActionFrameSlot.AGENT) != null && !a.getFiller(ActionFrameSlot.AGENT).getHeader().equals("")) {
				count[agents.indexOf(a.getFiller(ActionFrameSlot.AGENT).getHeader())]++;
			}
		}
		
		FastList<Integer> hits = new FastList<Integer>(count.length);
		for(int i = 0; i<count.length; i++) {
			hits.add(new Integer(count[i]));
		}
		
		actor = agents.get(indexOfMax(hits));
		
		return actor;
	}
	
	public int indexOfMax(FastList<Integer> list) {
		int max_index = 0, max = 0, index = 0;
		for(Integer i : list) {
			if(i > max) {
				max_index = index;
				max = i;
			}
			index++;
		}
		return max_index;
	}
	
}
