package minijava.typechecker.implementation;

import java.util.ArrayList;

import minijava.ast.*;
import minijava.util.FunTable;
import minijava.visitor.Visitor;

public class ClassTableVisitor implements Visitor<FunTable<Info>>
{
  @Override
  public <T extends AST> FunTable<Info> visit(NodeList<T> n)
  {
    FunTable<Info> t = FunTable.theEmpty();
    for(int i = n.size() - 1; i >= 0; --i)
    {
      // Iterate all nodes and find symbols
      t = t.merge(n.elementAt(i).accept(this));
    }
    
    return t;
  }

  @Override
  public FunTable<Info> visit(Program n)
  {
    // Find symbols in MainClass and other classes
    return n.mainClass.accept(this)
                      .merge(n.classes.accept(this));
  }

  @Override
  public FunTable<Info> visit(MainClass n)
  {
    FunTable<Info> t = FunTable.theEmpty();
    
    // Create main MethodInfo declaration
    MethodInfo m  = new MethodInfo();
    m.formals     = t;
    m.locals      = t;
    m.formalsList = new ArrayList<VarInfo>();
    
    // Create main ClassInfo declaration
    ClassInfo c = new ClassInfo();
    c.name      = n.className;
    c.fields    = t;
    c.methods   = t.insert("main", m);
    
    // Add to symbol table
    // Find symbols in statements and combine with the symbol table
    return t.insert(n.className, c)
            .merge(n.statement.accept(this));
  }

  @Override
  public FunTable<Info> visit(ClassDecl n)
  {
    // Create ClassInfo declaration
    ClassInfo c   = new ClassInfo();
    c.name        = n.name;
    c.superClass  = n.superName;
    c.fields      = n.vars.accept(this);
    c.methods     = n.methods.accept(this);
    
    FunTable<Info> t = FunTable.theEmpty();
    return t.insert(n.name, c);
  }

  @Override
  public FunTable<Info> visit(VarDecl n)
  {
    // Create VarInfo declaration
    VarInfo v = new VarInfo();
    v.kind    = n.kind;
    v.type    = n.type;
    
    FunTable<Info> t = FunTable.theEmpty();
    return t.insert(n.name, v);
  }

  @Override
  public FunTable<Info> visit(MethodDecl n)
  {
    // Create MethodInfo declaration
    MethodInfo m  = new MethodInfo();
    m.formals     = n.formals.accept(this);
    m.formalsList = new ArrayList<VarInfo>();
    m.locals      = n.vars.accept(this);
    m.returnType  = n.returnType;
    
    // Create VarInfo declarations for each argument
    for(int i = 0; i < n.formals.size(); ++i)
    {
      VarDecl d   = n.formals.elementAt(i);
      VarInfo v   = new VarInfo();
      v.kind      = d.kind;
      v.type      = d.type;
      m.formalsList.add(v);
    }
    
    // Add to symbol table
    // Find symbols in statements and return expressions and combine
    FunTable<Info> t = FunTable.theEmpty();
    return t.insert(n.name, m)
            .merge( n.statements.accept(this)
                                .merge(n.returnExp.accept(this)));
  }

  @Override
  public FunTable<Info> visit(IntArrayType n) { return FunTable.theEmpty(); }

  @Override
  public FunTable<Info> visit(BooleanType n)  { return FunTable.theEmpty(); }

  @Override
  public FunTable<Info> visit(IntegerType n)  { return FunTable.theEmpty(); }

  @Override
  public FunTable<Info> visit(ObjectType n)   { return FunTable.theEmpty(); }

  @Override
  public FunTable<Info> visit(Block n)
  {
    // Find symbols in statements
    return n.statements.accept(this);
  }

  @Override
  public FunTable<Info> visit(If n)
  {
    // Find symbols in condition, then, and else statements
    return n.tst.accept(this)
                .merge(n.thn.accept(this)
                            .merge(n.els.accept(this)));
  }

  @Override
  public FunTable<Info> visit(While n)
  {
    // Find symbols in condition and body statements
    return n.tst.accept(this)
                .merge(n.body.accept(this));
  }

  @Override
  public FunTable<Info> visit(Print n)
  {
    // Find symbols in print statement
    return n.exp.accept(this);
  }

  @Override
  public FunTable<Info> visit(Assign n)
  {
    // Find symbols in value
    return n.value.accept(this);
  }

  @Override
  public FunTable<Info> visit(ArrayAssign n)
  {
    // Find symbols in index and value
    return n.index.accept(this)
                  .merge(n.value.accept(this));
  }

  @Override
  public FunTable<Info> visit(And n)
  {
    // Find symbols in left and right expressions
    return  n.e1.accept(this)
                .merge(n.e2.accept(this));
  }

  @Override
  public FunTable<Info> visit(LessThan n)
  {
    // Find symbols in left and right expressions
    return  n.e1.accept(this)
                .merge(n.e2.accept(this));
  }

  @Override
  public FunTable<Info> visit(Plus n)
  {
    // Find symbols in left and right expressions
    return  n.e1.accept(this)
                .merge(n.e2.accept(this));
  }

  @Override
  public FunTable<Info> visit(Minus n)
  {
    // Find symbols in left and right expressions
    return  n.e1.accept(this)
                .merge(n.e2.accept(this));
  }

  @Override
  public FunTable<Info> visit(Times n)
  {
    // Find symbols in left and right expressions
    return  n.e1.accept(this)
                .merge(n.e2.accept(this));
  }

  @Override
  public FunTable<Info> visit(ArrayLookup n)
  {
    // Find symbols in index and array expressions
    return n.index.accept(this)
                  .merge(n.array.accept(this));
  }

  @Override
  public FunTable<Info> visit(ArrayLength n)
  {
    // Find symbols in array expression
    return n.array.accept(this);
  }

  @Override
  public FunTable<Info> visit(Call n)
  {
    // Find symbols in receiver and rands expressions and combine
    return  n.receiver.accept(this)
                      .merge(n.rands.accept(this));
  }

  @Override
  public FunTable<Info> visit(IntegerLiteral n) { return FunTable.theEmpty(); }

  @Override
  public FunTable<Info> visit(BooleanLiteral n) { return FunTable.theEmpty(); }

  @Override
  public FunTable<Info> visit(IdentifierExp n)  { return FunTable.theEmpty(); }

  @Override
  public FunTable<Info> visit(This n)           { return FunTable.theEmpty(); }

  @Override
  public FunTable<Info> visit(NewArray n)
  {
    // Find symbols in size expression
    return n.size.accept(this);
  }

  @Override
  public FunTable<Info> visit(NewObject n)      { return FunTable.theEmpty(); }

  @Override
  public FunTable<Info> visit(Not n)
  {
    // Find symbols in boolean expression
    return n.e.accept(this);
  }
}
