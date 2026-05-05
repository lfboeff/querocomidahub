package br.com.fiap.querocomidahub.exception;

public class UserInvalidPasswordException extends RuntimeException {
    public UserInvalidPasswordException() {
        super("Current password is incorrect");
    }
}
