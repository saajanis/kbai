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
	
	// TODO: run() needs to iterate through all the items, checking if the event has already
	// been added to some book. if not, add it, and the keywords to the book.
	
	public void categorize() {
		for(String key : logbook.entries()) {
			ActionFrame a = logbook.get(key);
			
			if(!alreadyEntered(a)) {
				Logbook l = new Logbook();
				l.add(a.getID(), a);
				books.add(l);
				
				processFrame(a, l);
				

			}
		}
		
		System.err.println("Categorizer done.");
	}
	
	public void processFrame(ActionFrame a, Logbook l) {
		for(String slot : a.getSlots()) {
			AgentFrame f = (AgentFrame) a.getFiller(slot);
			if(!f.getHeader().equals("")) {
				for(String entry : f.getSlots()) {
					if(!entry.equals(ActionFrameSlots.OBJECT) && !entry.equals(ActionFrameSlots.COOBJECT)) {
						ActionFrame i = (ActionFrame) f.getFiller(entry);
						if(convert(logbook.entries()).contains(i.getID())) {
							if(!alreadyEntered(i)) {
								l.add(i.getID(), i);
								processFrame(i, l);
							}
						}
					}
				}
			}
		}
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
	
	
	public void generate() {
		for(String key : logbook.entries()) {
			ActionFrame a = logbook.get(key);
			
			boolean isUsed = false;
			for(Logbook b : books) {
				for(String e : b.entries()) {
					if(e.equals(a.getID())) {
						isUsed = true;
					}
				}
			}
			
			if(!isUsed) {
				Logbook l = new Logbook();
				l.add(a.getID(), a);
				
				for(String slot : a.getSlots()) {
					l.addKeyword(a.getFiller(slot).getHeader());
				}
				
				for(String entry : logbook.entries()) {
					ActionFrame d = logbook.get(entry);
					if(d != a) {
						boolean isFound = false;
						for(String slot : d.getSlots()) {
							String value = d.getFiller(slot).getHeader();
							if(l.getKeywords().contains(value) && !value.equals("")) {
								isFound = true;
							}
						}
						if(isFound) {
							l.add(d.getID(), d);
							for(String slot : d.getSlots()) {
								if(!l.getKeywords().contains(d.getFiller(slot).getHeader())) {
									l.addKeyword(d.getFiller(slot).getHeader());
								}
							}
						}
					}
				}
				
				books.add(l);
				
			}
			
		}
		
		System.err.println("Categorizer done.");
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
