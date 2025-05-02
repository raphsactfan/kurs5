package app.controllers;

import app.entities.Supplier;
import app.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public List<Supplier> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy
    ) {
        return supplierService.getFilteredAndSorted(search, sortBy);
    }

    @PostMapping
    public Supplier create(@RequestBody Supplier supplier) {
        return supplierService.create(supplier);
    }

    @PutMapping("/{id}")
    public Supplier update(@PathVariable int id, @RequestBody Supplier updated) {
        return supplierService.update(id, updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        supplierService.delete(id);
    }
}
