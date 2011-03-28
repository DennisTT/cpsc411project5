package minijava.translate.implementation;

import minijava.ir.temp.Label;
import minijava.ir.tree.IRExp;
import minijava.ir.tree.IRStm;

public abstract class TranslateExp
{
  public abstract IRExp unEx();
  public abstract IRStm unNx();
  public abstract IRStm unCx(Label t, Label f);
}
