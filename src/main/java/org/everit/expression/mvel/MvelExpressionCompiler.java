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
package org.everit.expression.mvel;

import java.io.Serializable;
import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.everit.expression.ExpressionCompiler;
import org.everit.expression.ParserConfiguration;
import org.everit.expression.mvel.internal.MixedMVELClassLoader;
import org.everit.expression.mvel.internal.MvelCompiledExpression;
import org.mvel2.CompileException;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

public class MvelExpressionCompiler implements ExpressionCompiler {

    @Override
    public CompiledExpression compile(final char[] document, final int expressionStart, final int expressionLength,
            final ParserConfiguration parserConfiguration) {
        return compile(String.valueOf(document, expressionStart, expressionLength), parserConfiguration);
    }

    @Override
    public CompiledExpression compile(final String expression, final ParserConfiguration parserConfiguration) {

        if (parserConfiguration == null) {
            throw new IllegalArgumentException("Parser configuration must be defined");
        }

        org.mvel2.ParserConfiguration mvelConfiguration = new org.mvel2.ParserConfiguration();
        mvelConfiguration.setClassLoader(new MixedMVELClassLoader(parserConfiguration.getClassLoader()));

        org.mvel2.ParserContext mvelContext = new ParserContext(mvelConfiguration);
        @SuppressWarnings("rawtypes")
        Map nonGenericVariableTypes = parserConfiguration.getVariableTypes();

        @SuppressWarnings({ "rawtypes", "unchecked" })
        Map<String, Class> halfGenericVariableTypes = nonGenericVariableTypes;
        mvelContext.setInputs(halfGenericVariableTypes);

        try {

            Serializable compiledExpression = MVEL.compileExpression(expression, mvelContext);
            return new MvelCompiledExpression(compiledExpression, parserConfiguration.getStartRow(),
                    parserConfiguration.getStartColumn());

        } catch (CompileException e) {
            e.getMessage();
            if (e.getLineNumber() == 1) {
                e.setColumn(e.getColumn() + parserConfiguration.getStartColumn() - 1);
            }
            e.setLineNumber(e.getLineNumber() + parserConfiguration.getStartRow() - 1);

            throw e;
        }

    }
}
