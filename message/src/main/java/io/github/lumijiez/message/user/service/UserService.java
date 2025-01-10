package io.github.lumijiez.message.user.service;

import io.github.lumijiez.message.user.entity.User;
import io.github.lumijiez.message.user.repository.UserRepository;
import io.github.lumijiez.message.user.request.UserRequest;
import io.github.lumijiez.message.user.response.UserResponse;
import io.jsonwebtoken.Claims;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserResponse> getSelf(String sub, UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findByUserId(userRequest.getUserId());

        if (existingUser.isPresent()) return existingUser.map(UserResponse::from);

        return Optional.of(addUser(sub));
    }

    public Optional<UserResponse> getUser(UserRequest userRequest) {
        Optional<User> user = userRepository.findByUserId(userRequest.getUserId());
        return user.map(UserResponse::from);
    }

    public UserResponse addUser(String sub) {
        UUID userId = UUID.fromString(sub);
        User newUser = new User();
        newUser.setUserId(userId);
        userRepository.save(newUser);

        return UserResponse.from(newUser);
    }
}
