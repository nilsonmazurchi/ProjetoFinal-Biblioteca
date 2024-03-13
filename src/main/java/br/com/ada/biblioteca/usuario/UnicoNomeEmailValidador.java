package br.com.ada.biblioteca.usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

public class UnicoNomeEmailValidador implements ConstraintValidator<UnicoNomeEmail, Usuario> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(UnicoNomeEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(Usuario usuario, ConstraintValidatorContext context) {
        if (usuario == null) {
            return true;
        }

        // Verificar se a combinação de nome e email já existe usando uma consulta JPQL
        Long count = entityManager.createQuery(
                        "SELECT COUNT(u) FROM Usuario u WHERE u.nome = :nome AND u.email = :email" +
                                " AND u.id <> :id", Long.class)
                .setParameter("nome", usuario.getNome())
                .setParameter("email", usuario.getEmail())
                .setParameter("id", usuario.getId())
                .getSingleResult();

        if (count > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Combinação de nome e email já existe no cadastro. Use um nome ou email diferente.")
                    .addPropertyNode("nome")  // ou addPropertyNode("email") dependendo do caso
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
