package br.com.ada.biblioteca.livros;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/biblioteca/livro")
public class LivroController {
    private LivroService service;

    public LivroController(LivroService service){
        this.service =service;
    }
    @GetMapping("/lista")
    public List<Livro> listarLivros(){
        return service.pegarTodosLivros();
    }
    @PostMapping("/novo")
    public Livro criarLivro(@RequestBody Livro livro){
        service.salvarLivro(livro);
        return livro;
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarLivro(@PathVariable Integer id, @RequestBody Livro livroAtualizado) {
        String mensagem = service.atualizarLivro(id, livroAtualizado);
        return ResponseEntity.ok(mensagem);
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<String> apagarLivro(@PathVariable Integer id) {
        String mensagem = service.apagarLivro(id);
        return ResponseEntity.ok(mensagem);
    }

    @GetMapping("/disponiveis")
    public List<Livro> buscarLivrosNaoEmprestados() {
        return service.buscarLivrosNaoEmprestados();
    }


}
