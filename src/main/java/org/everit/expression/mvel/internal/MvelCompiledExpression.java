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
