This README file is intended for comments the TA should read before 
marking your project.

At the very least YOU MUST INCLUDE THE NAMES OF EACH MEMBER OF YOUR TEAM.

Team member 1: Jeff Cheung  66994062  p3c6
Team member 2: Dennis Tsang 69972065  s0g6

If you changed any code outside of 
  - minijava.codegen.x86 (for the code generator) 
  - minijava.analysis.implementation (for the liveness analysis)
please include a *brief* explanation here what you changed and why:

  - Added public Access alloc(int offset) to Frame.java to allow for
    TranslateVisitor.java to be able to generate the proper IR code when
    accessing class variables. Note that TranslateVisitor does not actually use
    this method to allocate space, as object allocation is done on the heap
    rather than the stack.

Additional comments to the TA (if any): 

  None
