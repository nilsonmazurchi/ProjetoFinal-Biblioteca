package br.com.ada.biblioteca.usuario;

public class UsuarioDuplicadoException extends RuntimeException{
    public UsuarioDuplicadoException(String message){
        super(message);
    }
}
