package org.everit.expression.mvel.internal;

import java.io.Serializable;
import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.mvel2.CompileException;
import org.mvel2.MVEL;

public class MvelCompiledExpression implements CompiledExpression {

    private final int lineCount;

    private final int lineOffset;

    private final Serializable mvelExpression;

    public MvelCompiledExpression(final Serializable mvelExpression, final int lineCount, final int lineOffset) {
        this.mvelExpression = mvelExpression;
        this.lineCount = lineCount;
        this.lineOffset = lineOffset;
    }

    @Override
    public Object eval(final Map<String, Object> vars) {
        try {
            return MVEL.executeExpression(mvelExpression, vars);
        } catch (CompileException e) {
            e.setLineNumber(lineCount + e.getLineNumber() - 1);
            e.setColumn(lineOffset + e.getColumn() - 1);
            throw e;
        }
    }

}
