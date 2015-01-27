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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.mvel2.compiler.Accessor;

/**
 * A {@link ClassLoader} implementation that first looks for resources and classes in the provided {@link ClassLoader}
 * and if they not found, it searches in the {@link ClassLoader} of MVEL.
 *
 */
public class MixedMVELClassLoader extends ClassLoader {

    private final ClassLoader mvelClassLoader;

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
