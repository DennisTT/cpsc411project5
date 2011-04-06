package minijava.analysis.implementation;

import minijava.analysis.InterferenceGraph;
import minijava.codegen.assem.A_MOVE;
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

}
