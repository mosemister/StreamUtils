package org.stream.utils;

import org.stream.utils.iterator.ByteOfIntIterator;
import org.stream.utils.iterator.IteratorToSplit;
import org.stream.utils.iterator.ShortOfIntIterator;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {

    public static <Value> Stream<Value> fromIterator(Iterator<Value> iterator) {
        var splitIterator = new IteratorToSplit<>(Objects.requireNonNull(iterator));
        return StreamSupport.stream(splitIterator, false);
    }

    public static IntStream fromByteArray(byte... array) {
        var ofInt = new ByteOfIntIterator(array);
        return StreamSupport.intStream(ofInt, false);
    }

    public static IntStream fromShortArray(short... array) {
        var ofInt = new ShortOfIntIterator(array);
        return StreamSupport.intStream(ofInt, false);
    }
}
