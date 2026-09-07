package com.draughtly.userservice.domain.shared;

/**
 * Base type for every business-rule violation raised by the domain. Unchecked,
 * and abstract because callers throw a concrete subclass that names the broken
 * rule; catching {@code DomainException} gives the web layer a single hook to
 * translate them into HTTP responses.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
