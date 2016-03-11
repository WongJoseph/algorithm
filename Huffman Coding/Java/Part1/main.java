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
		
		String a = "";
		int i = 0;
		int count = 0;
		
		LinkedList myList = new LinkedList();
		
		try {
			infile = new Scanner(file);
			while (infile.hasNext()) {
				if (infile.hasNextInt()) {
					i = infile.nextInt();		
				}
				else
				{
					a = infile.next();
				}
				if (++count%2 == 0) {
					myList.insert(a, i);
					print(args[1],myList.toString());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		print(args[2],myList.toString());
	}
	
	public static void print(String s, String data) {
		File file = new File (s);
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
