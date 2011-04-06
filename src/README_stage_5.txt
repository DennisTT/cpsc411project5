This README file is intended for comments the TA should read before 
marking your project.

At the very least YOU MUST INCLUDE THE NAMES OF EACH MEMBER OF YOUR TEAM.

Team member 1: Jeff Cheung  66994062  p3c6
Team member 2: Dennis Tsang 69972065  s0g6

If you changed any code outside of the minjava.translate.implementation package, 
please include a *brief* explanation here what you changed and why:

  - Fixed new object allocation size from 4 to 0 for class constructors with no
    arguments in TranslateVisitor because it was inconsistent with the expected
    output
  - Fixed register names in X86Frame because of missing "%" prefix
  - Fixed null exceptions related to A_OPER source and destination list
    arguments by using empty Lists
  - Corrected CALL MunchRule in X86Muncher, and added SP(), calleeSaves(), and
    callerSaves() in X86Frame, to generate argument passing instructions
    relative to %esp instead of %ebp for nicer code
  - Removed pushl assembly instructions from CALL MunchRule in X86Muncher for it
    was not necessary
  - Removed "fall-through jump instruction" from CJUMP MunchRule in X86Muncher
    for it was not necessary
  - Added assembly instruction optimizations in X86Muncher to better coincide
    with expected output
  - Formatted assembly code string to better coincide with expected output

Additional comments to the TA (if any): 

  - All test suites should now pass with the exception of
    TestTypeCheckPhase1Internal (which is not mandatory to pass)
