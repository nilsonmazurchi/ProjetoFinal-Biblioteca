package br.com.ada.biblioteca.emprestimo;

import br.com.ada.biblioteca.livros.Livro;
import br.com.ada.biblioteca.livros.LivroService;
import br.com.ada.biblioteca.usuario.Usuario;
import br.com.ada.biblioteca.usuario.UsuarioDuplicadoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/biblioteca/emprestimo")
public class EmprestimoController {
    private EmprestimoService service;

    @Autowired
    public EmprestimoController(EmprestimoService service) {
        this.service = service;
    }

    //    @GetMapping("/lista")
//    public List<Livro> listarEmprestimos(){
//        return service.pegarTodosEmprestimos();
//    }
    @PostMapping("/novo")
    public ResponseEntity<Emprestimo> criarEmprestimo(
            @RequestPart String nomeUsuario,
            @RequestPart String emailUsuario,
            @RequestPart String senhaUsuario,
            @RequestPart String tituloLivro,
            @RequestPart String autorLivro) {

        // Crie instâncias de Usuario e Livro usando os parâmetros recebidos

        Usuario usuario = new Usuario();
        usuario.setNome(nomeUsuario);
        usuario.setEmail(emailUsuario);
        usuario.setSenha(senhaUsuario);

        Livro livro = new Livro();
        livro.setTitulo(tituloLivro);
        livro.setAutor(autorLivro);
        livro.setEmprestado(false);

        try {
            Emprestimo emprestimo = service.emprestarLivro(nomeUsuario, emailUsuario, tituloLivro, autorLivro);
            return new ResponseEntity<>(emprestimo, HttpStatus.CREATED);
        } catch (UsuarioDuplicadoException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PutMapping("/atualizar/{id}")
//    public ResponseEntity<String> atualizarLivro(@PathVariable Integer id, @RequestBody Livro livroAtualizado) {
//        String mensagem = service.atualizarLivro(id, livroAtualizado);
//        return ResponseEntity.ok(mensagem);
//    }

//    @DeleteMapping("/apagar/{id}")
//    public ResponseEntity<String> apagarLivro(@PathVariable Integer id) {
//        String mensagem = service.apagarLivro(id);
//        return ResponseEntity.ok(mensagem);
//    }

    @PutMapping("/devolver/{id}")
    public ResponseEntity<String> devolverLivro(@PathVariable Integer id) {
        try {
            Emprestimo emprestimo = service.devolverLivro(id);
            double multa = emprestimo.getMulta();

            if (multa > 0) {
                return ResponseEntity.ok("Você devolveu fora do prazo e sua multa é R$ " + multa);
            } else {
                return ResponseEntity.ok("Parabéns! Você devolveu no prazo.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/devolver/teste/{id}")
    public ResponseEntity<String> devolverLivro(@PathVariable Integer id, @RequestParam("dataDevolucao") String dataDevolucaoString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dataDevolucao = dateFormat.parse(dataDevolucaoString);

            Optional<Emprestimo> emprestimoOptional = service.buscarEmprestimoPorId(id);

            if (emprestimoOptional.isPresent()) {
                Emprestimo emprestimo = emprestimoOptional.get();
                Emprestimo emprestimoDevolvido = service.realizarDevolucao(id, dataDevolucao);

                // Verifica se há multa
                double multa = emprestimoDevolvido.getMulta();
                if (multa > 0.0) {
                    return ResponseEntity.ok("Você devolveu fora do prazo e sua multa é de R$ " + multa);
                } else {
                    return ResponseEntity.ok("Parabéns! Você devolveu no prazo.");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ParseException e) {
            // Trate o erro de parsing da data aqui
            return ResponseEntity.badRequest().body("Formato de data inválido. Use o formato yyyy-MM-dd.");
        }
    }

    @GetMapping("/abertos")
    public ResponseEntity<?> buscarEmprestimosEmAberto() {
        List<Emprestimo> emprestimos = service.buscarEmprestimosEmAberto();
        if (emprestimos.isEmpty()) {
            return ResponseEntity.ok().body("Não há empréstimos pendentes no momento.");
        } else {
            return ResponseEntity.ok().body(emprestimos);
        }
    }
}
