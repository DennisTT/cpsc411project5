package minijava.typechecker.implementation;

import minijava.util.FunTable;
import minijava.util.Indentable;
import minijava.util.IndentingWriter;

public class ClassInfo implements Info, Indentable {
  public String name;
  public String superClass;
  public FunTable<Info> fields;
  public FunTable<Info> methods;
  
  @Override
  public void dump(IndentingWriter out)
  {
    out.println("class " + this.name + " extends " + this.superClass + " {");
    out.indent();
    
    out.print("fields ");
    this.fields.dump(out);
    out.println();
    
    out.print("methods ");
    this.methods.dump(out);
    out.println();
    
    out.outdent();
    out.print("}");
  }
}
