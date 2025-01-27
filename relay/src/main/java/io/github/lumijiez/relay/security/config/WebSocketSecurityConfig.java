package io.github.lumijiez.relay.security.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lumijiez.relay.security.jwt.JwtClaims;
import io.github.lumijiez.relay.security.jwt.JwtUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.Map;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketSecurityConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;

    public WebSocketSecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(
                        "https://lumijiez.site",
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
                .setHandshakeHandler(new DefaultHandshakeHandler())
                .addInterceptors(new CookieHandshakeInterceptor())
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                        message, StompHeaderAccessor.class
                );

                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = extractToken(accessor);

                    if (token != null) {
                        try {
                            JwtClaims claims = jwtUtil.validateAndGetClaims(token);

                            if (claims.isSuccess()) {
                                accessor.setUser(claims);
                                log.info("WebSocket authentication successful");
                            } else {
                                log.error("Invalid JWT token: {}", claims.getError());
                                throw new SecurityException("Invalid JWT token");
                            }
                        } catch (Exception e) {
                            log.error("Token validation error", e);
                            throw new SecurityException("Authentication failed");
                        }
                    } else {
                        log.error("No authentication token found");
                        throw new SecurityException("No authentication token");
                    }
                }

                return message;
            }

            private String extractToken(StompHeaderAccessor accessor) {
                Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

                if (sessionAttributes != null) {
                    return ((String) sessionAttributes.get("cookie")).split("=")[1];
                }

                return null;
            }
        });
    }
}