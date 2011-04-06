package minijava.analysis.implementation;

import java.util.HashMap;
import java.util.HashSet;

import minijava.analysis.FlowGraph;
import minijava.analysis.Liveness;
import minijava.analysis.util.graph.Node;
import minijava.ir.temp.Temp;
import minijava.util.List;

public class LivenessImplementation<N> extends Liveness<N>
{
  HashMap<Node<N>, HashSet<Temp>> liveIn;
  HashMap<Node<N>, HashSet<Temp>> liveOut;
  
  public LivenessImplementation(FlowGraph<N> graph)
  {
    super(graph);
    
    liveIn = new HashMap<Node<N>, HashSet<Temp>>();
    liveOut = new HashMap<Node<N>, HashSet<Temp>>();
    
    // Iteration algorithm
    
    // Setting up the hash map with empty sets.
    // Not using List here because List doesn't have a working equals() => sucks
    for(Node<N> node : graph.nodes())
    {
      liveIn.put(node, new HashSet<Temp>());
      liveOut.put(node, new HashSet<Temp>());
    }
    
    boolean changes;
    do
    {
      changes = false;
      
      for(Node<N> node : graph.nodes())
      {
        // Calculate the new sets
        HashSet<Temp> in = internalLiveIn(node);
        HashSet<Temp> out = internalLiveOut(node);
        
        // Make sure we re-iterate if changes are found
        if(!in.equals(liveIn.get(node)) || !out.equals(liveOut.get(node)))
        {
          changes = true;
        }
        
        // Update
        liveIn.put(node, in);
        liveOut.put(node, out);
      }
    } while(changes);
    
  }

  @Override
  public List<Temp> liveOut(Node<N> node)
  {
    List<Temp> temps = List.empty();
    for(Temp temp : liveOut.get(node))
    {
      temps.add(temp);
    }
    return temps;
  }
  
  private HashSet<Temp> internalLiveOut(Node<N> node)
  {
    // out[n] = U_{s in succ[n]} in[s]
    HashSet<Temp> liveOut = new HashSet<Temp>();
    for(Node<N> succNode : node.succ())
    {
      liveOut.addAll(internalLiveIn(succNode));
    }
    return liveOut;
  }
  
  private HashSet<Temp> internalLiveIn(Node<N> node)
  {
    // in[n] = use[n] U (out[n] - def[n])

    // (out[n] - def[n])
    HashSet<Temp> liveOut = internalLiveOut(node);
    for(Temp temp : g.def(node))
    {
      liveOut.remove(temp);
    }
    
    // use[n] U (out[n] - def[n])
    for(Temp temp : g.use(node))
    {
      liveOut.add(temp);
    }
    
    return liveOut;
  }
}
