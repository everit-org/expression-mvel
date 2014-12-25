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

public class MvelTest {

    @Test
    public void testExceptionWithLineNum() {
        MvelExpressionCompiler compiler = new MvelExpressionCompiler();
        ParserConfiguration parserContext = new ParserConfiguration(this.getClass().getClassLoader());

        parserContext.setStartColumn(5);
        parserContext.setStartRow(3);

        String testExpression = " \n   a.xx()";
        CompiledExpression compiled = compiler.compile(testExpression, parserContext);
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
        CompiledExpression compiled = compiler.compile(testExpression, new ParserConfiguration(this.getClass()
                .getClassLoader()));
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
