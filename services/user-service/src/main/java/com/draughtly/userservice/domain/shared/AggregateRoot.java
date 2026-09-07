package com.draughtly.userservice.domain.shared;

import java.util.ArrayList;
import java.util.List;

/**
 * Supertype for aggregate roots. Its only job is to buffer the domain events
 * raised while a behaviour runs, so the application layer can drain and publish
 * them after the aggregate has been persisted.
 *
 * <p>Identity and equality are left to each concrete aggregate.
 */
public abstract class AggregateRoot {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /** Buffers an event raised by a behaviour of this aggregate. */
    protected void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    /** Returns the buffered events as an unmodifiable list and clears the buffer. */
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> pending = List.copyOf(domainEvents);
        domainEvents.clear();
        return pending;
    }
}
