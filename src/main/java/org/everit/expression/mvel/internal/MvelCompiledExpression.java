package org.everit.expression.mvel.internal;

import java.io.Serializable;
import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.mvel2.CompileException;
import org.mvel2.MVEL;

public class MvelCompiledExpression implements CompiledExpression {

    private final int exprStartInContext;

    private final char[] exprWithContext;

    private final int lineCount;

    private final int lineOffset;

    private final Serializable mvelExpression;

    public MvelCompiledExpression(final Serializable mvelExpression, final char[] exprWithContext,
            final int expressionStartInContext, final int lineCount, final int lineOffset) {
        this.mvelExpression = mvelExpression;
        this.lineCount = lineCount;
        this.lineOffset = lineOffset;
        this.exprStartInContext = expressionStartInContext;
        this.exprWithContext = exprWithContext;
    }

    // private int countColumnBeforeCursor(final int cursor) {
    // int position = cursor + exprStartInContext;
    // for (; position > exprStartInContext
    // && exprWithContext[position] != '\n'; position--) {
    // ;
    // }
    // return cursor + exprStartInContext - position;
    // }
    //
    // private int countLineEndsInExpressionTillCursor(final int cursor) {
    // int lineEnds = 0;
    // for (int i = exprStartInContext; i < cursor; i++) {
    // if (exprWithContext[i] == '\n') {
    // lineEnds++;
    // }
    // }
    // return lineEnds;
    // }

    @Override
    public Object eval(final Map<String, Object> vars) {
        try {
            return MVEL.executeExpression(mvelExpression, vars);
        } catch (CompileException e) {
            // int eLineNumber = e.getLineNumber() + countLineEndsInExpressionTillCursor(e.getCursor());
            int eLineNumber = e.getLineNumber();
            e.setLineNumber(lineCount + eLineNumber - 1);
            int ecolumn = e.getColumn();
            e.setColumn(lineOffset + ecolumn - 1);
            e.setExpr(exprWithContext);
            e.setCursor(e.getCursor() + exprStartInContext);

            // if (e.getColumn() == 0) {
            // ecolumn = countColumnBeforeCursor(e.getCursor());
            // e.setColumn(ecolumn);
            // }
            // if (eLineNumber == 1) {
            // e.setColumn(lineOffset + ecolumn);
            // }
            // putCurrentLine(e);

            throw e;
        }
    }
    // private void putCurrentLine(final CompileException e) {
    // // Please note that this has to be done as CompileException is buggy if there are new lines in the expr
    // int cursor = exprStartInContext + e.getCursor();
    //
    // if (cursor == 0 && exprWithContext[cursor] <= ' ') {
    // int additionalLineNum = 0;
    // int column = e.getColumn();
    // for (cursor = 0; cursor < exprWithContext.length && exprWithContext[cursor] <= ' '; cursor++) {
    // if (exprWithContext[cursor] == '\n') {
    // additionalLineNum++;
    // column = 1;
    // } else {
    // column++;
    // }
    // }
    // e.setColumn(column);
    // e.setLineNumber(e.getLineNumber() + additionalLineNum);
    // }
    //
    // int positionOfNewLineBefore = cursor;
    //
    // int lowestPosition = cursor - 20;
    // if (lowestPosition < 0) {
    // lowestPosition = -1;
    // }
    // while (positionOfNewLineBefore > lowestPosition && exprWithContext[positionOfNewLineBefore] >= ' ') {
    // positionOfNewLineBefore--;
    // }
    // positionOfNewLineBefore++;
    //
    // while (positionOfNewLineBefore < cursor && exprWithContext[positionOfNewLineBefore] == ' ') {
    // positionOfNewLineBefore++;
    // }
    //
    // int positionOfNewLineAfter = cursor;
    // int highestPosition = cursor + 30;
    // if (highestPosition >= exprWithContext.length) {
    // highestPosition = exprWithContext.length - 1;
    // }
    // while (positionOfNewLineAfter <= highestPosition && exprWithContext[positionOfNewLineAfter] >= ' ') {
    // positionOfNewLineAfter++;
    // }
    // positionOfNewLineAfter--;
    //
    // while (positionOfNewLineAfter > cursor && exprWithContext[positionOfNewLineAfter] == ' ') {
    // positionOfNewLineAfter--;
    // }
    //
    // int length = positionOfNewLineAfter - positionOfNewLineBefore + 1;
    //
    // char[] reducedExpr = new char[length];
    //
    // System.arraycopy(exprWithContext, positionOfNewLineBefore, reducedExpr, 0, length);
    // e.setExpr(reducedExpr);
    // e.setCursor(cursor - positionOfNewLineBefore);
    // }

}
