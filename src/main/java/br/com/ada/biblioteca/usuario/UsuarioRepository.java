package br.com.ada.biblioteca.usuario;
import java.util.Optional;


import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
    boolean existsByNomeAndEmail(String nome, String email);
    Optional<Usuario> findByNomeAndEmail(String nome, String email);
}
