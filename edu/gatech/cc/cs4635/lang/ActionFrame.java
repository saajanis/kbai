package edu.gatech.cc.cs4635.lang;

public class ActionFrame extends Frame {
	
	private String identifier;
	
	public ActionFrame(String id, String action) {
		super(action);
		identifier = id;
	}
	
	public String getAction() {
		return getHeader();
	}
	
	public String getID() {
		return identifier;
	}
	
	public final void debug() {
		System.out.println("(" + getID() + ") " + getHeader());
		System.out.println("---");
		for(String slot : getSlots()) {
			System.out.println(slot + "->" + getFiller(slot).getHeader());
		}
	}
}
