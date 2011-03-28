package minijava.analysis;
import minijava.analysis.util.graph.Graph;
import minijava.analysis.util.graph.Node;
import minijava.ir.temp.Temp;
import minijava.util.DefaultIndentable;
import minijava.util.IndentingWriter;
import minijava.util.List;

abstract public class InterferenceGraph extends Graph<Temp> {
	
	public class Move extends DefaultIndentable {
		
		public final Node<Temp> src, dst;
		public Move(Node<Temp> dst, Node<Temp> src) {
			this.dst = dst;
			this.src = src;
		}
		
		@Override
		public void dump(IndentingWriter out) {
			out.print(dst);
			out.print(" <= ");
			out.print(src);
		}
	}
	
	abstract public List<Move> moves();
	
	/**
	 * This default implementation will work, but you should 
	 * override it to provide a better implementation. 
	 * A good implementation should assign a higher spill cost
	 * to a Temp that is used frequently (and also may reduce
	 * spill cost if a temp interferes with lots of other temps,
	 * because spilling it will help avoid more spills.
	 */
	public double spillCost(Node<Temp> node) {return 1;}
	
	@Override
	protected Node<Temp> makeNode(Temp content) {
		// Create nodes that print nicer.
		return new Node<Temp>(this, content) {
			@Override
			public String toString() {
				return wrappee().toString();
			}
		};
	}
}
