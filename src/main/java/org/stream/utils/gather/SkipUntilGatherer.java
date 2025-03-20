package org.stream.utils.gather;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Gatherer;

class SkipUntilGatherer<Value> implements Gatherer<Value, AtomicBoolean, Value> {

    private final Predicate<Value> until;
    private final boolean includeStarter;

    public SkipUntilGatherer(Predicate<Value> until, boolean includeStarter) {
        this.includeStarter = includeStarter;
        this.until = Objects.requireNonNull(until);
    }

    @Override
    public Integrator<AtomicBoolean, Value, Value> integrator() {
        return Integrator.of((state, element, downstream) -> {
            if (state.get()) {
                downstream.push(element);
                return true;
            }
            if (until.test(element)) {
                state.set(true);
                if (includeStarter) {
                    downstream.push(element);
                    return true;
                }
            }
            return false;
        });
    }
}
