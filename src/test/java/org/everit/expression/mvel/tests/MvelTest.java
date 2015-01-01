/**
 * This file is part of Everit - Expression MVEL.
 *
 * Everit - Expression MVEL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Everit - Expression MVEL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Everit - Expression MVEL.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        CompiledExpression compiledExpression = compiler.compile(testExpression, parserContext);
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("a", "test");
        try {
            compiledExpression.eval(vars);
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
