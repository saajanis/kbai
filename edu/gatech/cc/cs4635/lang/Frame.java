package edu.gatech.cc.cs4635.lang;

import java.util.Set;

import javolution.util.FastMap;

public abstract class Frame implements Frameable {
	
	private final String header;
	private FastMap<String, Frameable> frameMap;
	
	public Frame(String header) {
		this.header = header;
		frameMap = new FastMap<String, Frameable>();
	}
	
	public String getHeader() {
		return header;
	}
	
	public void addFiller(String slot, Frameable filler) {
		frameMap.put(slot, filler);
	}
	
	public Frameable getFiller(String slot) {
		Frameable f = frameMap.get(slot);
		return f;
	}
	
	public Set<String> getSlots() {
		return frameMap.keySet();
	}
}
