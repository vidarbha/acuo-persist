package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
@EqualsAndHashCode(exclude = "id")
public class Foo implements Entity {

  @Id
  @GeneratedValue
  private Long id;

  @Id
  private int fooId;

  private String name;

  public Foo() {
  }

  public Foo(int fooId, String name) {
    this.fooId = fooId;
    this.name = name;
  }
}