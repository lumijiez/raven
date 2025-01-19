package io.github.lumijiez.relay.config;

import io.github.lumijiez.relay.security.jwt.JwtClaims;
import io.github.lumijiez.relay.security.jwt.JwtUtil;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(
                        "http://*:[*]",
                        "https://*:[*]",
                        "http://*",
                        "https://*",
                        "capacitor://*",
                        "ionic://*",
                        "file://*",
                        "file://[*]",
                        "null"
                )
                .withSockJS();
    }
}
