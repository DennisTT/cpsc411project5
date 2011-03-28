package minijava.translate.implementation;

import java.util.Iterator;
import java.util.Stack;

import junit.framework.Assert;
import minijava.ast.AST;
import minijava.ast.And;
import minijava.ast.ArrayAssign;
import minijava.ast.ArrayLength;
import minijava.ast.ArrayLookup;
import minijava.ast.Assign;
import minijava.ast.Block;
import minijava.ast.BooleanLiteral;
import minijava.ast.BooleanType;
import minijava.ast.Call;
import minijava.ast.ClassDecl;
import minijava.ast.IdentifierExp;
import minijava.ast.If;
import minijava.ast.IntArrayType;
import minijava.ast.IntegerLiteral;
import minijava.ast.IntegerType;
import minijava.ast.LessThan;
import minijava.ast.MainClass;
import minijava.ast.MethodDecl;
import minijava.ast.Minus;
import minijava.ast.NewArray;
import minijava.ast.NewObject;
import minijava.ast.NodeList;
import minijava.ast.Not;
import minijava.ast.ObjectType;
import minijava.ast.Plus;
import minijava.ast.Print;
import minijava.ast.Program;
import minijava.ast.This;
import minijava.ast.Times;
import minijava.ast.VarDecl;
import minijava.ast.While;
import minijava.ir.frame.Access;
import minijava.ir.frame.Frame;
import minijava.ir.temp.Label;
import minijava.translate.Fragment;
import minijava.translate.Fragments;
import minijava.translate.Translator;
import minijava.util.List;
import minijava.visitor.Visitor;

public class SymbolTableBuilderVisitor implements Visitor<SymbolTable>
{
  private Fragments     fragments,
                        classFragments;
  
  private Stack<Frame>  frames = new Stack<Frame>();
  private SymbolTable   symbols = new SymbolTable();
  private String        currentClass,
                        currentMethod;
  
  public SymbolTableBuilderVisitor(Frame frameFactory, Fragments fragments)
  {
    this.frames.push(frameFactory);
    this.fragments = fragments;
    this.classFragments = new Fragments(frameFactory);
  }
  
  @Override
  public <T extends AST> SymbolTable visit(NodeList<T> ns)
  {
    // Iterate all nodes and generate IR
    for(int i = 0; i < ns.size(); ++i) { ns.elementAt(i).accept(this); }
    return null;
  }
  
  @Override
  public SymbolTable visit(Program n)
  {
    // Make sure class declarations are defined before mainClass is visited
    n.classes.accept(this);
    n.mainClass.accept(this);
    
    // Make sure mainClass Fragment from is defined before class Fragments
    Iterator<Fragment> it = this.classFragments.iterator();
    while(it.hasNext())
    {
      this.fragments.add(it.next());
    }
    
    return this.symbols;
  }
  
  @Override
  public SymbolTable visit(MainClass n)
  {
    List<Boolean> frameParams = List.empty();
    this.frames.push(this.createNewFrame(Translator.L_MAIN, frameParams));
    this.currentClass = n.className;
    this.currentMethod = "main";
    this.currentClass = this.currentMethod = null;
    this.frames.pop();
    
    return null;
  }

  @Override
  public SymbolTable visit(ClassDecl n)
  {
    this.currentClass = n.name;
    
    n.vars.accept(this);
    n.methods.accept(this);

    this.currentClass = null;

    return null;
  }

  @Override
  public SymbolTable visit(VarDecl n)
  {
    Access var;
    
    if(this.currentMethod != null)
    {
      // Allocate local method variable on current frame
      var = this.frames.peek().allocLocal(false);
      this.symbols.addClassMethodVar( this.currentClass,
                                      this.currentMethod,
                                      n.name,
                                      var);
      return null;
    }

    // Allocate class variable
    var = this.frames.peek().alloc(this.symbols.numClassVars(this.currentClass) * this.frames.peek().wordSize());
    this.symbols.addClassVar(this.currentClass, n.name, var);
    return null;
  }

  @Override
  public SymbolTable visit(MethodDecl n)
  {
    this.currentMethod = n.name;
    
    this.addClassMethod(n.name);
    
    // Include implicit argument for "this" in method frame
    List<Boolean> frameParams = List.list(true);
    int numFormals = n.formals.size();
    for(int i = 0; i < numFormals; ++i)
    {
      frameParams.add(true);
    }
    this.frames.push(this.createNewFrame(Label.get(n.name), frameParams));
    
    // Keep track of formal variables
    this.addFormal(0, "this");
    for(int j = 0; j < numFormals; ++j)
    {
      this.addFormal(j + 1, n.formals.elementAt(j).name);
    }
    
    // Allocate space for variables
    n.vars.accept(this);
    
    this.currentMethod = null;
    this.frames.pop();
    
    return null;
  }

  @Override
  public SymbolTable visit(IntArrayType n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(BooleanType n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(IntegerType n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(ObjectType n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(Block n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(If n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(While n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(Print n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(Assign n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(ArrayAssign n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(And n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(LessThan n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(Plus n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(Minus n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(Times n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(ArrayLookup n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(ArrayLength n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(Call n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(IntegerLiteral n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(BooleanLiteral n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(IdentifierExp n)
  {
    // No translation required
    return null;
  }
  
  @Override
  public SymbolTable visit(This n)
  {
    // No translation required
    return null;
  }
  
  @Override
  public SymbolTable visit(NewArray n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(NewObject n)
  {
    // No translation required
    return null;
  }

  @Override
  public SymbolTable visit(Not not)
  {
    // No translation required
    return null;
  }
  
  // ---------------------------------------------------------------------------
  
  private Frame createNewFrame(Label name, List<Boolean> formalsEscape)
  {
    return this.frames.peek().newFrame(name, formalsEscape);
  }
  
  // Adds the specified method as a symbol entry to the current class
  private void addClassMethod(String methodName)
  {
    this.symbols.addClassMethod(this.currentClass, methodName);
  }
  
  // Adds the specified formal as a symbol entry to the current class method
  private Access addFormal(int i, String id)
  {
    Assert.assertNotNull(this.currentMethod);
    
    Access var = this.frames.peek().getFormal(i);
    this.symbols.addClassMethodVar( this.currentClass,
                                    this.currentMethod,
                                    id,
                                    var);
    return var;
  }
  
}
