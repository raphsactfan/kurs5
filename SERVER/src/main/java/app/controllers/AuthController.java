package app.controllers;

import app.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.services.AuthService;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        boolean ok = authService.authenticate(user.getLogin(), user.getPassword());
        if (ok) {
            User foundUser = authService.getUserByLogin(user.getLogin());
            return ResponseEntity.ok(Map.of(
                    "status", "ok",
                    "id", foundUser.getId(),
                    "login", foundUser.getLogin(),
                    "role", foundUser.getRole()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error"));
        }
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        boolean success = authService.register(user);
        return success ? "ok" : "exists";
    }


}
