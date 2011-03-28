package minijava.translate.implementation;

import minijava.ir.temp.Label;
import minijava.ir.tree.IRExp;
import minijava.ir.tree.IRStm;

public class TranslateNx extends TranslateExp
{
  private IRStm stm;
  
  public TranslateNx(IRStm s)
  {
    this.stm = s;
  }
  
  @Override
  public IRExp unEx()
  {
    // This method should never be called for TranslateNx
    // Do nothing
    return null;
  }
  
  @Override
  public IRStm unNx()
  {
    return this.stm;
  }
  
  @Override
  public IRStm unCx(Label t, Label f)
  {
    // This method should never be called for TranslateNx
    // Do nothing
    return null;
  }
}
