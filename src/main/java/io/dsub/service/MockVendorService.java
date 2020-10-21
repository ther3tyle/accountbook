package io.dsub.service;

import io.dsub.model.Vendor;

import java.util.Collection;

public class MockVendorService implements ModelService<Vendor>{


    @Override
    public void save(Vendor item) {
        System.out.println("Vendor Save");
    }

    @Override
    public void saveAll(Collection<Vendor> items) {
        System.out.println("Vendor SaveAll");
    }

    @Override
    public void delete(Vendor item) {
        System.out.println("Vendor SaveDelete");
    }

    @Override
    public void deleteAll(Collection<Vendor> item) {
        System.out.println("Vendor DeleteAll");
    }
}
