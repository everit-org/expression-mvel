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
package org.everit.expression.mvel.internal;

import java.io.Serializable;
import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.mvel2.CompileException;
import org.mvel2.MVEL;
import org.mvel2.PropertyAccessException;

/**
 * The MVEL {@link CompiledExpression}.
 */
public class MvelCompiledExpression implements CompiledExpression {

  private final int column;

  private final String expression;

  private final int lineNumber;

  private final Serializable mvelExpression;

  /**
   * Constructor.
   */
  public MvelCompiledExpression(final String expression, final Serializable mvelExpression,
      final int lineNumber,
      final int column) {
    this.expression = expression;
    this.mvelExpression = mvelExpression;
    this.lineNumber = lineNumber;
    this.column = column;
  }

  @Override
  public Object eval(final Map<String, Object> vars) {
    try {
      return MVEL.executeExpression(mvelExpression, vars);
    } catch (CompileException e) {
      e.getMessage();
      if (e.getLineNumber() == 1) {
        e.setColumn((e.getColumn() + column) - 1);
      }
      e.setLineNumber((e.getLineNumber() + lineNumber) - 1);

      throw e;
    } catch (NoClassDefFoundError e) {
      PropertyAccessException pe = new PropertyAccessException(
          "Type of variable or property could not be resolved during evaluating expression.",
          expression.toCharArray(), 0, e, null);

      pe.setColumn(column);
      pe.setLineNumber(lineNumber);
      throw pe;
    }
  }

}
