package minijava.translate.implementation;

import minijava.ir.temp.Label;
import minijava.ir.temp.Temp;
import minijava.ir.tree.IR;
import minijava.ir.tree.IRExp;
import minijava.ir.tree.IRStm;

public abstract class TranslateCx extends TranslateExp
{
  @Override
  public IRExp unEx()
  {
    Temp r  = new Temp();
    Label t = Label.gen();
    Label f = Label.gen();
    
    return IR.ESEQ( IR.SEQ( IR.MOVE(IR.TEMP(r),
                                    IR.CONST(1)),
                            IR.SEQ( this.unCx(t, f),
                                    IR.SEQ( IR.LABEL(f),
                                            IR.SEQ( IR.MOVE(IR.TEMP(r),
                                                            IR.CONST(0)),
                                                    IR.LABEL(t))))),
                    IR.TEMP(r));
  }
  
  @Override
  public IRStm unNx()
  {
    Label t = Label.gen();
    Label f = Label.gen();
    
    return IR.SEQ(this.unCx(t, f), IR.SEQ(IR.LABEL(t), IR.LABEL(f)));
  }
}
