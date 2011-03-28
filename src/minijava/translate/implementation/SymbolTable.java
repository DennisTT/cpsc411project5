package minijava.translate.implementation;

import java.util.HashMap;
import java.util.Set;

import minijava.ir.frame.Access;

public class SymbolTable
{
  private HashMap<String, ClassTable> table = new HashMap<String, ClassTable>();
  
  public void addClassMethod(String className, String methodName)
  {
    if(!this.table.containsKey(className))
    {
      this.table.put(className, new ClassTable());
    }
    
    this.table.get(className).addMethod(methodName);
  }
  
  public void addClassMethodVar(String className, String methodName, String id, Access var)
  {
    if(!this.table.containsKey(className))
    {
      this.table.put(className, new ClassTable());
    }
    
    this.table.get(className).addMethodVar(methodName, id, var);
  }
  
  public void addClassVar(String className, String id, Access var)
  {
    if(!this.table.containsKey(className))
    {
      this.table.put(className, new ClassTable());
    }
    
    this.table.get(className).addClassVar(id, var);
  }
  
  public Set<String> lookupClassVars(String className)
  {
    if(!this.table.containsKey(className)) { return null; }
    
    return this.table.get(className).classVars();
  }
  
  public Access lookupClassVar(String className, String id)
  {
    if(!this.table.containsKey(className)) { return null; }
    
    return this.table.get(className).lookupClassVar(id);
  }
  
  public Access lookupMethodVar(String className, String methodName, String id)
  {
    if(!this.table.containsKey(className)) { return null; }
    
    return this.table.get(className).lookupMethodVar(methodName, id);
  }
  
  public int numClassVars(String className)
  {
    Set<String> vars = this.lookupClassVars(className);
    return (vars != null) ? vars.size() : 0;
  }
  
  // -------------------------------------------------------------------------
  
  private class ClassTable
  {
    private HashMap<String, Access>                  vars     = new HashMap<String, Access>();
    private HashMap<String, HashMap<String, Access>> methods  = new HashMap<String, HashMap<String, Access>>();
    
    public void addClassVar(String id, Access var)
    {
      this.vars.put(id, var);
    }
    
    public void addMethod(String methodName)
    {
      if(!this.methods.containsKey(methodName))
      {
        this.methods.put(methodName, new HashMap<String, Access>());
      }
    }
    
    public void addMethodVar(String methodName, String id, Access var)
    {
      this.addMethod(methodName);
      this.methods.get(methodName).put(id, var);
    }
    
    public Set<String> classVars()
    {
      return this.vars.keySet();
    }
    
    public Access lookupClassVar(String id)
    {
      return this.vars.get(id);
    }
    
    public Access lookupMethodVar(String methodName, String id)
    {
      if(!this.methods.containsKey(methodName)) { return null; }
      
      return this.methods.get(methodName).get(id);
    }
  }
}
