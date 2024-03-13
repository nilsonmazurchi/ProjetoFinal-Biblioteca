package br.com.ada.biblioteca.usuario;

import br.com.ada.biblioteca.emprestimo.EmprestimoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;
    private EnderecoRepository enderecoRepository;
    private EmprestimoRepository emprestimoRepository;


    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, EnderecoRepository enderecoRepository, EmprestimoRepository emprestimoRepository) {

        this.usuarioRepository = usuarioRepository;
        this.enderecoRepository = enderecoRepository;

        this.emprestimoRepository = emprestimoRepository;
    }
    @Transactional
    public Usuario salvarUsuario(@Valid Usuario usuario) {
          for (Endereco endereco : usuario.getEnderecos()) {
            endereco.setUsuario(usuario);

            // Verifica se o endereço é o principal (primeiro endereço adicionado)
            if (endereco.equals(usuario.getEnderecos().get(0))) {
                endereco.setTipo(TipoEndereco.Principal);
            } else {
                endereco.setTipo(TipoEndereco.Secundario);
            }
        }

        return usuarioRepository.save(usuario);
    }


    public List<Usuario> pegarTodosUsuarios() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Transactional
    public String atualizarUsuarioPorId(Integer id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElse(null);

        if (usuarioExistente != null) {
                usuarioExistente.setNome(usuarioAtualizado.getNome());
                usuarioExistente.setEmail(usuarioAtualizado.getEmail());
                usuarioExistente.setSenha(usuarioAtualizado.getSenha());
                usuarioExistente.setAtivo(usuarioAtualizado.getAtivo());

                usuarioRepository.save(usuarioExistente);

                return "O usuário '" + usuarioExistente.getNome() + "' com ID " + id + " foi atualizado com sucesso!";
            } else {
            return "Usuário com ID " + id + " não encontrado.";
        }
    }

    @Transactional
    public String atualizarEnderecoPorId(Integer usuarioId, Integer enderecoId, Endereco enderecoAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuarioExistente != null) {
            // Verifica se o endereço pertence ao usuário
            Optional<Endereco> enderecoParaAtualizar = usuarioExistente.getEnderecos().stream()
                    .filter(endereco -> endereco.getId().equals(enderecoId))
                    .findFirst();

            if (enderecoParaAtualizar.isPresent()) {
                Endereco endereco = enderecoParaAtualizar.get();
                endereco.setRua(enderecoAtualizado.getRua());
                endereco.setCidade(enderecoAtualizado.getCidade());
                endereco.setEstado(enderecoAtualizado.getEstado());
                // Adicione outros campos de endereço conforme necessário

                usuarioRepository.save(usuarioExistente);

                return "Endereço com ID " + enderecoId + " do usuário com ID " + usuarioId + " foi atualizado com sucesso!";
            } else {
                return "Endereço com ID " + enderecoId + " não encontrado para o usuário com ID " + usuarioId + ".";
            }
        } else {
            return "Usuário com ID " + usuarioId + " não encontrado.";
        }
    }

    @Transactional
    public String apagarEnderecoPorId(Integer usuarioId, Integer enderecoId) {
        Usuario usuarioExistente = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuarioExistente != null) {
            Optional<Endereco> enderecoParaApagar = usuarioExistente.getEnderecos().stream()
                    .filter(endereco -> endereco.getId().equals(enderecoId))
                    .findFirst();

            if (enderecoParaApagar.isPresent()) {
                usuarioExistente.getEnderecos().remove(enderecoParaApagar.get());
                usuarioRepository.save(usuarioExistente);
                enderecoRepository.deleteById(enderecoId);

                return "Endereço com ID " + enderecoId + " do usuário com ID " + usuarioId + " foi apagado com sucesso!";
            } else {
                return "Endereço com ID " + enderecoId + " não encontrado para o usuário com ID " + usuarioId + ".";
            }
        } else {
            return "Usuário com ID " + usuarioId + " não encontrado.";
        }
    }

    @Transactional
    public String adicionarEndereco(Integer usuarioId, Endereco novoEndereco) {
        Usuario usuarioExistente = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuarioExistente != null) {
            novoEndereco.setUsuario(usuarioExistente);
            usuarioExistente.getEnderecos().add(novoEndereco);
            usuarioRepository.save(usuarioExistente);

            return "Novo endereço adicionado para o usuário com ID " + usuarioId + ".";
        } else {
            return "Usuário com ID " + usuarioId + " não encontrado.";
        }
    }

    @Transactional
    public String desativarUsuario(Integer id) {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElse(null);

        if (usuarioExistente != null) {
            // Verificar se o usuário tem empréstimos pendentes de devolução
            boolean temEmprestimosPendentes = emprestimoRepository.existsByUsuarioAndDataDevolucaoIsNull(usuarioExistente);

            if (temEmprestimosPendentes) {
                return "O usuário não pode ser desativado devido a empréstimos pendentes de devolução.";
            } else {
                usuarioExistente.setAtivo(false); // Desativa o usuário
                usuarioRepository.save(usuarioExistente);
                return "O usuário '" + usuarioExistente.getNome() + "' com ID " + id + " foi desativado com sucesso!";
            }
        } else {
            return "Usuário com ID " + id + " não encontrado.";
        }
    }
    public List<Usuario> buscarUsuariosComEmprestimosPendentes() {
        return emprestimoRepository.findUsuariosComEmprestimosPendentes();
    }

}