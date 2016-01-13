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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

import org.mvel2.CompileException;
import org.mvel2.ErrorDetail;

/**
 * Extended {@link CompileException} that contains the template name.
 */
public class NamedCompileException extends CompileException {

  private static final long serialVersionUID = 5078914871309510172L;

  private String name;

  private CompileException wrappedCompileException;

  /**
   * Constructor.
   *
   * @param name
   *          the name of template.
   * @param compileException
   *          the {@link CompileException}.
   * @param columnNumber
   *          the column where the expression starts.
   * @param lineNumber
   *          the line where the expression starts.
   */
  public NamedCompileException(final String name, final CompileException compileException,
      final int columnNumber, final int lineNumber) {
    super("", compileException.getExpr(), compileException.getCursor());
    compileException.setColumn(columnNumber);
    compileException.setLineNumber(lineNumber);
    compileException.setErrors(compileException.getErrors());
    // setEvaluationContext();
    compileException.setLastLineStart(compileException.getLastLineStart());
    compileException.setStackTrace(compileException.getStackTrace());
    wrappedCompileException = compileException;
    this.name = name;
  }

  @Override
  public boolean equals(final Object obj) {
    return wrappedCompileException.equals(obj);
  }

  @Override
  public synchronized Throwable getCause() {
    return wrappedCompileException.getCause();
  }

  @Override
  public CharSequence getCodeNearError() {
    return wrappedCompileException.getCodeNearError();
  }

  @Override
  public int getColumn() {
    return wrappedCompileException.getColumn();
  }

  @Override
  public int getCursor() {
    return wrappedCompileException.getCursor();
  }

  @Override
  public int getCursorOffet() {
    return wrappedCompileException.getCursorOffet();
  }

  @Override
  public List<ErrorDetail> getErrors() {
    return wrappedCompileException.getErrors();
  }

  @Override
  public char[] getExpr() {
    return wrappedCompileException.getExpr();
  }

  @Override
  public int getLastLineStart() {
    return wrappedCompileException.getLastLineStart();
  }

  @Override
  public int getLineNumber() {
    return wrappedCompileException.getLineNumber();
  }

  @Override
  public String getLocalizedMessage() {
    return wrappedCompileException.getLocalizedMessage();
  }

  @Override
  public String getMessage() {
    return "Error in " + name + ": " + wrappedCompileException.getMessage();
  }

  @Override
  public StackTraceElement[] getStackTrace() {
    return wrappedCompileException.getStackTrace();
  }

  @Override
  public int hashCode() {
    return wrappedCompileException.hashCode();
  }

  @Override
  public void printStackTrace() {
    printStackTrace(System.err);
  }

  @Override
  public void printStackTrace(final PrintStream s) {
    wrappedCompileException.printStackTrace(s);
  }

  @Override
  public void printStackTrace(final PrintWriter s) {
    wrappedCompileException.printStackTrace(s);
  }

  @Override
  public void setColumn(final int column) {
    wrappedCompileException.setColumn(column);
  }

  @Override
  public void setCursor(final int cursor) {
    wrappedCompileException.setCursor(cursor);
  }

  @Override
  public void setErrors(final List<ErrorDetail> errors) {
    wrappedCompileException.setErrors(errors);
  }

  @Override
  public void setEvaluationContext(final Object evaluationContext) {
    wrappedCompileException.setEvaluationContext(evaluationContext);
  }

  @Override
  public void setExpr(final char[] expr) {
    wrappedCompileException.setExpr(expr);
  }

  @Override
  public void setLastLineStart(final int lastLineStart) {
    wrappedCompileException.setLastLineStart(lastLineStart);
  }

  @Override
  public void setLineNumber(final int lineNumber) {
    wrappedCompileException.setLineNumber(lineNumber);
  }

  @Override
  public void setStackTrace(final StackTraceElement[] stackTrace) {
    wrappedCompileException.setStackTrace(stackTrace);
  }

  @Override
  public String toString() {
    return wrappedCompileException.toString();
  }

}
