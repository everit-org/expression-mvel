package org.everit.expression.mvel;

import java.io.Serializable;

import org.everit.expression.CompiledExpression;
import org.everit.expression.ExpressionCompiler;
import org.everit.expression.ParserConfiguration;
import org.everit.expression.mvel.internal.MvelCompiledExpression;
import org.mvel2.CompileException;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

public class MvelExpressionCompiler implements ExpressionCompiler {

    @Override
    public CompiledExpression compile(final String expression, final ParserConfiguration parserConfiguration) {

        if (parserConfiguration == null) {
            throw new IllegalArgumentException("Parser configuration must be defined");
        }

        org.mvel2.ParserConfiguration mvelConfiguration = new org.mvel2.ParserConfiguration();
        mvelConfiguration.setClassLoader(parserConfiguration.getClassLoader());

        org.mvel2.ParserContext mvelContext = new ParserContext(mvelConfiguration);

        try {

            Serializable compiledExpression = MVEL.compileExpression(expression, mvelContext);
            return new MvelCompiledExpression(compiledExpression, parserConfiguration.getLineNumber(),
                    parserConfiguration.getColumn());

        } catch (CompileException e) {
            e.getMessage();
            if (e.getLineNumber() == 1) {
                e.setColumn(e.getColumn() + parserConfiguration.getColumn() - 1);
            }
            e.setLineNumber(e.getLineNumber() + parserConfiguration.getLineNumber() - 1);

            throw e;
        }

    }
}
