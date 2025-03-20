package org.stream.utils.gather;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UseUntilTests {

    @Test
    public void getsAllUntilTheStringThirdIsHitIncludingThird() {
        //Arrange
        var array = List.of("first", "second", "third", "fourth", "fifth");
        var expected = List.of("first", "second", "third");

        //Act
        var result = array.stream().gather(StreamUtilsGatherers.takeUntil(t -> t.equals("third"), true));

        //Assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    public void getsAllUntilTheStringThirdIsHitExcludingThird() {
        //Arrange
        var array = List.of("first", "second", "third", "fourth", "fifth");
        var expected = List.of("first", "second");

        //Act
        var result = array.stream().gather(StreamUtilsGatherers.takeUntil(t -> t.equals("third"), false));

        //Assert
        Assertions.assertEquals(result, expected);
    }

    @Test
    public void getsAllUntilTheStringThirdIsHitExcludingThirdUsingDefault() {
        //Arrange
        var array = List.of("first", "second", "third", "fourth", "fifth");
        var expected = List.of("first", "second");

        //Act
        var result = array.stream().gather(StreamUtilsGatherers.takeUntil(t -> t.equals("third")));

        //Assert
        Assertions.assertEquals(result, expected);
    }
}
