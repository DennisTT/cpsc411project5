package minijava.analysis;

import minijava.analysis.util.graph.Node;
import minijava.ir.temp.Temp;
import minijava.util.DefaultIndentable;
import minijava.util.IndentingWriter;
import minijava.util.List;

/**
 * This class defines the interface for a Liveness analysis. It provides some
 * mechanism to attach / compute liveness information to/for a FlowGraph.
 */
public abstract class Liveness<N> extends DefaultIndentable {
	
	private static final int TAB_STOP = 50;
	
	protected final FlowGraph<N> g;
	
	public Liveness(FlowGraph<N> graph) {
		this.g = graph;
	}
	
	/**
	 * Returns a list of Temps that are live *after* the
	 * execution of a given node in the FlowGraph.
	 * <p>
	 * Note: you will probably also need to compute liveIn sets,
	 * but they are not part of the external interface because
	 * they are not used by the algorithm that constructs the
	 * interference graph.
	 */
	public abstract List<Temp> liveOut(Node<N> node);
	
	/**
	 * Print a human-readable dump for debugging.
	 */
	@Override
	public void dump(IndentingWriter out) {
		for (Node<N> n : g.nodes()) {
			out.print(n.toString());
			out.print(": ");
			for(Temp temp : g.def(n)) {
				out.print(temp.toString());
				out.print(" ");
			}
			out.print(g.isMove(n) ? "<= " : "<- ");
			for(Temp temp : g.use(n)) {
				out.print(temp);
				out.print(" ");
			}
			out.print("; goto ");
			for(Node<N> succ : n.succ()) {
				out.print(succ);
				out.print(" ");
			}
			out.tabTo(TAB_STOP); 
			for (Temp liveOne : liveOut(n)) {
				out.print(liveOne);
				out.print(" ");
			}
			out.println();
		}
	}
}
