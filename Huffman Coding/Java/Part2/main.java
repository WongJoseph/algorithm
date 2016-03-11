// Joseph Wong

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class main {
	public static void main(String[] args) {
		File file = new File(args[0]);
		Scanner infile;
		
		String chString = "";
		int probIn = 0;
		int count = 0;
		boolean newChar = false;
		boolean newProb = false;
		
		LinkedList myList = new LinkedList();	
		File outfile = new File(args[1]);
		
		try {
			infile = new Scanner(file);
			while (infile.hasNext()) {
				if (infile.hasNextInt()) {
					probIn = infile.nextInt();
					newProb = true;
				}
				else
				{
					chString = infile.next();
					newChar = true;
				}
				if (newChar && newProb) {
					Node newNode = new Node(chString,probIn);
					myList.insert(newNode);
					newChar = false;
					newProb = false;
					//myList.printDebug(outfile, ++count);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		print(outfile, myList.toString());
		//outfile = new File(args[3]);
		outfile = new File(args[2]);
		myList.binaryTree(outfile, 0);
	}
	
	public static void print(File file, String data) {
		FileWriter writer = null;
		
		try {
			writer = new FileWriter(file, true);
			writer.write(data);
			writer.write(System.getProperty("line.separator"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) try { writer.close(); } catch (IOException ignore) {}
		}
	}			
}