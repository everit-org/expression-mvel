package org.everit.expression.mvel.tests;

import java.util.HashMap;
import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.everit.expression.ParserConfiguration;
import org.everit.expression.mvel.MvelExpressionCompiler;
import org.junit.Assert;
import org.junit.Test;
import org.mvel2.CompileException;

public class MvelTest {

    @Test
    public void testExceptionDuringEvaluation() {
        MvelExpressionCompiler compiler = new MvelExpressionCompiler();
        ParserConfiguration parserContext = new ParserConfiguration(this.getClass().getClassLoader());

        parserContext.setStartColumn(11);
        parserContext.setStartRow(21);

        String testExpression = " \n   a.substring(-1)";
        CompiledExpression compiled = compiler.compile(testExpression, parserContext);
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("a", "test");
        try {
            compiled.eval(vars);
            Assert.fail("Exception should have been thrown");
        } catch (CompileException e) {
            // Positions are wrong here due to the bug of MVEL
        }
    }

    @Test
    public void testExceptionOnCompilation() {
        MvelExpressionCompiler compiler = new MvelExpressionCompiler();
        ParserConfiguration parserContext = new ParserConfiguration(this.getClass().getClassLoader());

        parserContext.setStartColumn(11);
        parserContext.setStartRow(21);

        String testExpression = " \n   1.xx()";

        try {
            compiler.compile(testExpression, parserContext);
            Assert.fail("Exception should have been thrown");
        } catch (CompileException e) {
            Assert.assertEquals(22, e.getLineNumber());
            Assert.assertEquals(4, e.getColumn());
        }

    }

    @Test
    public void testSimpleExpression() {
        MvelExpressionCompiler compiler = new MvelExpressionCompiler();

        String testExpression = "a + b";

        CompiledExpression compiledExpression = compiler.compile(testExpression,
                new ParserConfiguration(this.getClass().getClassLoader()));

        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("a", 1);
        vars.put("b", 2);
        Number eval = (Number) compiledExpression.eval(vars);
        Assert.assertEquals(3, eval.intValue());
    }
}
