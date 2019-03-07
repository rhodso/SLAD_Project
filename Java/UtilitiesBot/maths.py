import sys
print(str(eval(sys.argv[1])))

#   The Above two lines are so we don't need to write a maths expression parser
#   A maths expression parser is something like:
#       Take 1+2 as a string
#       Get the + operator
#       Get the 1 as the first opperand
#       Get the 2 as the second opperand
#       Perform the operation
#       Get the result
#       Output the result
#   Since this is built into python, let's just write 2 lines of python to do this
#   which will be far easier than writing the expression parser in java
