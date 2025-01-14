package io.github.lumijiez.message.domain.user.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@Document(collection = "users")
public class User {

    @Id
    private UUID id;
    private String username;
    private String email;
}
