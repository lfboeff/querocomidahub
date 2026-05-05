package br.com.fiap.querocomidahub.exception;

public class UserLoginFailedException extends RuntimeException {
    public UserLoginFailedException() {
        super("Invalid login or password");
    }
}
