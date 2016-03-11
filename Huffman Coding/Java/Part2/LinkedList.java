// Joseph Wong

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LinkedList {
	private Node listHead = new Node();

	public void insert(Node newNode) {
		Node walker = listHead;
		while (walker.next != null && walker.next.prob < newNode.prob) {
			walker = walker.next;
		}
		newNode.next = walker.next;
		walker.next = newNode;
	}
	
	public void binaryTree(File outfile, int count) {
		Node oldListHead = new Node(); 
		oldListHead.next = listHead.next;
		while (listHead.next.next != null) {
			Node newNode = new Node(listHead.next.chString + listHead.next.next.chString,
									listHead.next.prob + listHead.next.next.prob);
			newNode.left = listHead.next;
			newNode.right = listHead.next.next;
			listHead.next = listHead.next.next.next;
			
			insert(newNode);
			printDebug(outfile, ++count);
		}
		
		Node root = listHead.next;
		listHead = oldListHead;		
	}
	
	public String toString() {
		Node walker = listHead.next;
		String returnString = "";
		while (walker != null) {
			returnString += walker.chString + " "+ walker.prob + " ";
			walker = walker.next;
		}
		return returnString;
	}
	
	public void printDebug(File outfile, int count) {
		FileWriter writer = null;
		Node walker = listHead.next;
		
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
