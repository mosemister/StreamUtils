package org.stream.utils.gather;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupByTests {

    @Test
    public void canGroupBySimple() {
        //Arrange
        var array = new String[]{"one", "two", "three", "four"};

        //Act
        Map<Character, List<String>> result = Stream
                .of(array)
                .gather(StreamUtilsGatherers.groupBy(value -> value.charAt(0)))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toList()));

        //Arrange
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.containsKey('o'), "group by one failed");
        Assertions.assertTrue(result.containsKey('t'), "group by two/three failed");
        Assertions.assertTrue(result.containsKey('f'), "group by four failed");
    }

    @Test
    public void canGroupByCompare() {
        //Arrange
        var array = new String[]{"one", "two", "three", "four"};

        //Act
        Map<Character, List<String>> result = Stream
                .of(array)
                .gather(StreamUtilsGatherers.groupByCompare(value -> value.charAt(0)))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toList()));

        //Arrange
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.containsKey('o'), "group by one failed");
        Assertions.assertTrue(result.containsKey('t'), "group by two/three failed");
        Assertions.assertTrue(result.containsKey('f'), "group by four failed");
    }
}
