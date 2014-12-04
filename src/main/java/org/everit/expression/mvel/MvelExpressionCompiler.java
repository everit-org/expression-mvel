package org.everit.expression.mvel;

import java.io.Serializable;

import org.everit.expression.CompiledExpression;
import org.everit.expression.ExpressionCompiler;
import org.everit.expression.ParserConfiguration;
import org.everit.expression.mvel.internal.MvelCompiledExpression;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

public class MvelExpressionCompiler implements ExpressionCompiler {

    @Override
    public CompiledExpression compile(final String expression) {
        return compile(expression, null);
    }

    @Override
    public CompiledExpression compile(final String expression, final ParserConfiguration parserContext) {

        Serializable compiledExpression;
        int lineCount = 1;
        int lineOffset = 1;
        if (parserContext == null) {
            compiledExpression = MVEL.compileExpression(expression);
        } else {
            org.mvel2.ParserConfiguration mvelConfiguration = new org.mvel2.ParserConfiguration();
            mvelConfiguration.setClassLoader(parserContext.getClassLoader());
            org.mvel2.ParserContext mvelContext = new ParserContext(mvelConfiguration);
            lineCount = parserContext.getLineNumber();
            lineOffset = parserContext.getColumn();
            compiledExpression = MVEL.compileExpression(expression.toCharArray(), 0, expression.length(), mvelContext);
        }

        return new MvelCompiledExpression(compiledExpression, lineCount, lineOffset);
    }
}
