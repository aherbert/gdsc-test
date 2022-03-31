String Templates
----------------

Contains template files that are parsed into java source code using the StringTemplate
library. Template substitution functions from the StringTemplate library are
called from a code generator application based on a configurable properties file.
The generator is built in the gdsc-test-generator module.

Suffix        File
.st           Template file
.properties   Properties file

Entries in a properties file are key = value pairs.

The key is split using the . character into a scope and substitution word.
The substitution word is expected to be replaced with some other text in
the context of the scope.

Substitutions
-------------

The value is a space delimited list of substitutions.
Optional field delimiter is ".
Special characters are escape using \\.
The string \\N is replaced with a null substitution.
This allows the empty, null or whitespace-only string to be passed, e.g.
  key = "" \\N " " value4
creates a list of:
  {"", null, " ", "value4"}

Scopes
------

classname scope

The 'classname.' prefix is used to replace the substitution word with each
entry in the substitution list.

The substitution word must be in the Java class name. The generator will
produce a new Java file for each value in the list of substitutions.

The substitution word is also used per class as a pattern substitute in upper
and lower case.
E.G.
  classname.Type = Double Int
Produces two classes with the substitution word 'Type' in the class name substituted
for Double and Int. The contents of the template have the following substitutions:
<Type> => (Double|Int)
<type> => (double|int)

If multiple 'classname.' scope keys are used the number of elements must match the
length the first 'classname.' scope key.


class scope

The 'class.' prefix is used to replace the substitution word with each
entry in the substitution list.

The substitution is done per output Java class and so
the number of elements must match the length of the first 'classname.' scope key.
This is not valid if a 'classname.' scope key is absent.
E.G.
  class.other = Long Integer
Produces the substitution <other> => Long for the first class, and <other> => Integer
for the second class.


template scope

Entries with the 'template.' prefix are injected into the template once.

A list of substitutions can be used to match a repeated code block:
template.types = float int
with template:
<types:{each | <each> value = 1;
}>
produces:
float value = 1;
int value = 1;
