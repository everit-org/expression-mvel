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
package org.everit.expression.mvel.internal;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.mvel2.compiler.Accessor;

/**
 * A {@link ClassLoader} implementation that first looks for resources and classes in the provided
 * {@link ClassLoader} and if they not found, it searches in the {@link ClassLoader} of MVEL.
 *
 */
public class MixedMVELClassLoader extends ClassLoader {

  private final ClassLoader mvelClassLoader;

  /**
   * Constructor.
   */
  public MixedMVELClassLoader(final ClassLoader wrapped) {
    super(wrapped);
    ClassLoader tmpMvelClassLoader = Accessor.class.getClassLoader();
    if (tmpMvelClassLoader.equals(wrapped)) {
      mvelClassLoader = null;
    } else {
      mvelClassLoader = tmpMvelClassLoader;
    }
  }

  @Override
  protected Class<?> findClass(final String name) throws ClassNotFoundException {
    return mvelClassLoader.loadClass(name);
  }

  @Override
  protected URL findResource(final String name) {
    return mvelClassLoader.getResource(name);
  }

  @Override
  protected Enumeration<URL> findResources(final String name) throws IOException {
    return mvelClassLoader.getResources(name);
  }

}
