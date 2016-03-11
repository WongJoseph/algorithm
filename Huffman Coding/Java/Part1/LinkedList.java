// Joseph Wong

public class LinkedList {
	private Node first = new Node();
	private Node last = first;
	private int length = 0;

	public int getLength() {
		return length;
	}

	public void insert(String a, int d){
		Node n = new Node(a, d);
		Node p = first.next;
		Node r = first;

		while (p != null && p.prob < n.prob) {
			r = p;
			p = p.next;
		}
		
		if (p == null)
			last = n;	
		n.next = r.next;
		r.next = n;
		length++;
	}
	
	public String toString() {
		Node p = first.next;
		String returnString = "";
		while (p != null) {
			returnString += p.letter + " "+ p.prob + " ";
			p = p.next;
		}
		return returnString;
	}
}
