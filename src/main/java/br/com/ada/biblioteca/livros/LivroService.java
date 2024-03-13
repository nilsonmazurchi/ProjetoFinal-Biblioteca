package br.com.ada.biblioteca.livros;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

    private LivroRepository repository;

    public LivroService(LivroRepository repository){
        this.repository = repository;
    }

    public void salvarLivro(Livro livro){
        repository.save(livro);
    }

    public List<Livro> pegarTodosLivros(){
        return (List<Livro>) repository.findAll();    }

    public String atualizarLivro(Integer id, Livro livroAtualizado) {
        Livro livroExistente = repository.findById(id).orElse(null);
        if (livroExistente != null) {
            livroExistente.setTitulo(livroAtualizado.getTitulo());
            livroExistente.setAutor(livroAtualizado.getAutor());
            livroExistente.setAno(livroAtualizado.getAno());
            livroExistente.setIsbn(livroAtualizado.getIsbn());
            livroExistente.setEmprestado(livroAtualizado.getEmprestado());

            repository.save(livroExistente);

            return "O livro '" + livroExistente.getTitulo() + "' com id " + id + " foi atualizado com sucesso!";
        } else {
            return "Livro com id " + id + " não encontrado.";
        }
        }

    public String apagarLivro(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "O livro com id " + id + " foi apagado com sucesso!";
        } else {
            return "Livro com id " + id + " não encontrado.";
        }
    }

    public List<Livro> buscarLivrosNaoEmprestados() {
        return repository.findByEmprestadoFalse();
    }
}
