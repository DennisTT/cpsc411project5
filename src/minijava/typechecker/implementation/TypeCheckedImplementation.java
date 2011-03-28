package minijava.typechecker.implementation;

import minijava.ast.AST;
import minijava.ast.Type;
import minijava.typechecker.TypeChecked;
import minijava.util.FunTable;

public class TypeCheckedImplementation extends TypeChecked
{
  public Type           type;
  public AST            program;
  public FunTable<Info> classTable;
  
  public TypeCheckedImplementation(Type type, AST program, FunTable<Info> classTable)
  {
    this.type = type;
    this.program = program;
    this.classTable = classTable;
  }
}
