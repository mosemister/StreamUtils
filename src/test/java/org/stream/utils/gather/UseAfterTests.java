package org.stream.utils.gather;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UseAfterTests {

    @Test
    public void getsAllAfterTheStringThirdIsHitIncludingThird() {
        //Arrange
        var array = List.of("first", "second", "third", "fourth", "fifth");
        var expected = List.of("third", "fourth", "fifth");

        //Act
        var result = array.stream().gather(StreamUtilsGatherers.skipUntil(t -> t.equals("third"), true));

        //Assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    public void getsAllAfterTheStringThirdIsHitExcludingThird() {
        //Arrange
        var array = List.of("first", "second", "third", "fourth", "fifth");
        var expected = List.of("fourth", "fifth");

        //Act
        var result = array.stream().gather(StreamUtilsGatherers.skipUntil(t -> t.equals("third"), false));

        //Assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    public void getsAllAfterTheStringThirdIsHitExcludingThirdUsingDefault() {
        //Arrange
        var array = List.of("first", "second", "third", "fourth", "fifth");
        var expected = List.of("fourth", "fifth");

        //Act
        var result = array.stream().gather(StreamUtilsGatherers.skipUntil(t -> t.equals("third")));

        //Assert
        Assertions.assertEquals(result, expected);
    }
}
