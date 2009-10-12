package edu.gatech.cc.cs4635.lang;

import javolution.util.FastList;

public class ActionFrameSlot {
	
	public static final String AGENT = "agent";
	public static final String COAGENT = "coagent";
	public static final String OBJECT = "object";
	public static final String COOBJECT = "coobject";
	public static final String LOCATION = "location";
	public static final String TIME = "time";
	
	public static final String PRECONDITION = "precondition";
	public static final String POSTCONDITION = "postcondition";
	
	public static FastList<String> values() {
		FastList<String> values = new FastList<String>();
		values.add(AGENT);
		values.add(COAGENT);
		values.add(OBJECT);
		values.add(LOCATION);
		values.add(TIME);
		values.add(PRECONDITION);
		values.add(POSTCONDITION);
		return values;
	}

}
