package br.com.ada.biblioteca.usuario;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cep;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    @Enumerated(EnumType.STRING)
    private TipoEndereco tipo;


    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private Usuario usuario;

    // Construtores, getters e setters

    public TipoEndereco getTipo() {
        return tipo;
    }

    public Endereco() {
        // Construtor padrão
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setTipo(TipoEndereco tipo) {
        if (tipo == TipoEndereco.Principal) {
            // Se o tipo for principal, encontra o endereço anteriormente principal e o torna secundário
            if (usuario != null) {
                for (Endereco endereco : usuario.getEnderecos()) {
                    if (endereco.getTipo() == TipoEndereco.Principal) {
                        endereco.setTipo(TipoEndereco.Secundario);
                    }
                }
            }
        }
        this.tipo = tipo;
    }

}

