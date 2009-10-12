package edu.gatech.cc.cs4635.util;

import edu.gatech.cc.cs4635.lang.ActionFrame;
import edu.gatech.cc.cs4635.lang.ActionFrameSlot;
import edu.gatech.cc.cs4635.lang.Logbook;
import javolution.util.FastList;

public class Reasoner {
	
	private FastList<Logbook> chains = new FastList<Logbook>();
	
	public void processClusters(FastList<Logbook> clusters) {
		for(Logbook l : clusters) {
			for(String key : l.entries()) {
				ActionFrame a = l.get(key);
				if(a.getFiller(ActionFrameSlot.PRECONDITION) != null) {
					if(a.getFiller(ActionFrameSlot.PRECONDITION).getHeader().equals("none")) {
						Logbook chain = new Logbook();
						//System.out.println("Start building!");
						buildChain(l, chain, a);
						chains.add(chain);
					}
				}
			}
		}
		
		System.err.println("Reasoner Output (Causal Chain)");
		
		for(Logbook l : chains) {
			System.out.println(".:Chain:.");
			l.debug();
			System.out.print("\n");
		}
		
		
		
		//Internals.GLOSSARY.get("robbers").debug();
	}
	
	public FastList<Logbook> getChains() {
		return chains;
	}
	
	public void buildChain(Logbook cluster, Logbook chain, ActionFrame a) {
		chain.add(a.getID(), a);
		for(String key : cluster.entries()) {
			ActionFrame b = cluster.get(key);
			//System.err.println(b.getAction());
			if(b.getFiller(ActionFrameSlot.PRECONDITION) != null) {
				String pre = b.getFiller(ActionFrameSlot.PRECONDITION).getHeader();
				String post = a.getFiller(ActionFrameSlot.POSTCONDITION).getHeader();
				//System.out.println(pre + "," + post);
				//System.out.println(pre.length() + "," + post.length());
				if(pre.equals(post) && !pre.equals("none")) {
					buildChain(cluster, chain, b);
					return;
				}
			}
		}
	}

}
