package br.com.ada.biblioteca.usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.List;


public class UsuarioDTO {
    private Usuario usuario;
    private List<Endereco> enderecos;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }
}
