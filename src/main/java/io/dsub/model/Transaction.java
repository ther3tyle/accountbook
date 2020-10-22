package io.dsub.model;

import io.dsub.constants.DataType;
import lombok.With;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * An immutable transaction model
 */

@With
public class Transaction extends Model implements Serializable {

    private final long amount;
    private final int vendorId;
    private final LocalDate date;
    private final UUID id;
    private static final long serialVersionUID = 1L;

    public Transaction(long amount, int vendorId) {
        this(amount, vendorId, LocalDate.now(), UUID.randomUUID());
    }

    public Transaction(long amount, int vendorId, LocalDate date) {
        this(amount,vendorId, date,UUID.randomUUID());
    }

    public Transaction(long amount, int vendorId, LocalDate date, UUID id) {
        this.amount = amount;
        this.vendorId = vendorId;
        this.date = date;
        this.id = id;
    }

    public static Function<String, Model> getParser() {
        return (String s) -> {
            String[] data = s.split(",");
            if (data.length != 4) throw new InvalidParameterException("input size must be 4");

            long amount;
            int vendorId;
            try {
                amount = Long.parseLong(data[0]);
                vendorId = Integer.parseInt(data[1]);
            } catch (NumberFormatException e) {
                Logger.getLogger(Transaction.class.getName()).severe(e.getMessage());
                return null;
            }

            LocalDate date;

            try {
                date = LocalDate.parse(data[2]);
            } catch (DateTimeParseException e) {
                Logger.getLogger(Transaction.class.getName()).severe(e.getMessage());
                return null;
            }

            UUID id;
            try {
                id = UUID.fromString(data[3].trim());
            } catch (IllegalArgumentException e) {
                Logger.getLogger(Transaction.class.getName()).severe(e.getMessage());
                return null;
            }

            return new Transaction(amount, vendorId, date, id);
        };
    }

    public long getAmount() {
        return amount;
    }

    public int getVendorId() {
        return vendorId;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getId() {
        return id.toString();
    }

    public static DataType getDataType() {
        return DataType.TRANSACTION;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s\n",
                this.getAmount(),
                this.getVendorId(),
                this.getDate(),
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
        if (!this.date.equals(that.date)) return false;
        return this.id.equals(that.id);
    }
}



