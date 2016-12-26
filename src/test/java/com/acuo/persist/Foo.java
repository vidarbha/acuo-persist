package com.acuo.persist;

import com.acuo.persist.entity.Entity;
import com.google.common.base.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Foo extends Entity {

  private int fooId;
  private String name;

  public Foo() {
  }

  public Foo(int fooId, String name) {
    this.fooId = fooId;
    this.name = name;
  }
}