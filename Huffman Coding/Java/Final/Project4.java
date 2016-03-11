import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Project4 {
	public static void main(String[] args) {	
		LinkedList myList = new LinkedList();	
		
		File outfile = new File(args[1]);		
		Scanner infile;
		
		int count = 0;
		int size = 256;
		int[] Hist;
		char[] specialArray;
		
		try {
			infile = new Scanner(new File(args[0]));
			Hist = calcHistogram(infile, size);
			specialArray = printProbPairs(Hist, outfile);
			infile = new Scanner(new File(args[1]));
			count = HuffmanLinkedList(myList, infile, outfile);
			
			outfile = new File(args[2]);	
			//print(outfile, myList.toString());
			printLinkedList(myList.getHead(), outfile, 0);
			
			
			outfile = new File(args[3]);
			Node root = myList.HuffmanBinaryTree(outfile);
			printLinkedList(myList.getHead(), outfile, 0);
			
			HuffmanCode(root, "");
			
			char[] chArray = new char[count + 1];
			String[] codeArray = new String[count + 1];
			double[] probArray = new double[count + 1], entropyArray = new double[count + 1];
			int[] bitArray = new int[count + 1];
			EntropyTable(myList.getHead(), chArray, codeArray, probArray, bitArray, entropyArray);
			
			outfile = new File(args[4]);
			printEntropyTable(chArray, codeArray, probArray, bitArray, entropyArray, outfile);
			
			outfile = new File(args[5]);
			infile = new Scanner(new File(args[7]));
			printEncode(chArray, codeArray, specialArray, infile, outfile);
			
			outfile = new File(args[6]);
			
			infile = new Scanner(new File(args[5]));
			decode(root, specialArray, infile, outfile);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int HuffmanLinkedList(LinkedList myList, Scanner infile, File outfile) {	
		String chString = "";
		double probIn = 0;
		int count = 0;
		while (infile.hasNext()) {
			chString = infile.next();
			probIn = infile.nextDouble();
			Node newNode = new Node(chString,probIn);
			myList.insert(newNode);
			count++;
			//printLinkedList(myList.getHead(), outfile, ++count);
		}	
		return count;
	}
	
	public static void HuffmanCode(Node T, String code) {
		Node walker = T;
		if (walker == null)
			return;
		if (walker.left == null && walker.right == null) {
			walker.code = code;
			return;
		}
		else {
			HuffmanCode(walker.left, code + "0");
			HuffmanCode(walker.right, code +"1");
		}
	}
	
	public static void EntropyTable(Node listHead, char[] chArray, String[] codeArray, double[] probArray, int[] bitArray, double[] entropyArray) {
		int index = 1;
		Node walker = listHead.next;
		while (walker.next != null) {
			if (walker.chString.length() == 1) {
				chArray[index] = walker.chString.charAt(0);
				codeArray[index] = walker.code;
				probArray[index] = walker.prob;
				bitArray[index] = walker.bitNum = walker.code.length();
				entropyArray[index] = walker.enProb = walker.prob * walker.bitNum;
				index++;
			}
			walker = walker.next;
		}	
	}
	
	public static void printEntropyTable(char[] chArray, String[] codeArray, double[] probArray, int[] bitArray, double[] entropyArray, File outfile) {
		FileWriter writer = null;
		
		double totalEntropy = 0;
		try {
			writer = new FileWriter(outfile, false);
			
			writer.write("Letter | Prob |  Code  | BitNum | Entropy");
			writer.write(System.getProperty("line.separator"));
			for (int i = 1; i < chArray.length; i++) {
				totalEntropy+= entropyArray[i];
				writer.write(String.format("%4s %6.4s %11s %5s %10.4s%n", chArray[i], probArray[i], codeArray[i], bitArray[i], entropyArray[i]));
			}
			writer.write(String.format("%35s %.4s%n","Total Entropy: ",totalEntropy/100));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) try { writer.close(); } catch (IOException ignore) {}
		}
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
	
	public static void printLinkedList(Node listHead, File outfile, int count) {
		FileWriter writer = null;
		Node walker = listHead;
		
		try {
			writer = new FileWriter(outfile, true);
			writer.write("Iteration " + count + ": listHead -->");
			while (walker.next != null) {
				writer.write("(" + walker.chString + ", " + walker.prob + ", " + walker.next.chString + ")-->");
				walker = walker.next;
			}
			writer.write("(" + walker.chString + ", " + walker.prob + ", NULL)-->NULL");
			writer.write(System.getProperty("line.separator"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) try { writer.close(); } catch (IOException ignore) {}
		}
	}
	
	public static int[] calcHistogram(Scanner infile, int size){
		int[] Hist = new int[size];
		String chString;
		infile.useDelimiter("");
		while (infile.hasNext()) {
			chString = infile.next();
			Hist[(int)chString.charAt(0)]++;
		}	
		return Hist;
		
	}
	
	public static char[] printProbPairs(int[] Hist, File outfile) {
		
		int total = 0;
		for (int i = 0; i < Hist.length; i++){
			total+= Hist[i];
		}
		int special = 200;
		char[] specialArray = new char[10];
		int index = 0;
		int count = 0;
		
		FileWriter writer = null;
		
		try {
			writer = new FileWriter(outfile, false);
			while (count < Hist.length) {
				if (count < 33 && Hist[count] > 0) {
					writer.write(String.format("%1s %.4s%n",(char) special++ , (Hist[count]*1.0/total*100)));
					specialArray[index++] = (char) count;
				}
				else if (Hist[count] > 0)
					writer.write(String.format("%1s %.4s%n",(char) count, (Hist[count]*1.0/total*100)));
				count++;
			}
		} catch (IOException e) {
				e.printStackTrace();
		} finally {
			if (writer != null) try { writer.close(); } catch (IOException ignore) {}
		}
		return specialArray;
	}
	
	public static void printEncode(char[] chArray, String[]codeArray, char[] specialArray, Scanner infile, File outfile) {
		FileWriter writer = null;
		infile.useDelimiter("");
		
		try {
			writer = new FileWriter(outfile, false);
			while (infile.hasNext()) {
			writer.write(findCode(chArray, codeArray, specialArray, infile.next()));
			}
		} catch (IOException e) {
				e.printStackTrace();
		} finally {
			if (writer != null) try { writer.close(); } catch (IOException ignore) {}
		}
	}
	
	public static void decode(Node root, char[] specialArray, Scanner infile, File outfile) {
		infile.useDelimiter("");
		int c;
		Node walker = root;
		FileWriter writer = null;
		try {
			writer = new FileWriter(outfile);
			
			while(infile.hasNext()) {
				c = infile.nextInt();
				if (c == 0)
					walker = walker.left;
				if (c == 1)
					walker = walker.right;
				if (walker.left == null && walker.right == null) {
					boolean special = false;
					for (int i = 0; i < 10; i++) {
						if (walker.chString.equals(String.valueOf((char)(200+i)))) {
							writer.write(String.valueOf(specialArray[i]));
							special = true;
						}
					}
					if (!special)
						writer.write(walker.chString);
					walker = root;
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) try { writer.close(); } catch (IOException ignore) {}
		}
	}
	
	public static String findCode(char[] chArray, String[] codeArray, char[] specialArray, String s) {
		int index = 0;
		
		for (int i = 0; i < specialArray.length; i++) {
			if (s.charAt(0) == specialArray[i]) {
				s = String.valueOf((char)(200+i));
			}
		}
		
		while (chArray[index] != s.charAt(0)) {
			index++;
		}
		return codeArray[index];
	}
}
