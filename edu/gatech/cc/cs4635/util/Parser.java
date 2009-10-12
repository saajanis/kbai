package edu.gatech.cc.cs4635.util;

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
import edu.gatech.cc.cs4635.lang.base.Frame;

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
				//System.err.println(line);
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
		
		ActionFrame template = Internals.LEXICON.get(id);
		if(template != null) {
			action.addFiller(ActionFrameSlot.PRECONDITION, template.getFiller(ActionFrameSlot.PRECONDITION));
			action.addFiller(ActionFrameSlot.POSTCONDITION, template.getFiller(ActionFrameSlot.POSTCONDITION));
		}
		
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
			addFiller(action, ActionFrameSlot.AGENT, token);
		} else {
			split = token.split("\\(");
			addFiller(action, ActionFrameSlot.AGENT, split[0]);
			split = token.split("^[\\w-]*");
			token = split[1].trim().substring(1, split[1].trim().length()-1);
			addFiller(action, ActionFrameSlot.OBJECT, token);
		}
		
		Internals.GLOSSARY.get(token).addFiller(action.getID(), action);
		
		token = tokens.removeFirst();
		if(!token.equals("")) {
			String[] result = token.split("\\(");
			
			if(result.length < 2) {
				result = token.split("\\s");
				if(result.length < 2) {
					addFiller(action, ActionFrameSlot.OBJECT, token);
				} else {
					StringTokenizer tokenizer = new StringTokenizer(token, " ");
					FastList<String> toks = new FastList<String>(2);
					while(tokenizer.hasMoreTokens()) {
						toks.add(tokenizer.nextToken());
					}
					
					addFiller(action, ActionFrameSlot.OBJECT, toks.removeFirst());
					addFiller(action, ActionFrameSlot.COOBJECT, toks.removeFirst());
					
					action.getFiller(ActionFrameSlot.COOBJECT).addFiller(action.getID(), action);
				}
			} else {
				if(isEvent) {
					result = token.split("\\s");
					if(result.length < 2) {
						result = token.split("\\(");
						addFiller(action, ActionFrameSlot.OBJECT, result[0]);
						result = token.split("^[\\w-]*");
						token = result[1].trim().substring(1, result[1].trim().length()-1);
						addFiller((AgentFrame) action.getFiller(ActionFrameSlot.OBJECT), ActionFrameSlot.OBJECT, token);
					} else {
						result = token.split("\\(");
						addFiller(action, ActionFrameSlot.OBJECT, result[0]);
						result = token.split("^[\\w-]*");
						token = result[1].trim().substring(1, result[1].trim().length()-1);
						StringTokenizer tokenizer = new StringTokenizer(token, " ");
						FastList<String> toks = new FastList<String>(2);
						while(tokenizer.hasMoreTokens()) {
							toks.add(tokenizer.nextToken());
						}
				
						addFiller((AgentFrame) action.getFiller(ActionFrameSlot.OBJECT), ActionFrameSlot.OBJECT, toks.removeFirst());
						addFiller((AgentFrame) action.getFiller(ActionFrameSlot.OBJECT), ActionFrameSlot.COOBJECT, toks.removeFirst());
						
						action.getFiller(ActionFrameSlot.OBJECT).getFiller(ActionFrameSlot.COOBJECT).addFiller(action.getID(), action);
					}
				} else {
					if(result.length < 3) {
						//System.err.println(token);
						result = token.split("\\s");
						if(result.length < 2) {
							result = token.split("\\(");
							ActionFrame a = new ActionFrame(Internals.INDEX.toString(), result[0]);
							Internals.INDEX++;
							action.addFiller(ActionFrameSlot.OBJECT, a);
							result = token.split("^[\\w-]*");
							token = result[1].trim().substring(1, result[1].trim().length()-1);
							addFiller(a, ActionFrameSlot.AGENT, token);
							Internals.GLOSSARY.get(token).addFiller(a.getID(), a);
							
							a.getFiller(ActionFrameSlot.AGENT).addFiller(a.getID(), a);
							//a.getFiller(ActionFrameSlots.OBJECT).addFiller(a.getID(), a);
						} else {
							result = token.split("\\(");
							ActionFrame a = new ActionFrame(Internals.INDEX.toString(), result[0]);
							Internals.INDEX++;
							action.addFiller(ActionFrameSlot.OBJECT, a);
							result = token.split("^[\\w-]*");
							token = result[1].trim().substring(1, result[1].trim().length()-1);
							//System.err.println(token);
							StringTokenizer tokenizer = new StringTokenizer(token, " ");
							FastList<String> toks = new FastList<String>(2);
							while(tokenizer.hasMoreTokens()) {
								toks.add(tokenizer.nextToken());
							}
							
							String tok = toks.removeFirst();
							addFiller(a, ActionFrameSlot.AGENT, tok);
							Internals.GLOSSARY.get(tok).addFiller(a.getID(), a);
							addFiller(a, ActionFrameSlot.OBJECT, toks.removeFirst());
							
							a.getFiller(ActionFrameSlot.AGENT).addFiller(a.getID(), a);
							a.getFiller(ActionFrameSlot.OBJECT).addFiller(a.getID(), a);
						}
					} else {
						ActionFrame a = new ActionFrame(Internals.INDEX.toString(), result[0]);
						Internals.INDEX++;
						action.addFiller(ActionFrameSlot.OBJECT, a);
						result = token.split("^[\\w-]*");
						token = result[1].trim().substring(1, result[1].trim().length()-2);
						//System.err.println(token);
						result = token.split("\\(");
						addFiller(a, ActionFrameSlot.AGENT, result[0]);
						Internals.GLOSSARY.get(result[0]).addFiller(a.getID(), a);
						result = token.split("^[\\w-]*");
						token = result[1].trim().substring(1, result[1].trim().length()-1);
						addFiller(a, ActionFrameSlot.OBJECT, token);
						//System.err.println(token);
						
						a.getFiller(ActionFrameSlot.AGENT).addFiller(a.getID(), a);
						a.getFiller(ActionFrameSlot.OBJECT).addFiller(a.getID(), a);
					}
				}
			}
			action.getFiller(ActionFrameSlot.OBJECT).addFiller(action.getID(), action);
		}
	
		// ActionFrameSlots.LOCATION
		token = tokens.removeFirst().trim();
		if(!tokens.equals("")) {
			addFiller(action, ActionFrameSlot.LOCATION, token);
			action.getFiller(ActionFrameSlot.LOCATION).addFiller(action.getID(), action);
		}
		
		// ActionFrameSlots.COAGENT
		token = tokens.removeFirst().trim();
		if(!tokens.equals("")) {
			addFiller(action, ActionFrameSlot.COAGENT, token);
			action.getFiller(ActionFrameSlot.COAGENT).addFiller(action.getID(), action);
		}
		
		// ActionFrameSlots.TIME
		token = tokens.removeFirst().trim();
		if(!tokens.equals("")) {
			AgentFrame time = new AgentFrame(token);
			action.addFiller(ActionFrameSlot.TIME, time);
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
