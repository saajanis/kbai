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
import edu.gatech.cc.cs4635.lang.ActionFrameSlots;

import javolution.util.FastList;

public class Bootstrapper {
	
	public void loadFile(String filename) {		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/bootstrap.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/" + filename, true));
			
			while(br.ready()) {
				String line = br.readLine();
				bw.write(line);
				bw.flush();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadLexicon() {
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/lexicon.txt"));
			
			while(br.ready()) {
				String line = br.readLine();
				updateLexicon(line);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateLexicon(String line) {
		StringTokenizer st = new StringTokenizer(line, ":");
		FastList<String> tokens = new FastList<String>(3);
		while(st.hasMoreTokens()) {
			tokens.add(st.nextToken());
		}
		
		ActionFrame a = new ActionFrame("0", tokens.removeFirst());
		a.addFiller(ActionFrameSlots.PRECONDITION, new ActionFrame("0", tokens.removeFirst()));
		a.addFiller(ActionFrameSlots.POSTCONDITION, new ActionFrame("0", tokens.removeFirst()));
		
		Internals.LEXICON.add(a.getAction(), a);
	}
}
