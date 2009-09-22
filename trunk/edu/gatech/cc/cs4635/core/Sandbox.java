package edu.gatech.cc.cs4635.core;

import edu.gatech.cc.cs4635.util.Bootstrapper;
import edu.gatech.cc.cs4635.util.Categorizer;
import edu.gatech.cc.cs4635.util.Parser;
import edu.gatech.cc.cs4635.util.Reasoner;

public class Sandbox {

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
		
		System.err.println("Knowledge Output:");
		Internals.GLOSSARY.debug();
		
		Categorizer categorizer = new Categorizer();
		//categorizer.generate();
		categorizer.categorize();
		
		System.err.println("Categorizer Output:");
		categorizer.debug();
		
		Reasoner reasoner = new Reasoner();
		reasoner.processClusters(categorizer.getClusters());
	}

}
