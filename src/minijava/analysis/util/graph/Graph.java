package minijava.analysis.util.graph;

import static minijava.util.List.cons;
import static minijava.util.List.empty;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import minijava.util.DefaultIndentable;
import minijava.util.IndentingWriter;
import minijava.util.List;

/**
 * This is an implementation of a Graph representation. It is nearly
 * identical to the book's Graph implementation, except that this
 * implementation use Java generics to allow Graph nodes to carry
 * information (e.g. a FlowGraph is a Graph<Instr>: so every FlowGraph
 * contains an Instr instance).
 * 
 * @author kdvolder
 */
public class Graph<N> extends DefaultIndentable {

	int nodecount=0;
	List<Node<N>> mynodes = empty();
	
	/**
	 * Maps objects to their respective nodes. This map is not constructed
	 * unless it is needed (i.e. only if you call nodeFor() ).
	 */
	private Map<N,Node<N>> nodeMap;
	
	public List<Node<N>> nodes() { return mynodes;} 

	public final Node<N> newNode(N content) {
		Node<N> node = makeNode(content);
		if (nodeMap != null) {
			// If nodeMap is being used, must keep it up-to-date!
			Assert.assertFalse("Calling newNode, but the node already exists",nodeMap.containsKey(content));
			nodeMap.put(content, node);
		}
		return node;
	}
	
	/**
	 * Override this factory method to create a different type of nodes. This method should not
	 * be called by client code.
	 */
	protected Node<N> makeNode(N content) {
		return new Node<N>(this, content);
	}
	
	public Node<N> nodeFor(N content) {
		if (nodeMap==null)
			initNodeMap();
		Node<N> node = nodeMap.get(content);
		if (node==null) {
			node = newNode(content);
		}
		return node;
	}

	private void initNodeMap() {
		nodeMap = new HashMap<N, Node<N>>();
		for (Node<N> node : mynodes) {
			Assert.assertFalse("Duplicate keys in node map", nodeMap.containsKey(node.wrappee()));
			nodeMap.put(node.wrappee(), node);
		}
	}

	void check(Node<N> n) {
		if (n.mygraph != this)
			throw new Error("Graph.addEdge using nodes from the wrong graph");
	}

	public void addEdge(Node<N> from, Node<N> to) {
		check(from); check(to);
		if (from.goesTo(to)) return;
		to.preds= cons(from, to.preds);
		from.succs=cons(to, from.succs);
	}

	public void rmEdge(Node<N> from, Node<N> to) {
		to.preds=to.preds.delete(from);
		from.succs=from.succs.delete(to);
	}

	/**
	 * Print a human-readable dump for debugging.
	 */
	@Override
	public void dump(IndentingWriter out) {
		for (Node<N> n : nodes()) {
			out.print(n.toString());
			out.print(": ");
			for (Node<N> succ : n.succ()) {
				out.print(succ);
				out.print(" ");
			}
			out.println();
		}
	}

	public void rmNode(Node<N> node) {
		for (Node<N> pred : node.pred())
			rmEdge(pred, node);
		for (Node<N> succ : node.succ())
			rmEdge(node, succ);
		mynodes = mynodes.delete(node);
	}

}