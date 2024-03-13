package br.com.ada.biblioteca.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/biblioteca/usuario")
public class UsuarioController {
    private UsuarioService service;

    public UsuarioController(UsuarioService service){
        this.service =service;
    }

    @GetMapping("/lista")
    public List<Usuario> listarUsuarios(){
        return service.pegarTodosUsuarios();
    }

    @PostMapping("/novo")
    public ResponseEntity<String> criarUsuario(@RequestBody Usuario usuario) {
        try{
            Usuario usuarioSalvo = service.salvarUsuario(usuario);
            return new ResponseEntity<>("Usuário e Endereço salvos com sucesso", HttpStatus.CREATED);
        } catch (UsuarioDuplicadoException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }


    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarUsuario(
            @PathVariable Integer id,
            @RequestBody Usuario usuarioAtualizado){

        String mensagem = service.atualizarUsuarioPorId(id, usuarioAtualizado);

        if (mensagem.startsWith("Digite a senha")) {
            return ResponseEntity.ok(mensagem);
        } else {
            return ResponseEntity.badRequest().body(mensagem);
        }
    }

    @PutMapping("/atualizar/endereco/{usuarioId}/{enderecoId}")
    public ResponseEntity<String> atualizarEndereco(
            @PathVariable Integer usuarioId,
            @PathVariable Integer enderecoId,
            @RequestBody Endereco enderecoAtualizado) {

        String mensagem = service.atualizarEnderecoPorId(usuarioId, enderecoId, enderecoAtualizado);

        if (mensagem.startsWith("Endereço com ID")) {
            return ResponseEntity.ok(mensagem);
        } else {
            return ResponseEntity.badRequest().body(mensagem);
        }
    }

    @DeleteMapping("/apagar/endereco/{usuarioId}/{enderecoId}")
    public ResponseEntity<String> apagarEndereco(
            @PathVariable Integer usuarioId,
            @PathVariable Integer enderecoId) {

        String mensagem = service.apagarEnderecoPorId(usuarioId, enderecoId);

        if (mensagem.startsWith("Endereço com ID")) {
            return ResponseEntity.ok(mensagem);
        } else {
            return ResponseEntity.badRequest().body(mensagem);
        }
    }

    @PostMapping("/adicionarendereco/{usuarioId}")
    public ResponseEntity<String> adicionarEndereco(
            @PathVariable Integer usuarioId,
            @RequestBody Endereco novoEndereco) {

        String mensagem = service.adicionarEndereco(usuarioId, novoEndereco);

        if (mensagem.startsWith("Novo endereço adicionado")) {
            return ResponseEntity.ok(mensagem);
        } else {
            return ResponseEntity.badRequest().body(mensagem);
        }
    }

    @PutMapping("/desativar/{id}")
    public ResponseEntity<String> desativarUsuario(@PathVariable Integer id) {
        String mensagem = service.desativarUsuario(id);

        if (mensagem.startsWith("O usuário")) {
            return ResponseEntity.ok(mensagem);
        } else if (mensagem.equals("O usuário não pode ser desativado devido a empréstimos pendentes de devolução.")) {
            return ResponseEntity.badRequest().body(mensagem);
        } else {
            return ResponseEntity.notFound().build(); // Usuário não encontrado
        }
    }
    @GetMapping("/emprestimos-pendentes")
    public ResponseEntity<List<Usuario>> buscarUsuariosComEmprestimosPendentes() {
        List<Usuario> usuarios = service.buscarUsuariosComEmprestimosPendentes();
        return ResponseEntity.ok(usuarios);
    }
}
