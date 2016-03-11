// Joseph Wong

public class Node {
	
	public String chString;
	public int prob;
	public Node left;
	public Node right;
	public int bitNum;
	public int enProb;
	public Node next;
	
	public Node(String c, int data){
		chString = c;
		prob = data;
		left = null;
		right = null;
		next = null;
	}
	
	public Node(){
		chString = "dummy";
		prob = 0;
		left = null;
		right = null;
		next = null;
	}
}
