package org.example.services.persistance;

import org.example.models.AppUser;
import org.example.models.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDbService {

    private final UserRepository userRepository;

    public UserDbService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser saveUser(AppUser user) {
        return userRepository.save(user);
    }

    public AppUser createUser(AppUser user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }
        return userRepository.save(user);
    }

    public Optional<AppUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public List<AppUser> getServiceWithLimit(int limit){
        return userRepository.findAll(PageRequest.of(0, limit)).getContent();
    }

    public List<AppUser> getUserUnique(int limit, int offset) {
        return userRepository.findUniqueUser(limit, (offset * limit));
    }
}
