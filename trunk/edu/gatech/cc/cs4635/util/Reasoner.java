package edu.gatech.cc.cs4635.util;

import edu.gatech.cc.cs4635.lang.ActionFrame;
import edu.gatech.cc.cs4635.lang.ActionFrameSlots;
import edu.gatech.cc.cs4635.lang.Logbook;
import javolution.util.FastList;

public class Reasoner {
	
	private FastList<Logbook> chains = new FastList<Logbook>();
	
	public void processClusters(FastList<Logbook> clusters) {
		for(Logbook l : clusters) {
			for(String key : l.entries()) {
				ActionFrame a = l.get(key);
				if(a.getFiller(ActionFrameSlots.PRECONDITION) != null) {
					if(a.getFiller(ActionFrameSlots.PRECONDITION).getHeader().equals("none")) {
						Logbook chain = new Logbook();
						buildChain(l, chain, a);
						chains.add(chain);
					}
				}
			}
		}
		
		for(Logbook l : chains) {
			System.out.println(".:Chain:.");
			l.debug();
		}
	}
	
	public void buildChain(Logbook cluster, Logbook chain, ActionFrame a) {
		chain.add(a.getID(), a);
		for(String key : cluster.entries()) {
			ActionFrame b = cluster.get(key);
			//System.err.println(b.getAction());
			String pre = b.getFiller(ActionFrameSlots.PRECONDITION).getHeader();
			String post = a.getFiller(ActionFrameSlots.POSTCONDITION).getHeader();
			if(pre.equals(a.getAction()) && post.equals(b.getAction())) {
				buildChain(cluster, chain, b);
				return;
			}
		}
	}

}
