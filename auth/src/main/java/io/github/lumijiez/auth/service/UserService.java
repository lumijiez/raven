package io.github.lumijiez.auth.service;

import io.github.lumijiez.auth.domain.entity.TwoFactorAuth;
import io.github.lumijiez.auth.domain.entity.User;
import io.github.lumijiez.auth.dto.request.FindUserRequestDTO;
import io.github.lumijiez.auth.dto.response.UserDetailsDTO;
import io.github.lumijiez.auth.exception.AuthException;
import io.github.lumijiez.auth.exception.IncorrectCredentialsException;
import io.github.lumijiez.auth.exception.UserAlreadyExistsException;
import io.github.lumijiez.auth.exception.UserNotFoundException;
import io.github.lumijiez.auth.repository.UserRepository;
import io.github.lumijiez.auth.dto.request.LoginRequestDTO;
import io.github.lumijiez.auth.dto.request.RegisterRequestDTO;
import io.github.lumijiez.auth.dto.response.AuthResponseDTO;
import io.github.lumijiez.auth.security.JwtHelper;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final TwoFactorAuthService twoFactorService;
    private final JwtHelper jwtHelper;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponseDTO initiateRegistration(RegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("username", request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("email", request.getEmail());
        }

        twoFactorService.generateAndSendRegistrationCode(request);
        return AuthResponseDTO.requiresTwoFactor(request.getEmail());
    }

    @Transactional
    public AuthResponseDTO completeRegistration(String email, String code) {
        TwoFactorAuth tfa = twoFactorService.validateRegistrationCode(email, code)
                .orElseThrow(() -> new AuthException("Not found!"));

        User user = User.builder()
                .username(tfa.getUsername())
                .email(tfa.getEmail())
                .password(tfa.getPassword())
                .build();

        userRepository.save(user);
        tfa.setUsed(true);

        return AuthResponseDTO.from(jwtHelper.generateTokenForUser(user));
    }

    @Transactional
    public AuthResponseDTO initiateLogin(LoginRequestDTO request) {
        User user = userRepository
                .findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getUsernameOrEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IncorrectCredentialsException();
        }

        twoFactorService.generateAndSendLoginCode(user);
        return AuthResponseDTO.requiresTwoFactor(user.getEmail());
    }

    @Transactional
    public AuthResponseDTO completeLogin(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        TwoFactorAuth tfa = twoFactorService.validateLoginCode(email, code)
                .orElseThrow(() -> new InvalidTwoFactorCodeException());

        tfa.setUsed(true);
        return AuthResponseDTO.from(jwtHelper.generateTokenForUser(user));
    }
}


