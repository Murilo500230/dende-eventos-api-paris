package br.com.softhouse.dende.exceptions;

public class EventoNaoEncontradoException extends RuntimeException {
    public EventoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
