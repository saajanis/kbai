package edu.gatech.cc.cs4635.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import edu.gatech.cc.cs4635.core.Internals;
import edu.gatech.cc.cs4635.lang.ActionFrame;
import edu.gatech.cc.cs4635.lang.ActionFrameSlot;

import javolution.util.FastList;

public class Bootstrapper {
	
	public void loadLexicon() {
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/lexicon.txt"));
			
			while(br.ready()) {
				String line = br.readLine();
				//updateLexicon(line);
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
	
	private void parse(String line) {
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
		
		String token = tokens.removeFirst().trim();
		action.addFiller(ActionFrameSlot.PRECONDITION, new ActionFrame("pre", token));
		
		token = tokens.removeFirst().trim();
		action.addFiller(ActionFrameSlot.POSTCONDITION, new ActionFrame("post", token));
		
		Internals.LEXICON.add(id, action);
	}
	
	public void updateLexicon(String line) {
		StringTokenizer st = new StringTokenizer(line, ":");
		FastList<String> tokens = new FastList<String>(3);
		while(st.hasMoreTokens()) {
			tokens.add(st.nextToken());
		}
		
		ActionFrame a = new ActionFrame("0", tokens.removeFirst());
		a.addFiller(ActionFrameSlot.PRECONDITION, new ActionFrame("0", tokens.removeFirst()));
		a.addFiller(ActionFrameSlot.POSTCONDITION, new ActionFrame("0", tokens.removeFirst()));
		
		Internals.LEXICON.add(a.getAction(), a);
	}
}
