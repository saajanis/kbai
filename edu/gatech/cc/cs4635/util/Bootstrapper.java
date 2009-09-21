package edu.gatech.cc.cs4635.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Bootstrapper {
	
	public void loadFile(String filename) {		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/bootstrap.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/" + filename, true));
			
			while(br.ready()) {
				String line = br.readLine();
				bw.write(line);
				bw.flush();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
