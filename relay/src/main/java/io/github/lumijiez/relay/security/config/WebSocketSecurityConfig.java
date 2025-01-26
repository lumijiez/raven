package io.github.lumijiez.relay.security.config;

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
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

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
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
                log.info("FULL MESSAGE: {}", message);

                log.info("MESSAGE HEADERS: {}", message.getHeaders());

                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                        message, StompHeaderAccessor.class
                );

                if (accessor != null) {
                    log.info("ACCESSOR COMMAND: {}", accessor.getCommand());
                    log.info("ACCESSOR HEADERS: {}", accessor.getMessageHeaders());

                    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                        log.info("CONNECT HEADERS: {}", accessor.getMessageHeaders());
                    }
                }

                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = extractTokenFromCookie();

                    log.info("EXTRACTED TOKEN: {}", token);

                    if (token != null) {
                        try {
                            JwtClaims claims = jwtUtil.validateAndGetClaims(token);

                            log.info("JWT CLAIMS SUCCESS: {}", claims.isSuccess());
                            log.info("JWT CLAIMS DETAILS: {}", claims);

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

            private String extractTokenFromCookie() {
                try {
                    ServletRequestAttributes requestAttributes =
                            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                    if (requestAttributes == null) {
                        log.error("RequestContextHolder returned null attributes");
                        return null;
                    }

                    HttpServletRequest request = requestAttributes.getRequest();

                    log.info("REQUEST METHOD: {}", request.getMethod());
                    log.info("REQUEST URI: {}", request.getRequestURI());
                    log.info("REQUEST HEADERS:");
                    java.util.Enumeration<String> headerNames = request.getHeaderNames();
                    while (headerNames.hasMoreElements()) {
                        String headerName = headerNames.nextElement();
                        log.info("{}: {}", headerName, request.getHeader(headerName));
                    }

                    Cookie[] cookies = request.getCookies();
                    log.info("TOTAL COOKIES COUNT: {}", cookies != null ? cookies.length : 0);

                    if (cookies != null) {
                        for (Cookie cookie : cookies) {
                            log.info("COOKIE NAME: {}, VALUE: {}, DOMAIN: {}, PATH: {}, MAX AGE: {}, SECURE: {}, HTTP ONLY: {}",
                                    cookie.getName(),
                                    cookie.getValue(),
                                    cookie.getDomain(),
                                    cookie.getPath(),
                                    cookie.getMaxAge(),
                                    cookie.getSecure(),
                                    cookie.isHttpOnly()
                            );

                            if ("authToken".equals(cookie.getName())) {
                                return cookie.getValue();
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("Comprehensive cookie extraction error", e);
                }
                return null;
            }
        });
    }
}