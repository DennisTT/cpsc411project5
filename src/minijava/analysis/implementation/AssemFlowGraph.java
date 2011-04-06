package minijava.analysis.implementation;

import java.util.Iterator;

import minijava.analysis.FlowGraph;
import minijava.analysis.InterferenceGraph;
import minijava.analysis.InterferenceGraph.Move;
import minijava.analysis.util.graph.Node;
import minijava.codegen.assem.A_MOVE;
import minijava.codegen.assem.Instr;
import minijava.ir.temp.Temp;
import minijava.util.List;

public class AssemFlowGraph extends FlowGraph<Instr>
{  
  public AssemFlowGraph(List<Instr> body)
  {
    Node<Instr> n,
                prevNode = null;
    
    Iterator<Instr> itr = body.iterator();
    while(itr.hasNext())
    {
      Instr i = itr.next();
      n = this.nodeFor(i);
      
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
    return node.wrappee().def();
  }

  @Override
  public List<Temp> use(Node<Instr> node)
  {
    return node.wrappee().use();
  }

  @Override
  public boolean isMove(Node<Instr> node)
  {
    return (node.wrappee() instanceof A_MOVE);
  }

  @Override
  public InterferenceGraph getInterferenceGraph()
  {
    InterferenceGraphImplementation ig = new InterferenceGraphImplementation();
    LivenessImplementation<Instr> live = new LivenessImplementation<Instr>(this);
    
    for(Node<Instr> node : this.nodes())
    {
      // Special treatment for MOVE instructions
      if(isMove(node) && node.wrappee() instanceof A_MOVE)
      {
        // Record a move
        ig.addMove((A_MOVE) node.wrappee());
      }
      
      for(Temp def : this.def(node))
      {
        for(Temp out : live.liveOut(node))
        {
          if(!isMove(node) || !(node.wrappee() instanceof A_MOVE))
          {
            // Not a MOVE instruction
            
            // Check if redefining a previous MOVE dst
            for(Move move : ig.moves())
            {
              // Add edge back if clobbering dst, and src is still live
              if(move.dst.equals(def) && move.src.equals(out))
              {
                ig.addEdge(move.dst, move.src);
                ig.addEdge(move.src, move.dst);
              }
            }
            
            // Add regular interference edges
            if(!def.equals(out))
            {
              ig.addEdge(ig.nodeFor(def), ig.nodeFor(out));
              ig.addEdge(ig.nodeFor(out), ig.nodeFor(def));
            }
          }
          else
          {
            // MOVE instruction
            
            // def = destination
            // source is in the out list
            Temp mvSrc = ((A_MOVE) node.wrappee()).src;
            Temp mvDst = ((A_MOVE) node.wrappee()).dst;
            
            // For move nodes, add interference edges that are not (s, t) for s -> t moves
            if(!(mvDst.equals(def) && mvSrc.equals(out)) && !def.equals(out))
            {
              ig.addEdge(ig.nodeFor(def), ig.nodeFor(out));
              ig.addEdge(ig.nodeFor(out), ig.nodeFor(def));
            }
          }
        }
      }
    }
    
    return ig;
  }
}
