package org.stream.utils.iterator;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

public class IteratorToSplit<Value> implements Spliterator<Value> {

    private final Iterator<Value> iterator;

    public IteratorToSplit(Iterator<Value> iterator) {
        this.iterator = Objects.requireNonNull(iterator);
    }

    @Override
    public boolean tryAdvance(Consumer<? super Value> action) {
        if (this.iterator.hasNext()) {
            var next = this.iterator.next();
            action.accept(next);
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<Value> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        return IMMUTABLE | ORDERED;
    }
}
