package io.dsub.model;

import io.dsub.constants.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * An immutable transaction model
 */

@Builder
@With
@Getter
@AllArgsConstructor
public class Transaction extends Model implements Serializable {

    private final long amount;
    private final int vendorId;
    private final LocalDateTime time;
    private final UUID id;
    private static final long serialVersionUID = 1L;

    public Transaction(long amount, int vendorId) {
        this(amount, vendorId, LocalDateTime.now(), UUID.randomUUID());
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

            LocalDateTime time;

            try {
                time = LocalDateTime.parse(data[2]);
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

            return new Transaction(amount, vendorId, time, id);
        };
    }

    ///////////////////////////////////////////////////////////////////////////
    // Getters
    ///////////////////////////////////////////////////////////////////////////
    public long getAmount() {
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

    public static DataType getDataType() {
        return DataType.TRANSACTION;
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
        if (!this.time.equals(that.time)) return false;
        return this.id.equals(that.id);
    }
}



