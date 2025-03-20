package org.stream.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StreamUtilsTests {

    @Test
    public void canBuildByteArray() {
        //Arrange
        var array = new byte[]{0, 2, 1};

        //Act
        var result = StreamUtils.fromByteArray(array).count();

        //Assert
        Assertions.assertEquals(3, result);
    }

    @Test
    public void canBuildShortArray() {
        //Arrange
        var array = new short[]{0, 2, 1};

        //Act
        var result = StreamUtils.fromShortArray(array).count();

        //Assert
        Assertions.assertEquals(3, result);
    }
}
