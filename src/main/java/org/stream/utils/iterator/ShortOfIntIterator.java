package org.stream.utils.iterator;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.IntConsumer;

public class ShortOfIntIterator implements Spliterator.OfInt {

    private final short[] array;
    private int index;
    private int endIndex;

    public ShortOfIntIterator(short... bytes) {
        this(0, bytes);
    }

    public ShortOfIntIterator(int startIndex, short... bytes) {
        this(startIndex, null, bytes);
    }

    public ShortOfIntIterator(int startIndex, Integer endIndex, short... bytes) {
        this.array = bytes;
        this.index = startIndex;
        this.endIndex = Objects.requireNonNullElseGet(endIndex, () -> bytes.length);
    }

    @Override
    public OfInt trySplit() {
        var difference = endIndex - index;
        if (difference <= 2) {
            return null;
        }
        var newStart = index + (difference / 2);
        var originalEnd = endIndex;
        endIndex = newStart;
        return new ShortOfIntIterator(newStart, originalEnd, this.array);
    }

    @Override
    public long estimateSize() {
        return endIndex - index;
    }

    @Override
    public int characteristics() {
        return IMMUTABLE | CONCURRENT | SIZED | SUBSIZED;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (index >= endIndex) {
            return false;
        }
        action.accept(array[index]);
        index++;
        return true;
    }
}
