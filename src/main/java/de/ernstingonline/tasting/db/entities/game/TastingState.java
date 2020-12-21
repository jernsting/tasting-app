package de.ernstingonline.tasting.db.entities.game;

public enum TastingState {
    PLANNED, STARTED, CANCELLED, CLOSED;

    public boolean started() {
        return this == STARTED;
    }

    public boolean planned() {
        return this == PLANNED;
    }

    public boolean cancelled() {
        return this == CANCELLED;
    }

    public boolean closed() {
        return this == CLOSED;
    }
}
