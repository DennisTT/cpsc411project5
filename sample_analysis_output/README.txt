This folder contains some sample output from running the "whileLoop" unit test for each 
analysis.

This sample output is provided so you could have a look at it to see what a good 
output looks like. It is not intended to force you make your implementation produce
output which is exactly identical.

However, you should try to ensure that your test output contains sufficient information,
in readable format that is relatively easy to inspect manually (you will not be able to
check the correctness of your algorithms except by manual inspection).

Notes:

  - the later stages (liveness and interference graph) will retain
    and print out the info from the previous stage(s).
    This is not strictly necessary but it will help a lot if you want
    to read the output and inspect it for "correctness" (i.e. you need to
    compare the result of an analysis side-by-side with the inputs of the
    analysis).
    
  - You will see that the IR code has instructions to move callee save registers
    into new Temps at the beginning of each procedure (and back into the register
    at the end).
    
    This is how in chapter 11, 12 (project 6) we will implement saving of callee save
    registers. Your IR code most likely does not have these move instructions (yet).
    So the resulting assembly, flowgraph etc. will all be slightly different from
    this sample output.
