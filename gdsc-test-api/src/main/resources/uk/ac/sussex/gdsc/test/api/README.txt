# The 'classname.' prefix is used to replace the Class name with each 
# the entry in the list.
# The key must be in the class name.
# The key is also used per class as a pattern substitute in upper
# and lower case.
# E.G.
#   class.Type = Double Int
# Produces two classes with Type in the class name substituted 
# for Double and Int. The contents of the template have the following substitutions:
# <Type> => (Double|Int) 
# <type> => (double|int)
# If repeated the number of elements must match the length of the first 'classname.' prefix.

# The 'class.' prefix is used to replace the pattern with the given text,
# one per class. This number of elements must match the length of 'classname.' prefix.
# This is not valid if a 'classname.' prefix is absent.
# e.g. class.other = one two

# Entries with the 'template.' prefix are injected into the template once.
# Whitespace can be used to match a repeated code block:
# template.types = float int
# with template:
# <types:{each | <each> value = 1;
# }>
# produces:
# float value = 1;
# int value = 1;
