package ru.skypro.homework.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return User.builder()
                .username(userEntity.getEmail())
                .password(userEntity.getPassword())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name())
                ))
                .accountExpired(!userEntity.isAccountNonExpired())
                .accountLocked(!userEntity.isAccountNonLocked())
                .credentialsExpired(!userEntity.isCredentialsNonExpired())
                .disabled(!userEntity.isEnabled())
                .build();
    }

    public void createUser(Register register) {

        if (userExists(register.getUsername())) {
            throw new IllegalArgumentException("User already exists with username/email: " + register.getUsername());
        }

        //Создаем новую сущность UserEntity
        UserEntity newUser = new UserEntity();
        newUser.setEmail(register.getUsername());

        //Кодируем пароль из UserDetails перед сохранением. UserDetails, переданный в этот метод, должен содержать НЕзакодированный пароль!
        newUser.setPassword(passwordEncoder.encode(register.getPassword()));

        newUser.setRole(register.getRole());
        newUser.setFirstName(register.getFirstName());
        newUser.setLastName(register.getLastName());
        newUser.setPhone(register.getPhone());
        newUser.setImage(null);

        //Установливаем флаги учетной записи по умолчанию
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialsNonExpired(true);
        newUser.setEnabled(true);

        userRepository.save(newUser);
    }

    @Override
    public void createUser(UserDetails user) {
        // Не используется в текущей логике
        throw new UnsupportedOperationException("Use UserService for user updates");
    }

    @Override
    public void updateUser(UserDetails user) {
        // Не используется в текущей логике
        throw new UnsupportedOperationException("Use UserService for user updates");
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteByEmail(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // Не используется в текущей логике
        throw new UnsupportedOperationException("Use UserService.changePassword instead");
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByEmail(username).isPresent();
    }
}