package org.stream.utils.gather;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Gatherer;

class TakeUntilGatherer<Value> implements Gatherer<Value, AtomicBoolean, Value> {

    private final Predicate<Value> until;
    private final boolean includeStopper;

    public TakeUntilGatherer(Predicate<Value> until, boolean includeStopper) {
        this.includeStopper = includeStopper;
        this.until = Objects.requireNonNull(until);
    }

    @Override
    public Integrator<AtomicBoolean, Value, Value> integrator() {
        return Integrator.of((state, element, downstream) -> {
            if (state.get()) {
                return false;
            }
            if (until.test(element)) {
                state.set(true);
                if (includeStopper) {
                    downstream.push(element);
                    return true;
                }
                return false;
            }
            downstream.push(element);
            return true;
        });
    }
}
