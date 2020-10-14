package io.dsub.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * An immutable transaction model
 */
public class Transaction implements Serializable {

    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////
    private final int amount;
    private final int vendorId;
    private final LocalDateTime time;
    private final UUID uuid;
    private final boolean isCancelled;
    private static final long serialVersionUID = 1L;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////
    public Transaction(int amount, int vendorId) {
        this(amount, vendorId, LocalDateTime.now());
    }

    public Transaction(int amount, int vendorId, LocalDateTime time) {
        this(amount, vendorId, time, UUID.randomUUID(), false);
    }

    public Transaction(int amount, int vendorId, LocalDateTime time, UUID uuid, boolean isCancelled) {
        this.amount = amount;
        this.vendorId = vendorId;
        this.time = time;
        this.uuid = uuid;
        this.isCancelled = isCancelled;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Withers
    ///////////////////////////////////////////////////////////////////////////
    public Transaction withAmount(int amount) {
        return new Transaction(amount, this.vendorId, this.time, this.uuid, this.isCancelled);
    }

    public Transaction withVendorId(int vendorId) {
        return new Transaction(this.amount, vendorId, this.time, this.uuid, this.isCancelled);
    }

    public Transaction withTime(LocalDateTime time) {
        return new Transaction(this.amount, this.vendorId, time, this.uuid, this.isCancelled);
    }

    public Transaction withCancelled(boolean isCancelled) {
        return new Transaction(this.amount, this.vendorId, this.time, this.uuid, isCancelled);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Getters
    ///////////////////////////////////////////////////////////////////////////
    public int getAmount() {
        return amount;
    }

    public int getVendorId() {
        return vendorId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isCancelled() {
        return isCancelled;
    }
}
