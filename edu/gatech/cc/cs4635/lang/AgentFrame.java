package edu.gatech.cc.cs4635.lang;

import edu.gatech.cc.cs4635.lang.base.Frame;

public class AgentFrame extends Frame {
	
	public AgentFrame(String agent) {
		super(agent);
	}

	public String getAgent() {
		return getHeader();
	}
	
	public void debug() {
		System.out.println(".:" + getHeader() + ":.");
		for(String slot : getSlots()) {
			//System.out.println(slot + ":" + getFiller(slot).getHeader());
			((Frame) getFiller(slot)).debug();
			System.out.print("\n");
		}
	}
}
