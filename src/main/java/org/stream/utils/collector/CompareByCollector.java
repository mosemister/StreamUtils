package org.stream.utils.collector;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;
import java.util.stream.Collector;

class CompareByCollector<Value, By> implements Collector<Value, AtomicReference<Map.Entry<Value, By>>, Optional<Value>> {

    private final Comparator<By> comparing;
    private final Function<Value, By> toBy;
    private final IntPredicate toOverride;

    public CompareByCollector(Function<Value, By> toBy, Comparator<By> comparing, IntPredicate toOverride) {
        this.comparing = Objects.requireNonNull(comparing);
        this.toBy = Objects.requireNonNull(toBy);
        this.toOverride = Objects.requireNonNull(toOverride);
    }

    @Override
    public Supplier<AtomicReference<Map.Entry<Value, By>>> supplier() {
        return AtomicReference::new;
    }

    @Override
    public BiConsumer<AtomicReference<Map.Entry<Value, By>>, Value> accumulator() {
        return (reference, value) -> {
            var valueBy = toBy.apply(value);
            if (reference.get() == null) {
                reference.set(Map.entry(value, valueBy));
                return;
            }
            var compared = this.comparing.compare(reference.get().getValue(), valueBy);
            if (toOverride.test(compared)) {
                reference.set(Map.entry(value, valueBy));
            }
        };
    }

    @Override
    public BinaryOperator<AtomicReference<Map.Entry<Value, By>>> combiner() {
        return (original, compare) -> {
            if (original.get() == null) {
                return compare;
            }
            if (compare.get() == null) {
                return original;
            }
            var valueBy = original.get().getValue();
            var compareBy = compare.get().getValue();
            var compared = this.comparing.compare(valueBy, compareBy);
            if (toOverride.test(compared)) {
                return compare;
            }
            return original;
        };
    }

    @Override
    public Function<AtomicReference<Map.Entry<Value, By>>, Optional<Value>> finisher() {
        return value -> Optional.ofNullable(value.get()).map(Map.Entry::getKey);
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.CONCURRENT, Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH);
    }
}
