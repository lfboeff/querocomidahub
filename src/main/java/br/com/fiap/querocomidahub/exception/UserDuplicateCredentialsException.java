package br.com.fiap.querocomidahub.exception;

public class UserDuplicateCredentialsException extends RuntimeException {
    public UserDuplicateCredentialsException(String message) {
        super(message);
    }
}
