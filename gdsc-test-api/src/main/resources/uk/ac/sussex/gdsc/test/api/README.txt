# Entries in a properties file are key = value pairs.
#
# The key is split using the . character into a scope and substitution word.
# The substitution word is expected to be replaced with some other text in 
# the context of the scope.
#
# The value is a space delimited list of substitutions

# classname scope
#
# The 'classname.' prefix is used to replace the substitution word with each 
# the entry in the substitution list.
# The substitution word must be in the Java class name. The generator will
# produce a new Java file for each value in the list of substitutions.
# The substitution word is also used per class as a pattern substitute in upper
# and lower case.
# E.G.
#   classname.Type = Double Int
# Produces two classes with the substitution word 'Type' in the class name substituted 
# for Double and Int. The contents of the template have the following substitutions:
# <Type> => (Double|Int) 
# <type> => (double|int)
# If repeated the number of elements must match the length of the first 'classname.'
# scope key.

# class scope
#
# The 'class.' prefix is used to replace the substitution word with each 
# the entry in the substitution list.
# The substritution is done per output Java class and so
# the number of elements must match the length of the first 'classname.' scope key.
# This is not valid if a 'classname.' scope key is absent.
# e.g. class.other = one two

# template scope
#
# Entries with the 'template.' prefix are injected into the template once.
# A list of substitutions can be used to match a repeated code block:
# template.types = float int
# with template:
# <types:{each | <each> value = 1;
# }>
# produces:
# float value = 1;
# int value = 1;
