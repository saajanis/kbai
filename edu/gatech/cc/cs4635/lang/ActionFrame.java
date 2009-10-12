package edu.gatech.cc.cs4635.lang;

import edu.gatech.cc.cs4635.lang.base.Frame;

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
	
//	public final void debug() {
//		System.out.println("(" + getID() + ") " + getHeader());
//		System.out.println("---");
//		for(String slot : getSlots()) {
//			System.out.println(slot + "->" + getFiller(slot).getHeader());
//		}
//	}
	
	public final void debug() {
		System.out.print("(" + getID() + " (" + getHeader() + "(");
		for(String slot : ActionFrameSlot.values()) {
			Frame f = (Frame) getFiller(slot);
			if(f != null && f.getHeader().length() > 0) {
				if(!slot.equals(ActionFrameSlot.POSTCONDITION))
					System.out.print(" " + f.getHeader() + " ,");
				else
					System.out.print(" " + f.getHeader() + " ");
			} else {
				if(!slot.equals(ActionFrameSlot.POSTCONDITION))
					System.out.print("  ,");
				else
					System.out.print("  ");
			}
		}
		System.out.print(")))");
	}
}
