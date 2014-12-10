package org.everit.expression.mvel.tests;

import java.util.HashMap;
import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.everit.expression.ParserConfiguration;
import org.everit.expression.mvel.MvelExpressionCompiler;
import org.junit.Assert;
import org.junit.Test;

public class MvelTest {

    @Test
    public void testExceptionWithLineNum() {
        MvelExpressionCompiler compiler = new MvelExpressionCompiler();
        ParserConfiguration parserContext = new ParserConfiguration(this.getClass().getClassLoader());

        parserContext.setColumn(5);
        parserContext.setLineNumber(3);

        String testExpression = " \n   fff.xx()";
        CompiledExpression compiled = compiler.compile(testExpression.toCharArray(), 0, testExpression.length(),
                parserContext);
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("a", 0);
        try {
            compiled.eval(vars);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSimpleExpression() {
        MvelExpressionCompiler compiler = new MvelExpressionCompiler();
        String testExpression = "  a.intValue() + b";
        CompiledExpression compiled = compiler.compile(testExpression.toCharArray(), 2, testExpression.length() - 2,
                new ParserConfiguration(this.getClass().getClassLoader()));
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("a", 1);
        vars.put("b", 2);
        for (int i = 0; i < 20000; i++) {
            compiled.eval(vars);
        }
        Number eval = (Number) compiled.eval(vars);
        Assert.assertEquals(3, eval.intValue());
    }
}
