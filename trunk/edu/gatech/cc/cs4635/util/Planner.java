package edu.gatech.cc.cs4635.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import javolution.util.FastList;
import javolution.util.FastMap;

import edu.gatech.cc.cs4635.core.Internals;
import edu.gatech.cc.cs4635.lang.ActionFrame;
import edu.gatech.cc.cs4635.lang.ActionFrameSlot;
import edu.gatech.cc.cs4635.lang.AgentFrame;
import edu.gatech.cc.cs4635.lang.Logbook;
import edu.gatech.cc.cs4635.lang.Plan;

// Input: causal chains
// Output: causal chains with candidate plans

public class Planner {
	
	private Plan current = new Plan(null);
	private boolean initial = true;
	private FastMap<Logbook, FastList<Plan>> associations = new FastMap<Logbook, FastList<Plan>>();

	
	public void generate(String filename) {		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/plans.txt"));
			//BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/" + filename, true));
			
			while(br.ready()) {
				parse(br.readLine());
				
				//bw.write(line);
				//bw.flush();
			}
			
			Internals.PLANPOOL.addPlan(current);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void parse(String line) {
		StringTokenizer st = new StringTokenizer(line, ",");
		FastList<String> tokens = new FastList<String>();
		while(st.hasMoreTokens()) {
			tokens.add(st.nextToken());
		}
		
		try {
			//System.err.println("What?");
			//System.err.println(line);
			
			Integer id = new Integer(tokens.get(0));
			
			//System.err.println(line);
			//System.err.println(id);
			
			if(initial) {
				current.setGoal(new ActionFrame(tokens.get(0), tokens.get(1)));
				initial = false;
			} else {
				//System.err.println("Adding a plan.");
				Internals.PLANPOOL.addPlan(current);
				current = new Plan(new ActionFrame(tokens.get(0), tokens.get(1)));
			}
		} catch (NumberFormatException e) {
			line = line.substring(1, line.length()-1);
			
			String[] split = line.split("\\s\\(");
			String id = split[0].trim();
			
			split = line.split("^[0-9]{1,3}\\s");
			line = split[split.length-1];
			line = line.substring(1,line.length()-1);
			
			split = line.split("\\(.*\\)");
			ActionFrame action = new ActionFrame(id, split[0]);
			
			split = line.split("^[\\w-]*");
			line = split[split.length-1];
			line = line.substring(1, line.length()-1);
			
			st = new StringTokenizer(line, ",");
			tokens = new FastList<String>(5);
			while(st.hasMoreTokens()) {
				tokens.add(st.nextToken().trim());
			}
			
			String token = tokens.removeFirst().trim();
			action.addFiller(ActionFrameSlot.AGENT, new AgentFrame(token));
			
			token = tokens.removeFirst().trim();
			action.addFiller(ActionFrameSlot.OBJECT, new AgentFrame(token));
			
			token = tokens.removeFirst().trim();
			action.addFiller(ActionFrameSlot.LOCATION, new AgentFrame(token));
			
			token = tokens.removeFirst().trim();
			action.addFiller(ActionFrameSlot.COAGENT, new AgentFrame(token));
			
			token = tokens.removeFirst().trim();
			action.addFiller(ActionFrameSlot.TIME, new AgentFrame(token));
			
			current.add(action.getID(), action);
		}
		
	}
	
	public void debug() {
		//System.err.println(Internals.PLANPOOL.getPlans().size());
		for(Plan p : Internals.PLANPOOL.getPlans()) {
			p.verbose();
		}
	}
	
	public void associate(FastList<Logbook> chains) {
		
		for(Logbook l : chains) {
			associations.put(l, new FastList<Plan>());
			for(Plan p : Internals.PLANPOOL.getPlans()) {
				int hits = 0;
				for(String f : l.entries()) {
					ActionFrame b = l.get(f);
				
					for(String e : p.entries()) {
						ActionFrame a = p.get(e);
						if(b.getAction().equals(a.getAction())) {
							hits++;
						}
					}
				}
				if(hits > 0) {
					associations.get(l).add(p);
				}
			}
		}
	}
	
	public FastMap<Logbook, FastList<Plan>> getAssociations() {
		return associations;
	}
	
	public void displayAssociations() {
		for(Logbook l : associations.keySet()) {
			System.out.println("<<Chain>>");
			l.debug();
			System.out.println("Possible Plans: ");
			for(Plan p : associations.get(l)) {
				System.out.println(p.getGoal().getAction());
			}
		}
	}
	
	
}
