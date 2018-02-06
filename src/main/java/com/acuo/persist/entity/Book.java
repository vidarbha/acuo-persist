package com.acuo.persist.entity;

import com.acuo.common.ids.BookId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import static com.acuo.persist.neo4j.converters.TypedStringConverter.BookIdConverter;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class Book extends Entity<Book> {

    @Property(name = "id")
    @Id
    @Convert(BookIdConverter.class)
    private BookId bookId;

    @Relationship(type = "PART_OF")
    private Portfolio portfolio;

}
