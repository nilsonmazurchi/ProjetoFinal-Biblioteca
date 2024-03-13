package br.com.ada.biblioteca.emprestimo;
import br.com.ada.biblioteca.livros.Livro;
import br.com.ada.biblioteca.livros.LivroRepository;
import br.com.ada.biblioteca.usuario.Usuario;
import br.com.ada.biblioteca.usuario.UsuarioDuplicadoException;
import br.com.ada.biblioteca.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmprestimoService {
    private EmprestimoRepository repository;
    private LivroRepository livroRepository;
    private UsuarioRepository usuarioRepository;

    @Autowired
    public EmprestimoService(EmprestimoRepository repository, LivroRepository livroRepository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Emprestimo emprestarLivro(String nomeUsuario, String emailUsuario, String tituloLivro, String autorLivro) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByNomeAndEmail(nomeUsuario, emailUsuario);
        Optional<Livro> livroOptional = livroRepository.findByTituloAndAutor(tituloLivro, autorLivro);

        if (usuarioOptional.isPresent() && livroOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            Livro livro = livroOptional.get();

            if (!livro.getEmprestado()) {
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setUsuario(usuario);
                emprestimo.setLivro(livro);
                emprestimo.setDataEmprestimo(new Date());

                // Configurar a data de devolução prevista para 15 dias a partir da data de empréstimo
                Calendar cal = Calendar.getInstance();
                cal.setTime(emprestimo.getDataEmprestimo());
                cal.add(Calendar.DAY_OF_MONTH, 15);
                emprestimo.setDataDevolucaoPrevista(cal.getTime());

                // Atualiza o status do livro para emprestado
                livro.setEmprestado(true);

                repository.save(emprestimo);
                livroRepository.save(livro);

                return emprestimo;
            } else {
                throw new UsuarioDuplicadoException("O livro já está emprestado.");
            }
        } else {
            throw new UsuarioDuplicadoException("Usuário ou livro não encontrado.");
        }
    }

    @Transactional
    public Emprestimo devolverLivro(Integer emprestimoId) {
        Optional<Emprestimo> emprestimoOptional = repository.findById(emprestimoId);
        if (emprestimoOptional.isPresent()) {
            Emprestimo emprestimo = emprestimoOptional.get();
            emprestimo.setDataDevolucao(new Date());
            Date dataDevolucao = emprestimo.getDataDevolucao();

            // Calcular multa se a data de devolução for após a data prevista
            if (emprestimo.getDataDevolucao().after(emprestimo.getDataDevolucaoPrevista())) {
                long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataDevolucaoPrevista().toInstant(), dataDevolucao.toInstant());
                double multa = diasAtraso * 10.0; // Valor da multa: R$ 10,00 por dia de atraso

                // Atualiza o empréstimo com a data de devolução e o valor da multa
                emprestimo.setMulta(multa);
            }

            // Atualizar o status do livro para disponível
            Livro livro = emprestimo.getLivro();
            livro.setEmprestado(false);
            livroRepository.save(livro);

            return repository.save(emprestimo);
        } else {
            throw new RuntimeException("Empréstimo não encontrado.");
        }
    }
    @Transactional
    public Optional<Emprestimo> buscarEmprestimoPorId(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public Emprestimo realizarDevolucao(Integer id, Date dataDevolucao) {
        Emprestimo emprestimo = repository.findById(id)
                .orElseThrow(() -> new UsuarioDuplicadoException("Empréstimo não encontrado com o ID: " + id));

        // Verifica se a data de devolução é posterior à data prevista
        if (dataDevolucao.after(emprestimo.getDataDevolucaoPrevista())) {
            long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataDevolucaoPrevista().toInstant(), dataDevolucao.toInstant());
            double multa = diasAtraso * 10.0; // Valor da multa: R$ 10,00 por dia de atraso

            // Atualiza o empréstimo com a data de devolução e o valor da multa
            emprestimo.setDataDevolucao(dataDevolucao);
            emprestimo.setMulta(multa);
        } else {
            // Se não houver atraso, apenas atualiza a data de devolução
            emprestimo.setDataDevolucao(dataDevolucao);
        }

        // Atualiza o status do livro para disponível
        Livro livro = emprestimo.getLivro();
        livro.setEmprestado(false);
        livroRepository.save(livro);

        // Salva as alterações no empréstimo
        return repository.save(emprestimo);
    }

    public List<Emprestimo> buscarEmprestimosEmAberto() {
        return repository.findAllEmprestimosEmAberto();
    }

}
