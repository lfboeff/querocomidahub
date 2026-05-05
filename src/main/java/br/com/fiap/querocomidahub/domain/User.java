package br.com.fiap.querocomidahub.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class User {
    private Long id;
    private UserType type;
    private String name;
    private String email;
    private String login;
    private String password;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private Address address;
}
