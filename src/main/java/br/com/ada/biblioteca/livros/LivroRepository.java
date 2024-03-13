package br.com.ada.biblioteca.livros;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends CrudRepository<Livro, Integer> {
    boolean existsByTituloAndAutor(String titulo, String autor);
    Optional<Livro> findByTituloAndAutor(String titulo, String autor);

    @Query("SELECT l FROM Livro l WHERE l.emprestado = false")
    List<Livro> findByEmprestadoFalse();


}
