package app.services;

import app.dao.SupplierRepository;
import app.entities.Address;
import app.entities.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> getFilteredAndSorted(String search, String sortBy) {
        List<Supplier> suppliers = supplierRepository.findAll();

        if (search != null && !search.trim().isEmpty()) {
            String s = search.toLowerCase();
            suppliers = suppliers.stream()
                    .filter(sup ->
                            sup.getName().toLowerCase().contains(s) ||
                                    (sup.getRepresentative() != null && sup.getRepresentative().toLowerCase().contains(s)) ||
                                    (sup.getPhone() != null && sup.getPhone().toLowerCase().contains(s)) ||
                                    (sup.getAddress() != null &&
                                            (sup.getAddress().getCity().toLowerCase().contains(s) ||
                                                    sup.getAddress().getStreet().toLowerCase().contains(s) ||
                                                    sup.getAddress().getCountry().toLowerCase().contains(s)))
                    )
                    .collect(Collectors.toList());
        }

        if ("name".equalsIgnoreCase(sortBy)) {
            suppliers.sort(Comparator.comparing(Supplier::getName));
        } else if ("representative".equalsIgnoreCase(sortBy)) {
            suppliers.sort(Comparator.comparing(Supplier::getRepresentative));
        } else if ("phone".equalsIgnoreCase(sortBy)) {
            suppliers.sort(Comparator.comparing(Supplier::getPhone));
        } else if ("country".equalsIgnoreCase(sortBy)) {
            suppliers.sort(Comparator.comparing(s -> s.getAddress().getCountry(), Comparator.nullsLast(String::compareTo)));
        }

        return suppliers;
    }

    public Supplier create(Supplier supplier) {
        validate(supplier);
        return supplierRepository.save(supplier);
    }

    public Supplier update(int id, Supplier updated) {
        Optional<Supplier> optional = supplierRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("Поставщик не найден: " + id);
        }

        validate(updated);

        Supplier supplier = optional.get();
        supplier.setName(updated.getName());
        supplier.setRepresentative(updated.getRepresentative());
        supplier.setPhone(updated.getPhone());

        Address address = supplier.getAddress();
        if (address == null) address = new Address();
        Address updatedAddr = updated.getAddress();
        address.setCity(updatedAddr.getCity());
        address.setStreet(updatedAddr.getStreet());
        address.setCountry(updatedAddr.getCountry());
        supplier.setAddress(address);

        return supplierRepository.save(supplier);
    }

    public void delete(int id) {
        supplierRepository.deleteById(id);
    }

    private void validate(Supplier supplier) {
        if (supplier.getName() == null || supplier.getName().trim().isEmpty()
                || supplier.getRepresentative() == null || supplier.getRepresentative().trim().isEmpty()
                || supplier.getPhone() == null || supplier.getPhone().trim().isEmpty()
                || supplier.getAddress() == null
                || supplier.getAddress().getCity() == null || supplier.getAddress().getCity().trim().isEmpty()
                || supplier.getAddress().getStreet() == null || supplier.getAddress().getStreet().trim().isEmpty()
                || supplier.getAddress().getCountry() == null || supplier.getAddress().getCountry().trim().isEmpty()) {
            throw new RuntimeException("Все поля поставщика и адреса должны быть заполнены");
        }
    }
}
