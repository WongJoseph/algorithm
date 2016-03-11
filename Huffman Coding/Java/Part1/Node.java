// Joseph Wong

public class Node {
	
	public String letter;
	public int prob;
	public Node next;
	
	public Node(String c, int data){
		letter = c;
		prob = data;
		next = null;
	}
	
	public Node(){
		letter = "";
		prob = 0;
		next = null;
	}
}
