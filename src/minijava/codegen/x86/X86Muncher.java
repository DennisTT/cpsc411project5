package minijava.codegen.x86;

import static minijava.codegen.patterns.IRPat.*;
import static minijava.util.List.list;
import minijava.codegen.assem.A_LABEL;
import minijava.codegen.assem.A_MOVE;
import minijava.codegen.assem.A_OPER;
import minijava.codegen.assem.Instr;
import minijava.codegen.muncher.MunchRule;
import minijava.codegen.muncher.Muncher;
import minijava.codegen.muncher.MuncherRules;
import minijava.codegen.patterns.Matched;
import minijava.codegen.patterns.Pat;
import minijava.ir.frame.Frame;
import minijava.ir.temp.Label;
import minijava.ir.temp.Temp;
import minijava.ir.tree.CJUMP.RelOp;
import minijava.ir.tree.IRExp;
import minijava.ir.tree.IRStm;
import minijava.util.List;

public class X86Muncher extends Muncher
{
  /**
   * Empty list of Temps, use this  constant if you experience problems with just
   * using List.empty() (the Java type ckecker doesn't seem to like it
   * because it sometimes can't infer the type from its usage context.)
   */
  private static final List<Temp> noTemps = List.empty();
  
  private static MuncherRules<IRStm, Void> sm = new MuncherRules<IRStm, Void>();
  private static MuncherRules<IRExp, Temp> em = new MuncherRules<IRExp, Temp>();
  
  public X86Muncher(Frame frame) {
    super(frame, sm, em);
  }
  
  //////////// The munching rules ///////////////////////////////
  
  static { //Done only once, at class loading time.
    
    // Pattern "variables" (used by the rules below)
    
    final Pat<IRExp>         _e_ = Pat.any();
    final Pat<IRExp>         _f_ = Pat.any();
    final Pat<Temp>          _t_ = Pat.any();
    final Pat<Integer>       _i_ = Pat.any();
    
    final Pat<Label>        _l_ = Pat.any();
    final Pat<Label>        _m_ = Pat.any();
    final Pat<List<IRExp>>  _es_  = Pat.any();
    final Pat<RelOp>        _op_  = Pat.any();
    
    // An example of a Stm muncher rule:
    sm.add(new MunchRule<IRStm, Void>( MOVE(TEMP(_t_), _e_) ) {
      @Override
      protected Void trigger(Muncher m, Matched c) {
        m.emit(A_MOV( c.get(_t_),
                  m.munch(c.get(_e_)) ));
        return null;
      }
    });
    
    // An example of an Exp muncher rule
    em.add(new MunchRule<IRExp, Temp>(PLUS(_e_, CONST(_i_))) {
      @Override
      protected Temp trigger(Muncher m, Matched c) {
        Temp sum = new Temp();
        m.emit( A_MOV(sum, m.munch(c.get(_e_))) );
        m.emit( A_ADD(sum, c.get(_i_)) );
        return sum;
      }
    });
    
    // LABEL
    sm.add(new MunchRule<IRStm, Void>(LABEL(_l_))
    {
      @Override
      protected Void trigger(Muncher m, Matched c)
      {
        m.emit(A_LABEL(c.get(_l_)));
        return null;
      }
    });
    
    // CALL
    em.add(new MunchRule<IRExp, Temp>(CALL(NAME(_l_), _es_))
    {
      @Override
      protected Temp trigger(Muncher m, Matched c)
      {
        Label name = c.get(_l_);
        List<IRExp> args = c.get(_es_);
        Frame f = m.getFrame();
        List<Temp> r = f.registers();
        
        // Save current registers to frame
        for(int i = 0; i < r.size(); ++i)
        {
          m.emit(new A_OPER("pushl    `s0", null, list(r.get(i))));
        }
        
        // Add arguments to frame in reverse order
        int length = args.size();
        for(int i = length - 1; i >= 0; --i)
        {
          // Munch argument and move to appropriate location
          m.emit(A_MOV(m.munch(f.getOutArg(length - i).exp(f.FP())), m.munch(args.get(i))));
        }
        
        m.emit(A_CALL(name));
        
        return m.munch(f.RV());
      }
    });
    
    // CONST
    em.add(new MunchRule<IRExp, Temp>(CONST(_i_))
    {
      @Override
      protected Temp trigger(Muncher m, Matched c)
      {
        Temp num = new Temp();
        m.emit(A_MOV_CONST(num, c.get(_i_)));
        return num;
      }
    });
    
    // PLUS
    em.add(new MunchRule<IRExp, Temp>(PLUS(_e_, _f_))
    {
      @Override
      protected Temp trigger(Muncher m, Matched c)
      {
        Temp d = m.munch(c.get(_e_));
        m.emit(A_ADD(d, m.munch(c.get(_f_))));
        return d;
      }
    });
    
    // MINUS
    em.add(new MunchRule<IRExp, Temp>(MINUS(_e_, _f_))
    {
      @Override
      protected Temp trigger(Muncher m, Matched c)
      {
        Temp d = m.munch(c.get(_e_));
        m.emit(A_SUB(d, m.munch(c.get(_f_))));
        return d;
      }
    });
    
    // MUL
    em.add(new MunchRule<IRExp, Temp>(MUL(_e_, _f_))
    {
      @Override
      protected Temp trigger(Muncher m, Matched c)
      {
        Temp d = m.munch(c.get(_e_));
        m.emit(A_MUL(d, m.munch(c.get(_f_))));
        return d;
      }
    });
    
    // MEM
    em.add(new MunchRule<IRExp, Temp>(MEM(_e_))
    {
      @Override
      protected Temp trigger(Muncher m, Matched c)
      {
        Temp res = new Temp();
        m.emit(A_MEM_READ(res, m.munch(c.get(_e_))));
        return res;
      }
    });
    
    // MEM MOVE
    sm.add(new MunchRule<IRStm, Void>(MOVE(MEM(_f_), _e_))
    {
      @Override
      protected Void trigger(Muncher m, Matched c)
      {
        m.emit(A_MEM_WRITE(m.munch(c.get(_f_)), m.munch(c.get(_e_))));
        return null;
      }
    });
    
    // TEMP
    em.add(new MunchRule<IRExp, Temp>(TEMP(_t_))
    {
      @Override
      protected Temp trigger(Muncher m, Matched c)
      {
        return c.get(_t_);
      }
    });
    
    // EXP
    sm.add(new MunchRule<IRStm, Void>(EXP(_e_))
    {
      @Override
      protected Void trigger(Muncher m, Matched c)
      {
        m.munch(c.get(_e_));
        return null;
      }
    });

    // JUMP
    sm.add(new MunchRule<IRStm, Void>(JUMP(NAME(_l_)))
    {
      @Override
      protected Void trigger(Muncher m, Matched c)
      {
        m.emit(A_JUMP(c.get(_l_)));
        return null;
      }
    });

    // CJUMP
    sm.add(new MunchRule<IRStm, Void>(CJUMP(_op_, _e_, _f_, _l_, _m_))
    {
      @Override
      protected Void trigger(Muncher m, Matched c)
      {
        Label l = c.get(_l_);
        String j = null;
        
        // Include instruction for determining jump condition flag
        m.emit(A_SUB(m.munch(c.get(_f_)), m.munch(c.get(_e_))));
        
        switch(c.get(_op_))
        {
          case EQ:
          {
            j = "je";
          }
          case GE:
          {
            j = "jge";
          }
          case GT:
          {
            j = "jg";
          }
          case LE:
          {
            j = "jle";
          }
          case LT:
          {
            j = "jl";
            break;
          }
          case NE:
          {
            j = "jne";
            break;
          }
        }
        
        // Include jump instruction for when the condition is true
        m.emit(new A_OPER(j + "    `j0", null, null, list(l)));
        
        // Include fall-through jump instruction for when the condition is false
        m.emit(A_JUMP(c.get(_m_)));
        return null;
      }
    });
  }
  
  ///////// Helper methods to generate X86 assembly instructions //////////////////////////////////////
  
  private static Instr A_ADD(Temp reg, int i) {
    return new A_OPER("addl    $"+i+", `d0", 
        list(reg),
        list(reg));
  }
  
  private static Instr A_ADD(Temp d, Temp s) {
    return new A_OPER("addl    `s0, `d0", 
        list(d),
        list(s));
  }
  
  private static Instr A_SUB(Temp d, Temp s) {
    return new A_OPER("subl    `s0, `d0", 
        list(d),
        list(s));
  }
  
  private static Instr A_MUL(Temp d, Temp s) {
    return new A_OPER("imul    `s0, `d0", 
        list(d),
        list(s));
  }
  
  private static Instr A_MOV(Temp d, Temp s) {
    return new A_MOVE("movl    `s0, `d0", d, s);
  }
  
  private static Instr A_MOV_CONST(Temp reg, int c)
  {
    return new A_OPER("movl    $" + c + ", `d0", list(reg), list(reg));
  }
  
  private static Instr A_MEM_WRITE(Temp d, Temp s)
  {
    return new A_MOVE("movl    `s0, (`d0)", d, s);
  }
  
  private static Instr A_MEM_READ(Temp d, Temp s)
  {
    return new A_MOVE("movl    (`s0), `d0", d, s);
  }
  
  private static Instr A_LABEL(Label l)
  {
    return new A_LABEL(l.toString() + ":\n", l);
  }
  
  private static Instr A_CALL(Label l)
  {
    return new A_OPER("call    " + l.toString() + "\n", noTemps, noTemps);
  }
  
  private static Instr A_JUMP(Label l)
  {
    return new A_OPER("jmp     `j0", null, null, list(l));
  }
  
  /**
   * For debugging. This shows you a representation of the actual rules in your
   * Muncher, as well as some usage statistics (how many times each rule got triggered).
   */
  public static void dumpRules() {
    System.out.println("StmMunchers: "+sm);
    System.out.println("ExpMunchers: "+em);
  }
}
