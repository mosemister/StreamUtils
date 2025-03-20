package org.stream.utils.collector;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

class SingleCollector<Value> implements Collector<Value, AtomicReference<SingleCollector.Entry<Value>>, Optional<Value>> {

    public record Entry<Value>(Boolean alreadySet, Value key) {

    }

    @Override
    public Supplier<AtomicReference<SingleCollector.Entry<Value>>> supplier() {
        return () -> new AtomicReference<>(new Entry<>(false, null));
    }

    @Override
    public BiConsumer<AtomicReference<Entry<Value>>, Value> accumulator() {
        return (original, newValue) -> {
            if (newValue == null) {
                return;
            }
            if (original.get().alreadySet()) {
                original.set(new Entry<>(true, null));
            }
            original.set(new Entry<>(true, newValue));
        };
    }

    @Override
    public BinaryOperator<AtomicReference<Entry<Value>>> combiner() {
        return (original, newValue) -> {
            if (!original.get().alreadySet()) {
                return newValue;
            }
            if (!newValue.get().alreadySet()) {
                return original;
            }
            return new AtomicReference<>(new Entry<>(true, null));
        };
    }

    @Override
    public Function<AtomicReference<Entry<Value>>, Optional<Value>> finisher() {
        return (result) -> result.get().alreadySet() ? Optional.ofNullable(result.get().key()) : Optional.empty();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.CONCURRENT, Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH);
    }
}
