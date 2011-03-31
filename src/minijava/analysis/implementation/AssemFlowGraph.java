package minijava.analysis.implementation;

import minijava.analysis.FlowGraph;
import minijava.analysis.InterferenceGraph;
import minijava.analysis.util.graph.Node;
import minijava.codegen.assem.Instr;
import minijava.ir.temp.Temp;
import minijava.util.List;

public class AssemFlowGraph extends FlowGraph<Instr>
{
  public AssemFlowGraph(List<Instr> body)
  {
    // TODO
  }

  @Override
  public List<Temp> def(Node<Instr> node)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Temp> use(Node<Instr> node)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isMove(Node<Instr> node)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public InterferenceGraph getInterferenceGraph()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
