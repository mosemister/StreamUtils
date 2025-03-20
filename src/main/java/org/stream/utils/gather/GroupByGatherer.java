package org.stream.utils.gather;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

class GroupByGatherer<Value, By> implements Gatherer<Value, Map<By, Stream<Value>>, Map.Entry<By, Stream<Value>>> {

    private final Function<Value, By> by;
    private final BiPredicate<By, By> compare;

    GroupByGatherer(Function<Value, By> by, BiPredicate<By, By> compare) {
        this.by = Objects.requireNonNull(by);
        this.compare = Objects.requireNonNull(compare);
    }

    @Override
    public Supplier<Map<By, Stream<Value>>> initializer() {
        return ConcurrentHashMap::new;
    }

    @Override
    public Integrator<Map<By, Stream<Value>>, Value, Map.Entry<By, Stream<Value>>> integrator() {
        return Integrator.of((current, element, downstream) -> {
            var elementBy = by.apply(element);
            var opKeyObj = current.keySet().parallelStream().filter(key -> compare.test(key, elementBy)).findAny();
            if (opKeyObj.isPresent()) {
                var stream = current.get(opKeyObj.get());
                var newStream = Stream.concat(stream, Stream.of(element));
                current.replace(opKeyObj.get(), newStream);
                return true;
            }
            var newStream = Stream.of(element);
            current.put(elementBy, newStream);
            return true;
        });
    }

    @Override
    public BinaryOperator<Map<By, Stream<Value>>> combiner() {
        return (originalMap, newMap) -> {
            var returningMap = new ConcurrentHashMap<>(originalMap);
            newMap.forEach((elementBy, addingStream) -> {
                var opKeyObj = returningMap.keySet().parallelStream().filter(key -> compare.test(key, elementBy)).findAny();
                if (opKeyObj.isPresent()) {
                    var stream = returningMap.get(opKeyObj.get());
                    var newStream = Stream.concat(stream, addingStream);
                    returningMap.replace(opKeyObj.get(), newStream);
                    return;
                }
                returningMap.put(elementBy, addingStream);
            });
            return returningMap;
        };
    }

    @Override
    public BiConsumer<Map<By, Stream<Value>>, Downstream<? super Map.Entry<By, Stream<Value>>>> finisher() {
        return (map, downstream) -> map.entrySet().forEach(downstream::push);
    }
}
