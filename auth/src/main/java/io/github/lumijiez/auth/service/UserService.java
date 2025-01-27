package io.github.lumijiez.auth.service;

import io.github.lumijiez.auth.domain.entity.User;
import io.github.lumijiez.auth.dto.request.FindUserRequestDTO;
import io.github.lumijiez.auth.dto.response.UserDetailsDTO;
import io.github.lumijiez.auth.exception.IncorrectCredentialsException;
import io.github.lumijiez.auth.exception.UserAlreadyExistsException;
import io.github.lumijiez.auth.exception.UserNotFoundException;
import io.github.lumijiez.auth.repository.UserRepository;
import io.github.lumijiez.auth.dto.request.LoginRequestDTO;
import io.github.lumijiez.auth.dto.request.RegisterRequestDTO;
import io.github.lumijiez.auth.dto.response.AuthResponseDTO;
import io.github.lumijiez.auth.security.JwtHelper;
import jakarta.security.auth.message.AuthException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    private final TwoFactorAuthService twoFactorService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtHelper jwtHelper,
                       ModelMapper modelMapper,
                       TwoFactorAuthService twoFactorService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
        this.modelMapper = modelMapper;
        this.twoFactorService = twoFactorService;
    }

    @Transactional
    public AuthResponseDTO registerUser(RegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new UserAlreadyExistsException("username", request.getUsername());

        if (userRepository.existsByEmail(request.getEmail()))
            throw new UserAlreadyExistsException("email", request.getEmail());

        User user = modelMapper.map(request, User.class);
        userRepository.save(user);

        return authenticateUser(
                LoginRequestDTO.builder()
                        .usernameOrEmail(user.getUsername())
                        .password(request.getPassword())
                        .build());
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO authenticateUser(LoginRequestDTO request) {
        User user = userRepository
                .findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getUsernameOrEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new IncorrectCredentialsException();

        twoFactorService.generateAndSendCode(user);

        return AuthResponseDTO.builder()
                .token(null)
                .requiresTwoFactor(true)
                .build();
    }

    @Transactional
    public AuthResponseDTO verifyTwoFactorCode(String usernameOrEmail, String code) throws AuthException {
        User user = userRepository
                .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UserNotFoundException(usernameOrEmail));

        if (!twoFactorService.validateCode(user, code)) {
            throw new AuthException("2FA Code Incorrect");
        }

        return AuthResponseDTO.from(jwtHelper.generateTokenForUser(user));
    }

    @Transactional(readOnly = true)
    public UserDetailsDTO findUser(FindUserRequestDTO request) {
        User user = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getUsernameOrEmail()));

        return modelMapper.map(user, UserDetailsDTO.class);
    }
}

