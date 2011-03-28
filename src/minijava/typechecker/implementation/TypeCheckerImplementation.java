package minijava.typechecker.implementation;

import minijava.ast.AST;
import minijava.typechecker.ErrorReport;
import minijava.typechecker.TypeChecked;
import minijava.typechecker.TypeCheckerException;
import minijava.util.FunTable;

public class TypeCheckerImplementation {

	private AST program;
	private FunTable<Info> classTable;
	
	public TypeCheckerImplementation(AST program)
	{
	  this.program = program;
	}
	
	public TypeChecked typeCheck() throws TypeCheckerException
	{
    this.buildClassTable();
    
    ErrorReport e = new ErrorReport();
	  TypeChecked t = this.program.accept(new TypeCheckVisitor( this.program,
	                                                            this.classTable,
	                                                            e));
    e.close();
	  return t;
	}

	public Object buildClassTable()
	{
	  this.classTable = this.program.accept(new ClassTableVisitor());
	  return this.classTable;
	}
}
