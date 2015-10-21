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
package org.everit.expression.mvel;

import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.everit.expression.ExpressionCompiler;
import org.everit.expression.ParserConfiguration;
import org.everit.expression.mvel.internal.MixedMVELClassLoader;
import org.everit.expression.mvel.internal.MvelCompiledExpression;
import org.mvel2.CompileException;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

/**
 * The MVEL {@link ExpressionCompiler}.
 */
public class MvelExpressionCompiler implements ExpressionCompiler {

  /**
   * A {@link PrivilegedAction} that creates a {@link MixedMVELClassLoader}.
   */
  private static final class MixedMVELClassLoaderCreator implements PrivilegedAction<ClassLoader> {

    private final ParserConfiguration parserConfiguration;

    private MixedMVELClassLoaderCreator(final ParserConfiguration parserConfiguration) {
      this.parserConfiguration = parserConfiguration;
    }

    @Override
    public ClassLoader run() {
      return new MixedMVELClassLoader(parserConfiguration.getClassLoader());
    }

  }

  @Override
  public CompiledExpression compile(final char[] document, final int expressionStart,
      final int expressionLength,
      final ParserConfiguration parserConfiguration) {
    return compile(String.valueOf(document, expressionStart, expressionLength),
        parserConfiguration);
  }

  @Override
  public CompiledExpression compile(final String expression,
      final ParserConfiguration parserConfiguration) {

    if (parserConfiguration == null) {
      throw new IllegalArgumentException("Parser configuration must be defined");
    }

    org.mvel2.ParserConfiguration mvelConfiguration = new org.mvel2.ParserConfiguration();

    ClassLoader classLoader =
        AccessController.doPrivileged(new MixedMVELClassLoaderCreator(parserConfiguration));
    mvelConfiguration.setClassLoader(classLoader);

    ParserContext mvelContext = new ParserContext(mvelConfiguration);
    @SuppressWarnings("rawtypes")
    Map nonGenericVariableTypes = parserConfiguration.getVariableTypes();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    Map<String, Class> halfGenericVariableTypes = nonGenericVariableTypes;
    mvelContext.setInputs(halfGenericVariableTypes);

    try {

      Serializable compiledExpression = MVEL.compileExpression(expression, mvelContext);
      return new MvelCompiledExpression(expression, compiledExpression,
          parserConfiguration.getStartRow(),
          parserConfiguration.getStartColumn());

    } catch (CompileException e) {
      e.getMessage();
      if (e.getLineNumber() == 1) {
        e.setColumn((e.getColumn() + parserConfiguration.getStartColumn()) - 1);
      }
      e.setLineNumber((e.getLineNumber() + parserConfiguration.getStartRow()) - 1);

      throw e;
    }
  }
}
