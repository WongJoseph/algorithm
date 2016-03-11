
public class Node {
	public String chString;
	public double prob;
	public Node left;
	public Node right;
	public int bitNum;
	public double enProb;
	public String code;
	public Node next;
	
	public Node(String c, double probIn){
		chString = c;
		prob = probIn;
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
