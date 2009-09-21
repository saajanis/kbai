package edu.gatech.cc.cs4635.lang;

public interface Frameable {
	
	public String getHeader();
	public void addFiller(String slot, Frameable filler);
	public Frameable getFiller(String slot);
	public void debug();

}
