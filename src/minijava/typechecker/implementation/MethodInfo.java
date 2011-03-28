package minijava.typechecker.implementation;

import java.util.ArrayList;
import java.util.Iterator;

import minijava.ast.Type;
import minijava.util.FunTable;
import minijava.util.Indentable;
import minijava.util.IndentingWriter;

public class MethodInfo implements Info, Indentable {
  public Type returnType;
  public ArrayList<VarInfo> formalsList;
  public FunTable<Info> formals;
  public FunTable<Info> locals;
  
  @Override
  public void dump(IndentingWriter out) {
    out.println("MethodInfo {");
    out.indent();
    
    out.println("returnType " + this.returnType);
    
    out.print("formalsList [");
    if(this.formalsList.size() > 0)
    {
      Iterator<VarInfo> it = this.formalsList.iterator();
      while(it.hasNext())
      {
        it.next().dump(out);
      }
    }
    out.println("]");
    
    out.print("formals ");
    this.formals.dump(out);
    out.println();
    
    out.print("locals ");
    this.locals.dump(out);
    out.println();
    
    out.outdent();
    out.print("}");
  }
}
