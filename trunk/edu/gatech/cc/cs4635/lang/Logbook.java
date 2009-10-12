package edu.gatech.cc.cs4635.lang;

import edu.gatech.cc.cs4635.lang.base.Storage;
import javolution.util.FastList;

public class Logbook extends Storage<ActionFrame> {
	
	private FastList<String> keywords = new FastList<String>();
	
	public void addKeyword(String key) {
		keywords.add(key);
	}
	
	public FastList<String> getKeywords() {
		return keywords;
	}

}
