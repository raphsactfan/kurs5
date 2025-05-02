package app.services;

import app.dao.UserRepository;
import app.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public boolean authenticate(String login, String password) {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isEmpty()) {
            System.out.println("Пользователь не найден: " + login);
            return false;
        }

        String raw = password;
        String hashed = user.get().getPassword();

        System.out.println("Введённый пароль: " + raw);
        System.out.println("Пароль из БД:    " + hashed);
        System.out.println("Совпадение: " + encoder.matches(raw, hashed));

        return encoder.matches(raw, hashed);
    }

    public boolean register(User user) {
        String login = user.getLogin();
        String password = user.getPassword();

        // Проверка длины
        if (login.length() < 5 || password.length() < 5) {
            System.out.println("Слишком короткий логин или пароль");
            return false;
        }

        // Проверка пробелов
        if (login.contains(" ") || password.contains(" ")) {
            System.out.println("Логин или пароль содержит пробел");
            return false;
        }

        Optional<User> existing = userRepository.findByLogin(login);
        if (existing.isPresent()) {
            System.out.println("Пользователь уже существует: " + login);
            return false;
        }

        user.setPassword(encoder.encode(password));
        user.setRole("user");

        userRepository.save(user);
        System.out.println("Пользователь зарегистрирован: " + login);
        return true;
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + login));
    }

}

