package minijava.analysis.implementation;

import minijava.analysis.FlowGraph;
import minijava.analysis.Liveness;
import minijava.analysis.util.graph.Node;
import minijava.ir.temp.Temp;
import minijava.util.List;

public class LivenessImplementation<N> extends Liveness<N>
{
  public LivenessImplementation(FlowGraph<N> graph)
  {
    super(graph);
    // TODO Auto-generated constructor stub
  }

  @Override
  public List<Temp> liveOut(Node<N> node)
  {
    // TODO Auto-generated method stub
    return null;
  }
}
