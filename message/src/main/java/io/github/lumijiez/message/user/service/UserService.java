package io.github.lumijiez.message.user.service;

import io.github.lumijiez.message.jwt.JwtClaims;
import io.github.lumijiez.message.user.entity.User;
import io.github.lumijiez.message.user.exception.UserNotFoundException;
import io.github.lumijiez.message.user.repository.UserRepository;
import io.github.lumijiez.message.user.dto.response.GetSelfResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public GetSelfResponseDTO get(JwtClaims claims) {
        User user = userRepository
                .findById(claims.getSub())
                .map(existingUser -> {
                    boolean needsUpdate = false;

                    if (!existingUser.getEmail().equals(claims.getEmail())) {
                        existingUser.setEmail(claims.getEmail());
                        needsUpdate = true;
                    }
                    if (!existingUser.getUsername().equals(claims.getUsername())) {
                        existingUser.setUsername(claims.getUsername());
                        needsUpdate = true;
                    }

                    return needsUpdate ? userRepository.save(existingUser) : existingUser;
                })
                .orElse(userRepository.save(User.builder()
                        .id(claims.getSub())
                        .email(claims.getEmail())
                        .username(claims.getUsername())
                        .userChats(new ArrayList<>())
                        .build()));

        return GetSelfResponseDTO.from(user, "User data synchronized successfully.");
    }

    public void exists(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException();
        }
    }

    public List<User> findAllByIds(List<UUID> ids) {
        return userRepository.findAllById(ids.stream().map(UUID::toString).toList());
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
