package br.com.ada.biblioteca.emprestimo;


import br.com.ada.biblioteca.livros.Livro;
import br.com.ada.biblioteca.usuario.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmprestimoRepository extends CrudRepository<Emprestimo, Integer> {
    boolean existsByUsuarioAndDataDevolucaoIsNull(Usuario usuario);

    @Query("SELECT e.usuario FROM Emprestimo e WHERE e.dataDevolucao IS NULL")
    List<Usuario> findUsuariosComEmprestimosPendentes();


}
