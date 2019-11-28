/*
 * Copyright (C) 2000-2019 Heinz Max Kabutz
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Heinz Max Kabutz
 * licenses this file to you under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.  See the License for the specific
 * language governing permissions and limitations under the
 * License.
 */

package eu.javaspecialists.books.dynamicproxies.util.chain;

import eu.javaspecialists.books.dynamicproxies.util.*;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

// tag::listing[]
public class VTableDefaultMethodsHandler
    extends ChainedInvocationHandler {
  private final VTable vtable;
  public VTableDefaultMethodsHandler(
      VTable vtable, ChainedInvocationHandler next) {
    super(next);
    this.vtable = Objects.requireNonNull(vtable);
  }

  @Override
  public Object invoke(Object proxy, Method method,
                       Object[] args) throws Throwable {
    MethodHandle match = vtable.lookupDefaultMethod(method);
    if (match != null)
      return match.bindTo(proxy).invokeWithArguments(args);
    return super.invoke(proxy, method, args);
  }

  @Override
  protected Stream<Method> findUnhandledMethods(Class<?> target) {
    return
        super.findUnhandledMethods(target)
            .filter(method -> vtable.lookupDefaultMethod(method)
                                  == null);
  }
}
// end::listing[]