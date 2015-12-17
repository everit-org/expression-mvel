/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.biz)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
      System.out.println(e.getMessage());
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
