package com.acuo.persist;

public interface FooAdder<X extends Exception> {
  void addFoo(Foo foo) throws X;
}