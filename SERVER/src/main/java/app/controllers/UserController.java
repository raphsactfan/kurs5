package app.controllers;

import app.entities.User;
import app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy
    ) {
        return userService.getAll(search, sortBy);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody User updatedUser) {
        Optional<User> optional = userService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(404).body("Пользователь не найден: " + id);
        }

        try {
            User saved = userService.update(optional.get(), updatedUser);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }
}
