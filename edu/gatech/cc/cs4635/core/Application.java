package edu.gatech.cc.cs4635.core;

import edu.gatech.cc.cs4635.util.Arbiter;
import edu.gatech.cc.cs4635.util.Bootstrapper;
import edu.gatech.cc.cs4635.util.Categorizer;
import edu.gatech.cc.cs4635.util.Parser;
import edu.gatech.cc.cs4635.util.Planner;
import edu.gatech.cc.cs4635.util.Reasoner;
import edu.gatech.cc.cs4635.util.htn.HTNArbiter;
import edu.gatech.cc.cs4635.util.htn.HTNParser;
import edu.gatech.cc.cs4635.util.htn.HTNPlanner;

public class Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		
		Bootstrapper bootstrapper = new Bootstrapper();
//		bootstrapper.loadFile(args[0]);
		bootstrapper.loadLexicon();
	
		
		Parser parser = new Parser();
		parser.generate(args[0], args[1]);
		
		//Internals.GLOSSARY.get("steamy-photos").debug();
		
//		System.err.println("Knowledge Output:");
//		Internals.GLOSSARY.debug();
		
		Categorizer categorizer = new Categorizer();
		//categorizer.generate();
		categorizer.categorize();
		
//		System.err.println("Categorizer Output:");
//		categorizer.debug();
		
		Reasoner reasoner = new Reasoner();
		reasoner.processClusters(categorizer.getClusters());
		
//		Planner planner = new Planner();
//		planner.generate("planner.txt");
//		planner.debug();
//		planner.associate(reasoner.getChains());
//		//planner.displayAssociations();
//		
//		System.out.println("\n\n<<<RESULTS>>>");
//		
//		Arbiter arbiter = new Arbiter();
//		arbiter.evaluate(planner.getAssociations());
		
		HTNParser htnparser = new HTNParser();
		htnparser.generate();
		Internals.HTNS.debug();
		
		HTNPlanner htnplanner = new HTNPlanner();
		htnplanner.associate(reasoner.getChains());
		htnplanner.displayAssociations();
		
		System.out.println("\n\n<<<RESULTS>>>");
		
		HTNArbiter htnarbiter = new HTNArbiter();
		htnarbiter.evaluate(htnplanner.getAssociations());
		
		
		
		//Internals.LOGBOOK.debug();
		
	}

}
