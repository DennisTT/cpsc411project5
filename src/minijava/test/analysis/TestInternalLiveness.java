package minijava.test.analysis;

import minijava.analysis.FlowGraph;
import minijava.analysis.implementation.LivenessImplementation;
import minijava.codegen.AssemProc;
import minijava.codegen.assem.Instr;

/**
 * Unfortunately, there's no good way that I can think of to test this phase
 * reliably. 
 * <p>
 * So what we will do is simply to compile again all the TestTranslate programs into
 * IR code, then convert these into assem instructions, and then build liveness
 * information for the flowgraph, and dump this to System.out. 
 * <p>
 * The test provided here is therefore not much more than a test that your code produces
 * some output without crashing, not whether this output is correct in any sense.
 * <p>
 * This test is marked as "internal" because the liveness information is not used
 * directly for the next phases (register allocation), instead, the next phase uses
 * an "InterferenceGraph". It can be easily constructed from a FlowGraph with liveness
 * information.
 * 
 * @author kdvolder
 */
public class TestInternalLiveness extends TestFlowGraphs {
	
	@Override
	protected void test(AssemProc proc) {
		System.out.println("flow graph for : "+proc.getLabel());
		FlowGraph<Instr> flowGraph = FlowGraph.build(proc.getBody());
		System.out.println(flowGraph);
		
		LivenessImplementation<Instr> liveness = new LivenessImplementation<Instr>(flowGraph);
		System.out.println("Liveness:");
		System.out.println(liveness);
	}

}
