import java.io.File;

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
	
	public Node getHead() {
		return listHead;
	}
	
	public void setHead(Node T) {
		listHead = T;
	}
	
	public Node HuffmanBinaryTree(File outfile) {
		int count = 0;
		Node oldListHead = new Node(); 
		oldListHead.next = listHead.next;
		while (listHead.next.next != null) {
			Node newNode = new Node(listHead.next.chString + listHead.next.next.chString,
									listHead.next.prob + listHead.next.next.prob);
			newNode.left = listHead.next;
			newNode.right = listHead.next.next;
			listHead.next = listHead.next.next.next;
			insert(newNode);
			//printLinkedList(listHead, outfile, ++count);
		}
		
		Node root = listHead.next;
		root.right.next = root;
		listHead = oldListHead;
		return root;
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
}
