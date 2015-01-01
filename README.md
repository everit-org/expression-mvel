expression-mvel
===============

MVEL based implementation of the Everit Expression API.

## Usage

The following code snippet compiles an expression and evaluates it:

    MvelExpressionCompiler compiler = new MvelExpressionCompiler();

    String testExpression = "a + b";

    CompiledExpression compiledExpression = compiler.compile(testExpression,
            new ParserConfiguration(this.getClass().getClassLoader()));

    Map<String, Object> vars = new HashMap<String, Object>();
    vars.put("a", 1);
    vars.put("b", 2);

    Integer result = compiledExpression.eval(vars);
    System.out.println(result);

Please note that it is recommended to keep the compiled expression and reuse
it! The library is based on [MVEL][1]. The syntax of MVEL expressions can be
used.

## Known issues

In case of an exception during evaluation the column and row is not
calculated well. This is an issue of MVEL as sometimes the thrown exception
contains wrong position.

[1]: http://mvel.codehaus.org/Language+Guide+for+2.0