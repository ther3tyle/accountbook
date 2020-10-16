package io.dsub.model;

import io.dsub.util.DataType;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Logger;


/**
 * An immutable transaction model
 */
public class Transaction extends Model {

    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////
    private final int amount;
    private final int vendorId;
    private final LocalDateTime time;
    private final UUID id;
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

    public Transaction(int amount, int vendorId, LocalDateTime time, UUID id, boolean isCancelled) {
        this.amount = amount;
        this.vendorId = vendorId;
        this.time = time;
        this.id = id;
        this.isCancelled = isCancelled;
    }

    public static Function<String, Model> getParser() {
        return (String s) -> {
            String[] data = s.split(",");
            if (data.length != 4) throw new InvalidParameterException("input size must be 4");

            int amount;
            int vendorId;
            try {
                amount = Integer.parseInt(data[0]);
                vendorId = Integer.parseInt(data[1]);
            } catch (NumberFormatException e) {
                Logger.getLogger(Transaction.class.getName()).severe(e.getMessage());
                return null;
            }

            LocalDateTime time;

            try {
                time = LocalDateTime.parse(data[2]);
            } catch (DateTimeParseException e) {
                Logger.getLogger(Transaction.class.getName()).severe(e.getMessage());
                return null;
            }

            UUID id;
            try {
                id = UUID.fromString(data[3]);
            } catch (IllegalArgumentException e) {
                Logger.getLogger(Transaction.class.getName()).severe(e.getMessage());
                return null;
            }

            return new Transaction(amount, vendorId, time, id, false);
        };
    }

    ///////////////////////////////////////////////////////////////////////////
    // Withers
    ///////////////////////////////////////////////////////////////////////////
    public Transaction withAmount(int amount) {
        return new Transaction(amount, this.vendorId, LocalDateTime.parse(this.time.toString()), UUID.fromString(this.id.toString()), this.isCancelled);
    }

    public Transaction withVendorId(int vendorId) {
        return new Transaction(this.amount, vendorId, LocalDateTime.parse(this.time.toString()), UUID.fromString(this.id.toString()), this.isCancelled);
    }

    public Transaction withTime(LocalDateTime time) {
        return new Transaction(this.amount, this.vendorId, time, UUID.fromString(this.id.toString()), this.isCancelled);
    }

    public Transaction withCancelled(boolean isCancelled) {
        return new Transaction(this.amount, this.vendorId, LocalDateTime.parse(this.time.toString()), UUID.fromString(this.id.toString()), isCancelled);
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

    @Override
    public String getId() {
        return id.toString();
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s\n",
                this.getAmount(),
                this.getVendorId(),
                this.getTime(),
                this.getId()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (this.amount != that.amount) return false;
        if (this.vendorId != that.vendorId) return false;
        if (this.isCancelled != that.isCancelled) return false;
        if (!this.time.equals(that.time)) return false;
        return this.id.equals(that.id);
    }
}
