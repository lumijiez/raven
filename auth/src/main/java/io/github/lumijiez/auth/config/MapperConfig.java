package io.github.lumijiez.auth.config;

import io.github.lumijiez.auth.domain.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class MapperConfig {

    private final BCryptPasswordEncoder passwordEncoder;

    public MapperConfig(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.createTypeMap(RegisterRequestDTO.class, User.class)
                .addMappings(m -> m.skip(User::setPassword))
                .setPostConverter(context -> {
                    User user = context.getDestination();
                    RegisterRequestDTO request = context.getSource();
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    return user;
                });

        return mapper;
    }
}