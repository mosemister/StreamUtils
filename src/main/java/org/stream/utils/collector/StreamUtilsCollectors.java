package org.stream.utils.collector;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collector;

public class StreamUtilsCollectors {

    public static <Value, By extends Comparable<By>> Collector<Value, ?, Optional<Value>> minBy(Function<Value, By> toBy) {
        return minBy(toBy, Comparator.naturalOrder());
    }

    public static <Value, By> Collector<Value, ?, Optional<Value>> minBy(Function<Value, By> toBy, Comparator<By> compare) {
        return new CompareByCollector<>(toBy, compare, compared -> compared < 0);
    }

    public static <Value, By extends Comparable<By>> Collector<Value, ?, Optional<Value>> maxBy(Function<Value, By> toBy) {
        return maxBy(toBy, Comparator.naturalOrder());
    }

    public static <Value, By> Collector<Value, ?, Optional<Value>> maxBy(Function<Value, By> toBy, Comparator<By> compare) {
        return new CompareByCollector<>(toBy, compare, compared -> compared > 0);
    }

    public static <Value> Collector<Value, AtomicReference<SingleCollector.Entry<Value>>, Optional<Value>> single() {
        return new SingleCollector<>();
    }
}
