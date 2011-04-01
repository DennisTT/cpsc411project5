package minijava.analysis.implementation;

import java.util.HashMap;
import java.util.Iterator;

import minijava.analysis.FlowGraph;
import minijava.analysis.InterferenceGraph;
import minijava.analysis.util.graph.Node;
import minijava.codegen.assem.A_MOVE;
import minijava.codegen.assem.Instr;
import minijava.ir.temp.Temp;
import minijava.util.List;

public class AssemFlowGraph extends FlowGraph<Instr>
{
  private HashMap<Node<Instr>, Instr> table = new HashMap<Node<Instr>, Instr>();
  
  public AssemFlowGraph(List<Instr> body)
  {
    Node<Instr> n,
                prevNode = null;
    
    Iterator<Instr> itr = body.iterator();
    while(itr.hasNext())
    {
      Instr i = itr.next();
      n = this.nodeFor(i);
      this.table.put(n, i);
      
      if(prevNode != null)
      {
        this.addEdge(prevNode, n);
      }
      
      prevNode = n;
    }
  }

  @Override
  public List<Temp> def(Node<Instr> node)
  {
    Instr i = this.table.get(node);
    if(i == null)
    {
      return null;
    }
    
    return i.def();
  }

  @Override
  public List<Temp> use(Node<Instr> node)
  {
    Instr i = this.table.get(node);
    if(i == null)
    {
      return null;
    }
    
    return i.use();
  }

  @Override
  public boolean isMove(Node<Instr> node)
  {
    Instr i = this.table.get(node);
    return (i != null) && (i instanceof A_MOVE);
  }

  @Override
  public InterferenceGraph getInterferenceGraph()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
