package ru.gb.TrainingNotes.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gb.TrainingNotes.model.Role;
import ru.gb.TrainingNotes.model.User;
import ru.gb.TrainingNotes.repository.RoleRepository;
import ru.gb.TrainingNotes.repository.UserRepository;


import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void saveUser(User user) {
        User createdUser = new User();
        createdUser.setName(user.getName());
        createdUser.setLogin(user.getLogin());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if (role == null) {
            role = roleCheck();
        }
        createdUser.setRoles(List.of(role));
        userRepository.save(createdUser);
    }
    private Role roleCheck() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));
    }
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    @Transactional
    public User updateUsers(Long id, User user) {
        User updateUser = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        updateUser.setName(user.getName());
        updateUser.setLogin(user.getLogin());
        updateUser.setTrainings(user.getTrainings());
        return userRepository.save(updateUser);
    }

}
