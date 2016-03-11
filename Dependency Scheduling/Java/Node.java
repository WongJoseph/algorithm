
public class Node {
	public int jobId;
	public int time;
	public Node next;
	
	public Node(int id) {
		jobId = id;
		next = null;
	}
	
	public Node(int id, int t) {
		jobId = id;
		time = t;
		next = null;
	}
	
	public Node() {
		jobId = 0;
		time = 0;
		next = null;
	}
}
