package edu.gatech.cc.cs4635.util;

import edu.gatech.cc.cs4635.lang.ActionFrame;
import edu.gatech.cc.cs4635.lang.ActionFrameSlot;
import edu.gatech.cc.cs4635.lang.Logbook;
import edu.gatech.cc.cs4635.lang.Plan;
import javolution.util.FastList;
import javolution.util.FastMap;

// INPUT: causal chain plus candidate plans
// OUTPUT: most likely plan and goal for each actor

public class Arbiter {
	
	public void evaluate(FastMap<Logbook, FastList<Plan>> associations) {
		for(Logbook l : associations.keySet()) {
			FastList<Integer> hits = new FastList<Integer>(associations.get(l).size());
			for(Plan p : associations.get(l)) {
				Integer hit = 0;
				for(String f : l.entries()) {
					ActionFrame b = l.get(f);
				
					for(String e : p.entries()) {
						ActionFrame a = p.get(e);
						if(b.getAction().equals(a.getAction())) {
							hit++;
						}
					}
				}
				hits.add(hit);
			}
			System.out.print("\n"); // newline
			System.out.println("<<Chain>>");
			l.debug();
			System.out.println("<<Plan>>");
			associations.get(l).get(indexOfMax(hits)).debug();
			System.out.print("Goal: ");
			if(associations.get(l).size() > 1) {
				System.out.println(findActor(l) + " most likely plan(s) to " + associations.get(l).get(indexOfMax(hits)).getGoal().getAction() + ".");
			} else {
				System.out.println(findActor(l) + " plan(s) to " + associations.get(l).get(indexOfMax(hits)).getGoal().getAction() + ".");
			}
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
