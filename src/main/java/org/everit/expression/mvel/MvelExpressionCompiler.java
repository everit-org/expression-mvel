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
    public CompiledExpression compile(final char[] expression, final int start, final int offset,
            final ParserConfiguration parserConfiguration) {

        if (parserConfiguration == null) {
            throw new IllegalArgumentException("Parser configuration must be defined");
        }

        org.mvel2.ParserConfiguration mvelConfiguration = new org.mvel2.ParserConfiguration();
        mvelConfiguration.setClassLoader(parserConfiguration.getClassLoader());

        org.mvel2.ParserContext mvelContext = new ParserContext(mvelConfiguration);

        Serializable compiledExpression = MVEL
                .compileExpression(String.valueOf(expression, start, offset), mvelContext);

        int lineCount = parserConfiguration.getLineNumber();
        int lineOffset = parserConfiguration.getColumn();
        return new MvelCompiledExpression(compiledExpression, expression, start, lineCount, lineOffset);
    }
}
