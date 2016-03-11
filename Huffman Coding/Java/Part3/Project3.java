import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Project3 {
	public static void main(String[] args) {	
		LinkedList myList = new LinkedList();	
		
		File outfile = new File(args[1]);		
		Scanner infile;
		
		int count = 0;
		
		try {
			infile = new Scanner(new File(args[0]));
			count = HuffmanLinkedList(myList, infile, outfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
		print(outfile, myList.toString());
		
		outfile = new File(args[2]);
		Node root = myList.HuffmanBinaryTree(outfile);
		
		HuffmanCode(root, "");
		printLinkedList(myList.getHead(), outfile, 0);
		
		char[] chArray = new char[count + 1];
		String[] codeArray = new String[count + 1];
		int[] probArray = new int[count + 1], bitArray = new int[count + 1], entropyArray = new int[count + 1];
		EntropyTable(myList.getHead(), chArray, codeArray, probArray, bitArray, entropyArray);
		
		outfile = new File(args[3]);
		printEntropyTable(chArray, codeArray, probArray, bitArray, entropyArray, outfile);
	}
	
	public static int HuffmanLinkedList(LinkedList myList, Scanner infile, File outfile) {	
		String chString = "";
		int probIn = 0;
		int count = 0;
		
		while (infile.hasNext()) {
			chString = infile.next();
			probIn = infile.nextInt();
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
		if (walker.chString.length() == 1) {
			walker.code = code;
			return;
		}
		else {
			HuffmanCode(walker.left, code + "0");
			HuffmanCode(walker.right, code +"1");
		}
	}
	
	public static void EntropyTable(Node listHead, char[] chArray, String[] codeArray, int[] probArray, int[] bitArray, int[] entropyArray) {
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
	
	public static void printEntropyTable(char[] chArray, String[] codeArray, int[] probArray, int[] bitArray, int[] entropyArray, File outfile) {
		FileWriter writer = null;
		
		int totalEntropy = 0;
		try {
			writer = new FileWriter(outfile, true);
			
			writer.write("Letter | Prob |  Code  | BitNum | Entropy");
			writer.write(System.getProperty("line.separator"));
			for (int i = 1; i < chArray.length; i++) {
				totalEntropy+= entropyArray[i];
				writer.write(String.format("%4s %6s %11s %5s %10s%n", chArray[i], probArray[i], codeArray[i], bitArray[i], entropyArray[i]));
			}
			writer.write(String.format("%35s %.4s%n","Total Entropy: ",totalEntropy*1.0/100));
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
}
