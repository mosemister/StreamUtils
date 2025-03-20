package org.stream.utils.gather;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class StreamUtilsGatherers {

    public static <Value> Gatherer<Value, ?, Value> skipUntil(Predicate<Value> test) {
        return skipUntil(test, false);
    }

    public static <Value> Gatherer<Value, ?, Value> skipUntil(Predicate<Value> test, boolean includeStarter) {
        return new SkipUntilGatherer<>(test, includeStarter);
    }

    public static <Value> Gatherer<Value, ?, Value> takeUntil(Predicate<Value> test) {
        return takeUntil(test, false);
    }

    public static <Value> Gatherer<Value, ?, Value> takeUntil(Predicate<Value> test, boolean includeStopper) {
        return new TakeUntilGatherer<>(test, includeStopper);
    }

    public static <Value> Gatherer<Value, Map<Value, Stream<Value>>, Map.Entry<Value, Stream<Value>>> group() {
        return groupBy(t -> t, Object::equals);
    }

    public static <Value, By> Gatherer<Value, Map<By, Stream<Value>>, Map.Entry<By, Stream<Value>>> groupBy(Function<Value, By> by) {
        return groupBy(by, Object::equals);
    }

    public static <Value, By> Gatherer<Value, Map<By, Stream<Value>>, Map.Entry<By, Stream<Value>>> groupBy(Function<Value, By> by, BiPredicate<By, By> compare) {
        return new GroupByGatherer<>(by, compare);
    }

    public static <Value, By> Gatherer<Value, Map<By, Stream<Value>>, Map.Entry<By, Stream<Value>>> groupByCompare(Function<Value, By> by, Comparator<By> compare) {
        return groupBy(by, (original, comparing) -> compare.compare(original, comparing) == 0);
    }

    public static <Value, By extends Comparable<By>> Gatherer<Value, Map<By, Stream<Value>>, Map.Entry<By, Stream<Value>>> groupByCompare(Function<Value, By> by) {
        return groupBy(by, (original, comparing) -> original.compareTo(comparing) == 0);
    }
}
