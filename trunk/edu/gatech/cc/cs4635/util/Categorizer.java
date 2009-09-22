package edu.gatech.cc.cs4635.util;

import java.util.Set;

import javolution.util.FastList;
import edu.gatech.cc.cs4635.core.Internals;
import edu.gatech.cc.cs4635.lang.ActionFrame;
import edu.gatech.cc.cs4635.lang.ActionFrameSlots;
import edu.gatech.cc.cs4635.lang.AgentFrame;
import edu.gatech.cc.cs4635.lang.Frame;
import edu.gatech.cc.cs4635.lang.Logbook;

public class Categorizer {

	private Logbook logbook = Internals.LOGBOOK; //.internal();
	
	private FastList<Logbook> books = new FastList<Logbook>();
	private Logbook search = new Logbook();
	
	// TODO: run() needs to iterate through all the items, checking if the event has already
	// been added to some book. if not, add it, and the keywords to the book.
	
	public FastList<Logbook> getClusters() {
		return books;
	}
	
	public void categorize() {
		System.err.print("Categorizer starting...");
		
		for(String key : logbook.entries()) {
			ActionFrame a = logbook.get(key);
			
			if(!alreadyEntered(a)) {
				Logbook l = new Logbook();
				l.add(a.getID(), a);
				books.add(l);
				
				processFrame(a, l);
				

			}
		}
		
		System.err.println("done.");
	}
	
	public void processFrame(ActionFrame a, Logbook l) {
		for(String slot : a.getSlots()) {
			try {
				AgentFrame f = (AgentFrame) a.getFiller(slot);
				
				if(!f.getHeader().equals("")) {
					for(String entry : f.getSlots()) {
						if(!entry.equals(ActionFrameSlots.OBJECT) && !entry.equals(ActionFrameSlots.COOBJECT)) {
							ActionFrame i = (ActionFrame) f.getFiller(entry);
							if(!alreadyEntered(i) && !alreadySearched(i)) {
								if(convert(logbook.entries()).contains(i.getID())) {
								//if(!alreadyEntered(i)) {
									//processFrame(i,l);
									l.add(i.getID(), i);
								}
								search.add(i.getID(), i);
								processFrame(i, l);
							}
						} else {
							if(entry.equals(ActionFrameSlots.OBJECT)) {
								AgentFrame e = (AgentFrame) f.getFiller(ActionFrameSlots.OBJECT);
								for(String kai : e.getSlots()) {
									if(!kai.equals(ActionFrameSlots.OBJECT) && !kai.equals(ActionFrameSlots.COOBJECT)) {
										ActionFrame i = (ActionFrame) e.getFiller(kai);
										if(!alreadyEntered(i) && !alreadySearched(i)) {
											if(convert(logbook.entries()).contains(i.getID())) {
											//if(!alreadyEntered(i)) {
												//processFrame(i,l);
												l.add(i.getID(), i);
											}
											search.add(i.getID(), i);
											processFrame(i, l);
										}
									}
								}
							} else {
								AgentFrame e = (AgentFrame) f.getFiller(ActionFrameSlots.OBJECT);
								for(String kai : e.getSlots()) {
									if(!kai.equals(ActionFrameSlots.OBJECT) && !kai.equals(ActionFrameSlots.COOBJECT)) {
										ActionFrame i = (ActionFrame) e.getFiller(kai);
										if(!alreadyEntered(i) && !alreadySearched(i)) {
											if(convert(logbook.entries()).contains(i.getID())) {
											//if(!alreadyEntered(i)) {
												//processFrame(i,l);
												l.add(i.getID(), i);
											}
											search.add(i.getID(), i);
											processFrame(i, l);
										}
									}
								}
							}
							//System.err.println(f.getAgent() + ":" + entry);
						}
					}
				}
			} catch(ClassCastException e) {
				// skip it
			}
		}
	}
	
	public boolean isNumber(String input) {
		try {
			Integer.parseInt(input);
		} catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	public boolean alreadyEntered(ActionFrame a) {
		boolean isAlreadyEntered = false;
		for(Logbook l : books) {
			for(String key : l.entries()) {
				if(key.equals(a.getID())) {
					isAlreadyEntered = true;
				}
			}
		}
		return isAlreadyEntered;
	}
	
	public boolean alreadySearched(ActionFrame a) {
		boolean wasAlreadySearched = false;
		for(String key : search.entries()) {
			if(key.equals(a.getID())) {
				wasAlreadySearched = true;
			}
		}
		return wasAlreadySearched;
	}
	
	public FastList<String> convert(Set<String> set) {
		FastList<String> list = new FastList<String>(set.size());
		for(String key : set) {
			list.add(key);
		}
		return list;
	}
	
	public void debug() {
		for(Logbook l : books) {
			System.err.println(".:Logbook:.");
			for(String key : l.entries()) {
				System.err.println(l.get(key).getID() + ":" + l.get(key).getAction());
			}
			System.err.println("");
		}
	}
}
