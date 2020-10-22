package io.dsub.service;

import io.dsub.model.Vendor;

import java.util.Collection;

public class MockVendorService implements ModelService<Vendor>{

    @Override
    public String save(Vendor item) {
        System.out.println("Vendor Save");
        return "0";
    }

    @Override
    public void saveAll(Collection<Vendor> items) {
        System.out.println("Vendor SaveAll");
    }

    /**
     * Find item by name
     * If the given subclass did not implemented the method, it should throw {@link UnsupportedOperationException}
     *
     * @param name of item
     * @return item
     * @throws UnsupportedOperationException if unimplemented
     */
    @Override
    public Vendor find(String name) throws UnsupportedOperationException {
        return null;
    }

    /**
     * Find all items by name
     * If the given subclass did not implemented the method, it should throw {@link UnsupportedOperationException}
     *
     * @param name of item
     * @return item or null if not found
     * @throws UnsupportedOperationException if unimplemented
     */
    @Override
    public Vendor findByName(String name) throws UnsupportedOperationException {
        return null;
    }

    /**
     * Finds item by id
     *
     * @param id of item
     * @return item or null if not found
     */
    @Override
    public Vendor findById(String id) {
        return null;
    }

    @Override
    public void delete(Vendor item) {
        System.out.println("Vendor SaveDelete");
    }

    /**
     * Delete item of id if exists
     * It will either delete or ignore depends on the existence of target
     *
     * @param id of item to be deleted
     */
    @Override
    public void deleteById(String id) {

    }

    /**
     * Deletes an item by name.
     * <p>
     * If the given subclass did not implemented the method, it should throw
     * {@link UnsupportedOperationException}
     *
     * @param name of target item
     * @throws UnsupportedOperationException if unimplemented
     */
    @Override
    public void deleteByName(String name) throws UnsupportedOperationException {

    }

    @Override
    public void deleteAll(Collection<Vendor> item) {
        System.out.println("Vendor DeleteAll");
    }
}
