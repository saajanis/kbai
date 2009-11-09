package edu.gatech.cc.cs4635.util.htn;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


import javolution.util.FastList;
import edu.gatech.cc.cs4635.core.Internals;
import edu.gatech.cc.cs4635.lang.ActionFrame;
import edu.gatech.cc.cs4635.lang.ActionFrameSlot;
import edu.gatech.cc.cs4635.lang.AgentFrame;
import edu.gatech.cc.cs4635.lang.htn.Plan;
import edu.gatech.cc.cs4635.lang.htn.Method;
import edu.gatech.cc.cs4635.lang.htn.Task;

public class HTNParser {
	
	//FastList<Task> networks = new FastList<Task>();
	
	Task currentTask = null;
	Method currentMethod = null;
	Plan currentPlan = null;

	public void generate() {		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/htn.txt"));
			//BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/" + filename, true));
			
			while(br.ready()) {
				parse(br.readLine());
				
				//bw.write(line);
				//bw.flush();
			}
			
			currentMethod.add(currentPlan.getGoal().getID(), currentPlan);
			currentTask.add(currentMethod.getGoal().getID(), currentMethod);
			Internals.HTNS.addHTN(currentTask); //networks.add(currentTask);
			
			//Internals.PLANPOOL.addPlan(current);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parse(String line) {
		StringTokenizer st = new StringTokenizer(line, ",");
		FastList<String> tokens = new FastList<String>();
		while(st.hasMoreTokens()) {
			tokens.add(st.nextToken().trim());
		}
		
//		System.out.println(line);
//		System.out.println("length: " + tokens.size());
		
		switch(tokens.size()) {
		case 2:	
			if(currentTask != null) {
				currentMethod.add(currentPlan.getGoal().getID(), currentPlan);
				currentTask.add(currentMethod.getGoal().getID(), currentMethod);
				Internals.HTNS.addHTN(currentTask);
				currentMethod = null;
				currentPlan = null;
			}
			currentTask = new Task(new ActionFrame(tokens.get(0), tokens.get(1)));
			break;
		case 3:	
			if(currentMethod != null) {
				currentMethod.add(currentPlan.getGoal().getID(), currentPlan);
				currentTask.add(currentMethod.getGoal().getID(), currentMethod);
				currentPlan = null;
			}
			currentMethod = new Method(new ActionFrame(tokens.get(1), tokens.get(2)));
			currentMethod.setSuperGoal(currentTask.getGoal());
			break;
		case 4:
			if(currentPlan != null) {
				currentMethod.add(currentPlan.getGoal().getID(), currentPlan);
			}
			currentPlan = new Plan(new ActionFrame(tokens.get(2), tokens.get(3)));
			break;
		case 5: 
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
			
			currentPlan.add(action.getID(), action);
			break;
		default: System.err.println("Malformed entry.");
		}
		
	}	
}
