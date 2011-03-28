package minijava.test.analysis;

import junit.framework.Assert;
import minijava.analysis.FlowGraph;
import minijava.analysis.util.graph.Graph;
import minijava.analysis.util.graph.Node;
import minijava.codegen.AssemProc;
import minijava.codegen.assem.Instr;
import minijava.ir.temp.Temp;

/**
 * Again this test (mostly) just prints out the result of constructing the interference graph.
 * The test provided here is therefore not much more than a test that your code produces
 * some output without crashing, not whether this output is correct in any sense.
 * 
 * @author kdvolder
 */
public class TestInterference extends TestFlowGraphs {
	
	@Override
	protected void test(AssemProc proc) {
		System.out.println("flow graph for : "+proc.getLabel());
		FlowGraph<Instr> flowGraph = FlowGraph.build(proc.getBody());
		System.out.println(flowGraph);
		
		Graph<Temp> interf = flowGraph.getInterferenceGraph();
		System.out.println(interf);
		
		//There are a few simple sanity checks that can be performed:
		Assert.assertTrue(isSymmetric(interf));
		   // Should be symmetric because if t1 interferes with t2 it means they
		   // can not both be allocated to the same register. This is obviously a
		   // symmetric relationship.
		
		Assert.assertTrue(isNonReflexive(interf));
		   // No temp should ever interfere with itself (this would mean that
		   // it can not be allocated the same register as itself).
		
	}

	private <N> boolean isNonReflexive(Graph<N> g) {
		for (Node<N> node : g.nodes())
			if (node.goesTo(node)) return false;
		return true;
	}

	private <N> boolean isSymmetric(Graph<N> g) {
		for (Node<N> node : g.nodes()) {
			for (Node<N> succ : node.succ()) {
				Assert.assertTrue(node.goesTo(succ));
				Assert.assertTrue(succ.comesFrom(node));
				if (!succ.goesTo(node)) return false;
			}
		}
		return true;
	}

}
