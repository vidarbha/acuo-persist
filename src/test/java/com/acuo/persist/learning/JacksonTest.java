package com.acuo.persist.learning;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonTest {

    public static final class Point {

        @JsonProperty
        private final int x;
        @JsonProperty
        private final int y;

        @JsonCreator
        public Point(@JsonProperty("x") int x, @JsonProperty("y") int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            return y == point.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    @Test
    public void fromString() throws Exception {
        ObjectMapper om = new ObjectMapper();
        Point value = new Point(1, 2);
        String testAString = om.writeValueAsString(value); // error here!

        Point newTestA = om.readValue(testAString, Point.class);
        assertThat(newTestA).isEqualTo(value);
    }
}
