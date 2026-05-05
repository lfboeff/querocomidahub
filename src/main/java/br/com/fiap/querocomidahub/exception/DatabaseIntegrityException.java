package br.com.fiap.querocomidahub.exception;

public class DatabaseIntegrityException extends RuntimeException {
    public DatabaseIntegrityException(String message) {
        super(message);
    }
}
