package minijava.analysis.implementation;

import java.io.StringWriter;

import minijava.analysis.InterferenceGraph;
import minijava.codegen.assem.A_MOVE;
import minijava.util.IndentingWriter;
import minijava.util.List;

public class InterferenceGraphImplementation extends InterferenceGraph
{
  List<Move> moves = List.empty();
  
  public void addMove(A_MOVE move)
  {
    moves.add(new Move(this.nodeFor(move.dst), this.nodeFor(move.src)));
  }

  @Override
  public List<Move> moves()
  {
    return moves;
  }
  
  public String toString() {
    StringWriter out = new StringWriter();
    this.dump(new IndentingWriter(out));
    moves.dump(new IndentingWriter(out));
    return out.toString();
  }

}
