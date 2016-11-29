package com.acuo.persist;

import com.acuo.persist.entity.Entity;
import com.google.common.base.Objects;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Foo extends Entity {

  private int fooId;
  private String name;

  public Foo() {

  }

  public Foo(int fooId, String name) {
    this.fooId = fooId;
    this.name = name;
  }

  public int getFooId() {
    return fooId;
  }

  public String getName() {
    return name;
  }

  @Override public boolean equals(Object obj) {
    if (!(obj instanceof Foo)) return false;
    Foo other = (Foo) obj;
    return fooId == other.fooId && Objects.equal(name, other.name);
  }

  @Override public int hashCode() {
    return Objects.hashCode(fooId, name);
  }

  @Override public String toString() {
    return Objects.toStringHelper(this)
        .add("fooId", fooId)
        .add("name", name)
        .toString();
  }
}