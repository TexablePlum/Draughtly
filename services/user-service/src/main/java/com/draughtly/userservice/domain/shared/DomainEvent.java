package com.draughtly.userservice.domain.shared;

import java.time.Instant;

/**
 * A fact that has already happened inside the domain. Implementations are
 * immutable {@code record}s that carry {@link #occurredAt()} plus whatever
 * payload the fact needs (at least the id of the aggregate it concerns).
 */
public interface DomainEvent {

    Instant occurredAt();
}
