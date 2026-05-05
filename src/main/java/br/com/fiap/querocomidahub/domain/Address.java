package br.com.fiap.querocomidahub.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class Address {
    private Long id;
    private Long userId;
    private String zipCode;
    private String countryCode;
    private String stateCode;
    private String city;
    private String street;
    private String number;
    private String complement;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
