package app.services;

import app.dao.UserRepository;
import app.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public List<User> getAll(String search, String sortBy) {
        List<User> users = userRepository.findAll();

        if (search != null && !search.isEmpty()) {
            users = users.stream()
                    .filter(u -> u.getLogin().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if ("login".equalsIgnoreCase(sortBy)) {
            users.sort(Comparator.comparing(User::getLogin));
        } else if ("role".equalsIgnoreCase(sortBy)) {
            users.sort(Comparator.comparing(User::getRole));
        }

        return users;
    }

    public User create(User user) {
        String login = user.getLogin();
        String password = user.getPassword();

        if (login == null || login.trim().length() < 5 || login.contains(" ")) {
            throw new RuntimeException("Логин должен содержать не менее 5 символов и не содержать пробелов");
        }

        if (password == null || password.trim().length() < 5 || password.contains(" ")) {
            throw new RuntimeException("Пароль должен содержать не менее 5 символов и не содержать пробелов");
        }

        if (userRepository.findByLogin(login).isPresent()) {
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }

        user.setPassword(encoder.encode(password));
        return userRepository.save(user);
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public User update(User existing, User updated) {
        String newLogin = updated.getLogin();
        String newPassword = updated.getPassword();

        if (newLogin == null || newLogin.trim().length() < 5 || newLogin.contains(" ")) {
            throw new RuntimeException("Логин должен быть не менее 5 символов и не содержать пробелов");
        }

        if (newPassword == null || newPassword.trim().length() < 5 || newPassword.contains(" ")) {
            throw new RuntimeException("Пароль должен быть не менее 5 символов и не содержать пробелов");
        }

        if (!existing.getLogin().equals(newLogin)) {
            Optional<User> other = userRepository.findByLogin(newLogin);
            if (other.isPresent()) {
                throw new RuntimeException("Логин уже занят другим пользователем");
            }
        }

        existing.setLogin(newLogin);
        existing.setRole(updated.getRole());

        if (!newPassword.equals(existing.getPassword())) {
            existing.setPassword(encoder.encode(newPassword));
        }

        return userRepository.save(existing);
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
