package io.dsub.service;

import io.dsub.AppState;
import io.dsub.model.Vendor;
import io.dsub.repository.VendorRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VendorServiceImpl implements VendorService {

    VendorRepository repository;

    public VendorServiceImpl() throws SQLException {
        this(new VendorRepository(AppState.getInstance().getConn()));
    }

    public VendorServiceImpl(VendorRepository repository) {
        this.repository = repository;
    }

    @Override
    public String save(Vendor item) {
        try {
            return this.repository.save(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveAll(Collection<Vendor> items) {
        try {
            this.repository.saveAll(items);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vendor find(String name) throws UnsupportedOperationException {
        return this.findByName(name);
    }

    @Override
    public Vendor findByName(String name) throws UnsupportedOperationException {
        try {
            return this.repository.findByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Vendor findById(String id) {
        try {
            return this.repository.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Vendor> findAll() {

        List<Vendor> items = new ArrayList<>();
        try {
            items.addAll(this.repository.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public void delete(Vendor item) {
        try {
            repository.delete(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            repository.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByName(String name) throws UnsupportedOperationException {
        try {
            repository.deleteByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll(Collection<Vendor> items) {
        try {
            for (Vendor item : items) {
                this.repository.delete(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
