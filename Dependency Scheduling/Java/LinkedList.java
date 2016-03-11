
public class LinkedList {
	private Node listHead = new Node();
	
	public void append(Node newNode) {
		Node walker = listHead;
		while (walker.next != null) {
			walker = walker.next;
		}
		newNode.next = walker.next;
		walker.next = newNode;
	}
	
	public Node getHead() {
		return listHead;
	}
	
	public Node remove() {
		Node job = null; 
		job = listHead.next;
		listHead.next = job.next;
		return job;
	}
	
	public boolean isEmpty() {
		if(listHead.next == null)
				return true;
		return false;
	}
}
