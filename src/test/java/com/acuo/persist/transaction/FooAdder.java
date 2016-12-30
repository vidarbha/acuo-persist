package com.acuo.persist.transaction;

import com.acuo.persist.entity.Foo;

public interface FooAdder<X extends Exception> {
  void addFoo(Foo foo) throws X;
}