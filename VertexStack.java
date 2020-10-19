import java.util.EmptyStackException;
public class VertexStack { // stack for our paths so that we can find them

	private Node head;
	private Node top;
	
	public VertexStack() {
		head=null;
	}
	
	private class Node{ // inner int node class
		private int index;
		protected Node next;
		
		public Node(int n) {
			this.setIndex(n);
		}

	
		public void setIndex(int index) {
			this.index = index;
		}
	}
	
	
	public void push(int ind) {
		Node temp=head;
		head=new Node(ind);
		head.next=temp;
	}
	
	public int pop() throws EmptyStackException{
		int temp;
		if(head!=null) {
			temp=head.index;
			head=head.next;
		}
		else {
			throw new EmptyStackException();
		}
		return temp;
	}

	
	public boolean isEmpty() {
		return head==null;
	}

	public Node getTop() {
		return top;
	}

	public void setTop(Node top) {
		this.top = top;
	}
	
}
