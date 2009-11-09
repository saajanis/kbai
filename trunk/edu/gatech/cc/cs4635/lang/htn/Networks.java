package edu.gatech.cc.cs4635.lang.htn;

import javolution.util.FastList;

public class Networks {
	
	private FastList<Task> networks = new FastList<Task>();
	
	public void addHTN(Task htn) {
		networks.add(htn);
	}
	
	public FastList<Task> getHTNs() {
		return networks;
	}
	
	public void debug() {
		for(Task t : networks) {
			t.verbose();
		}
	}
}
