package org.everit.expression.mvel.internal;

import java.io.Serializable;
import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.mvel2.CompileException;
import org.mvel2.MVEL;

public class MvelCompiledExpression implements CompiledExpression {

    private final int column;

    private final int lineNumber;

    private final Serializable mvelExpression;

    public MvelCompiledExpression(final Serializable mvelExpression, final int lineNumber, final int column) {
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
                e.setColumn(e.getColumn() + column - 1);
            }
            e.setLineNumber(e.getLineNumber() + lineNumber - 1);

            throw e;
        }
    }

}
