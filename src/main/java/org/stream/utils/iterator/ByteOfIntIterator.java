package org.stream.utils.iterator;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.IntConsumer;

public class ByteOfIntIterator implements Spliterator.OfInt {

    private final byte[] array;
    private int index;
    private int endIndex;

    public ByteOfIntIterator(byte... bytes) {
        this(0, bytes);
    }

    public ByteOfIntIterator(int startIndex, byte... bytes) {
        this(startIndex, null, bytes);
    }

    public ByteOfIntIterator(int startIndex, Integer endIndex, byte... bytes) {
        this.array = bytes;
        this.index = startIndex;
        this.endIndex = Objects.requireNonNullElseGet(endIndex, () -> bytes.length);
    }

    @Override
    public Spliterator.OfInt trySplit() {
        var difference = endIndex - index;
        if (difference <= 2) {
            return null;
        }
        var newStart = index + (difference / 2);
        var originalEnd = endIndex;
        endIndex = newStart;
        return new ByteOfIntIterator(newStart, originalEnd, this.array);
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
