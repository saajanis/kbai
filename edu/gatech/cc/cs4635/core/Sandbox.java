package edu.gatech.cc.cs4635.core;

import edu.gatech.cc.cs4635.util.Categorizer;
import edu.gatech.cc.cs4635.util.Parser;

public class Sandbox {

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		
		Parser parser = new Parser();
		parser.generate(args[0], args[1]);
		
		//Internals.GLOSSARY.debug();
		
		Categorizer categorizer = new Categorizer();
		//categorizer.generate();
		categorizer.categorize();
		
		categorizer.debug();
		
////		String line = "fuck(you me)";
		//String line = "were-unfortunate(steamy-photos(john torch)))";
//		String line = "are(moral-values important)";
//		String[] result = line.split("\\(");
//		System.err.println(result.length + ":" + result[0] + " ^ "+ result[1]);
//		result = line.split("\\s");
//		System.err.println(result.length + ":" + result[0] + " ^ "+ result[1]);
		
		

	}

}
