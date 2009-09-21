package edu.gatech.cc.cs4635.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import javolution.util.FastList;

import edu.gatech.cc.cs4635.core.Internals;
import edu.gatech.cc.cs4635.lang.ActionFrame;
import edu.gatech.cc.cs4635.lang.ActionFrameSlots;
import edu.gatech.cc.cs4635.lang.AgentFrame;
import edu.gatech.cc.cs4635.lang.Frame;

public class Parser {
	
	private boolean isEvent = false;
	
	public void generate(String facts, String events) {
		System.err.print("Parser starting...");
		loadFile(facts);
		isEvent = true;
		loadFile(events);
		System.err.println("done.");
	}
	
	public void loadFile(String filename) {		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/" + filename));
			
			while(br.ready()) {
				String line = br.readLine();
				parse(line);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parse(String line) {
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
		
		StringTokenizer st = new StringTokenizer(line, ",");
		FastList<String> tokens = new FastList<String>(5);
		while(st.hasMoreTokens()) {
			tokens.add(st.nextToken().trim());
		}
		
		String token = tokens.removeFirst();
		split = token.split("\\(");
		if(split.length < 2) {
			addFiller(action, ActionFrameSlots.AGENT, token);
		} else {
			split = token.split("\\(");
			addFiller(action, ActionFrameSlots.AGENT, split[0]);
			split = token.split("^[\\w-]*");
			token = split[1].trim().substring(1, split[1].trim().length()-1);
			addFiller(action, ActionFrameSlots.OBJECT, token);
		}
		
		Internals.GLOSSARY.get(token).addFiller(action.getID(), action);
		
		token = tokens.removeFirst();
		if(!token.equals("")) {
			String[] result = token.split("\\(");
			
			if(result.length < 2) {
				result = token.split("\\s");
				if(result.length < 2) {
					addFiller(action, ActionFrameSlots.OBJECT, token);
				} else {
					StringTokenizer tokenizer = new StringTokenizer(token, " ");
					FastList<String> toks = new FastList<String>(2);
					while(tokenizer.hasMoreTokens()) {
						toks.add(tokenizer.nextToken());
					}
					
					addFiller(action, ActionFrameSlots.OBJECT, toks.removeFirst());
					addFiller(action, ActionFrameSlots.COOBJECT, toks.removeFirst());
					
					action.getFiller(ActionFrameSlots.COOBJECT).addFiller(action.getID(), action);
				}
			} else {
				if(isEvent) {
					result = token.split("\\s");
					if(result.length < 2) {
						result = token.split("\\(");
						addFiller(action, ActionFrameSlots.OBJECT, result[0]);
						result = token.split("^[\\w-]*");
						token = result[1].trim().substring(1, result[1].trim().length()-1);
						addFiller((AgentFrame) action.getFiller(ActionFrameSlots.OBJECT), ActionFrameSlots.OBJECT, token);
					} else {
						result = token.split("\\(");
						addFiller(action, ActionFrameSlots.OBJECT, result[0]);
						result = token.split("^[\\w-]*");
						token = result[1].trim().substring(1, result[1].trim().length()-1);
						StringTokenizer tokenizer = new StringTokenizer(token, " ");
						FastList<String> toks = new FastList<String>(2);
						while(tokenizer.hasMoreTokens()) {
							toks.add(tokenizer.nextToken());
						}
				
						addFiller((AgentFrame) action.getFiller(ActionFrameSlots.OBJECT), ActionFrameSlots.OBJECT, toks.removeFirst());
						addFiller((AgentFrame) action.getFiller(ActionFrameSlots.OBJECT), ActionFrameSlots.COOBJECT, toks.removeFirst());
						
						action.getFiller(ActionFrameSlots.OBJECT).getFiller(ActionFrameSlots.COOBJECT).addFiller(action.getID(), action);
					}
				} else {
					if(result.length < 3) {
						//System.err.println(token);
						result = token.split("\\s");
						if(result.length < 2) {
							result = token.split("\\(");
							ActionFrame a = new ActionFrame(Internals.INDEX.toString(), result[0]);
							Internals.INDEX++;
							action.addFiller(ActionFrameSlots.OBJECT, a);
							result = token.split("^[\\w-]*");
							token = result[1].trim().substring(1, result[1].trim().length()-1);
							addFiller(a, ActionFrameSlots.AGENT, token);
							Internals.GLOSSARY.get(token).addFiller(a.getID(), a);
							
							a.getFiller(ActionFrameSlots.AGENT).addFiller(a.getID(), a);
							//a.getFiller(ActionFrameSlots.OBJECT).addFiller(a.getID(), a);
						} else {
							result = token.split("\\(");
							ActionFrame a = new ActionFrame(Internals.INDEX.toString(), result[0]);
							Internals.INDEX++;
							action.addFiller(ActionFrameSlots.OBJECT, a);
							result = token.split("^[\\w-]*");
							token = result[1].trim().substring(1, result[1].trim().length()-1);
							//System.err.println(token);
							StringTokenizer tokenizer = new StringTokenizer(token, " ");
							FastList<String> toks = new FastList<String>(2);
							while(tokenizer.hasMoreTokens()) {
								toks.add(tokenizer.nextToken());
							}
							
							String tok = toks.removeFirst();
							addFiller(a, ActionFrameSlots.AGENT, tok);
							Internals.GLOSSARY.get(tok).addFiller(a.getID(), a);
							addFiller(a, ActionFrameSlots.OBJECT, toks.removeFirst());
							
							a.getFiller(ActionFrameSlots.AGENT).addFiller(a.getID(), a);
							a.getFiller(ActionFrameSlots.OBJECT).addFiller(a.getID(), a);
						}
					} else {
						ActionFrame a = new ActionFrame(Internals.INDEX.toString(), result[0]);
						Internals.INDEX++;
						action.addFiller(ActionFrameSlots.OBJECT, a);
						result = token.split("^[\\w-]*");
						token = result[1].trim().substring(1, result[1].trim().length()-2);
						//System.err.println(token);
						result = token.split("\\(");
						addFiller(a, ActionFrameSlots.AGENT, result[0]);
						Internals.GLOSSARY.get(result[0]).addFiller(a.getID(), a);
						result = token.split("^[\\w-]*");
						token = result[1].trim().substring(1, result[1].trim().length()-1);
						addFiller(a, ActionFrameSlots.OBJECT, token);
						//System.err.println(token);
						
						a.getFiller(ActionFrameSlots.AGENT).addFiller(a.getID(), a);
						a.getFiller(ActionFrameSlots.OBJECT).addFiller(a.getID(), a);
					}
				}
			}
			// YIKES
			action.getFiller(ActionFrameSlots.OBJECT).addFiller(action.getID(), action);
		}
	
		//action.getFiller(A)

		
		token = tokens.removeFirst();
		if(!tokens.equals("")) {
			AgentFrame location = Internals.GLOSSARY.get(token);
			
			if(location != null) {
				action.addFiller(ActionFrameSlots.LOCATION, location);
			} else {
				action.addFiller(ActionFrameSlots.LOCATION, new AgentFrame(token));
				Internals.GLOSSARY.add(token, (AgentFrame) action.getFiller(ActionFrameSlots.LOCATION));
			}
			
			// YIKES
			action.getFiller(ActionFrameSlots.LOCATION).addFiller(action.getID(), action);
		}
		
		token = tokens.removeFirst();
		if(!tokens.equals("")) {
			AgentFrame coagent = Internals.GLOSSARY.get(token);
			
			if(coagent != null) {
				action.addFiller(ActionFrameSlots.COAGENT, coagent);
			} else {
				action.addFiller(ActionFrameSlots.COAGENT, new AgentFrame(token));
				Internals.GLOSSARY.add(token, (AgentFrame) action.getFiller(ActionFrameSlots.COAGENT));
			}
			
			// YIKES
			action.getFiller(ActionFrameSlots.COAGENT).addFiller(action.getID(), action);
		}
		
		token = tokens.removeFirst();
		if(!tokens.equals("")) {
			AgentFrame time = new AgentFrame(token);
			action.addFiller(ActionFrameSlots.TIME, time);
		}
		
		if(isEvent) {
			Internals.LOGBOOK.add(action.getID(), action);
		} else {
			Internals.FACTBOOK.add(action.getID(), action);
		}
	}

	public void addFiller(Frame action, String slot, String filler) {
		AgentFrame frame = Internals.GLOSSARY.get(filler);
		
		if(frame != null) {
			action.addFiller(slot, frame);
		} else {
			action.addFiller(slot, new AgentFrame(filler));
			Internals.GLOSSARY.add(filler, (AgentFrame) action.getFiller(slot));
		}
	}
	
}
