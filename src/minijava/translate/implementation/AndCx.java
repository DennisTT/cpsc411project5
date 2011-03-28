package minijava.translate.implementation;

import minijava.ir.temp.Label;
import minijava.ir.tree.IR;
import minijava.ir.tree.IRStm;
import minijava.ir.tree.CJUMP.RelOp;

public class AndCx extends TranslateCx
{
  TranslateExp e1, e2;
  
  public AndCx(TranslateExp e1, TranslateExp e2)
  {
    this.e1 = e1;
    this.e2 = e2;
  }
  
  @Override
  public IRStm unCx(Label t, Label f)
  {
    Label e2Test = Label.generate("e2Test");
    
    return IR.SEQ( IR.CJUMP(RelOp.EQ, e1.unEx(), IR.TRUE, e2Test, f),
                            IR.LABEL(e2Test),
                            e2.unCx(t, f));
  }

}
