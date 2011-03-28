package minijava.translate.implementation;

import minijava.ir.temp.Label;
import minijava.ir.tree.CJUMP.RelOp;
import minijava.ir.tree.IR;
import minijava.ir.tree.IRExp;
import minijava.ir.tree.IRStm;

public class RelCx extends TranslateCx
{
  RelOp relOp;
  IRExp l;
  IRExp r;
  
  public RelCx(RelOp relOp, IRExp l, IRExp r)
  {
    this.relOp = relOp;
    this.l = l;
    this.r = r;
  }

  @Override
  public IRStm unCx(Label t, Label f)
  {
    return IR.CJUMP(relOp, l, r, t, f);
  }

}
