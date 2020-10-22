package io.dsub.model;

import io.dsub.constants.DataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class VendorTest {

    Vendor vendor;

    @BeforeEach
    void prep() {
        Random rand = new Random();
        this.vendor = new Vendor(rand.nextInt(), Integer.toHexString(rand.nextInt()), rand.nextInt());
    }

    @Test
    void getParser() {
        Function<String, Model> parser = Vendor.getParser();
        Vendor other = (Vendor) parser.apply(vendor.toString());
        assertEquals(other.getCatId(), vendor.getCatId());
        assertEquals(other.getId(), vendor.getId());
        assertEquals(other.getName(), vendor.getName());
        assertNotSame(other, vendor);
    }

    @Test
    void getDataType() {
        assertEquals(DataType.VENDOR, Vendor.getDataType());
    }
}