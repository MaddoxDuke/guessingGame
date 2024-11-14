
public class Tree {
	
	Node root = new Node("");
	
	public boolean isLeaf(Node n) {
		
		if ((n.yes == null) && (n.no == null)) return true;
		else return false;
	}
}
