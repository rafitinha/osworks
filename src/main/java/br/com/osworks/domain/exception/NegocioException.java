package br.com.osworks.domain.exception;

public class NegocioException extends  RuntimeException {

    public NegocioException(String message){
        super(message);
    }
}
