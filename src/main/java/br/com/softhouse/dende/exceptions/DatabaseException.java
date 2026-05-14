package br.com.softhouse.dende.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
