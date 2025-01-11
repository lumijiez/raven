package io.github.lumijiez.message.user.service;

import io.github.lumijiez.message.jwt.JwtClaims;
import io.github.lumijiez.message.user.entity.User;
import io.github.lumijiez.message.user.repository.UserRepository;
import io.github.lumijiez.message.user.response.GetUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<GetUserResponse> get(JwtClaims claims) {
        Optional<User> userOptional = userRepository.findById(claims.getSub());

        if (userOptional.isPresent()) {
            return GetUserResponse.success(userOptional.get(), "User found successfully.");
        } else {
            User user = User.builder()
                    .id(claims.getSub())
                    .email(claims.getEmail())
                    .username(claims.getUsername())
                    .userChats(new ArrayList<>())
                    .build();
            userRepository.save(user);
            return GetUserResponse.success(user, "User created successfully.");
        }
    }
}
