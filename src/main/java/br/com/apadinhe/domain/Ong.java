package br.com.apadinhe.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import br.com.apadinhe.domain.enumeration.Banco;

/**
 * A Ong.
 */
@Entity
@Table(name = "ong")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ong")
public class Ong implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "requisitos_apadrinhamento")
    private String requisitosApadrinhamento;

    @NotNull
    @Column(name = "conta", nullable = false)
    private String conta;

    @NotNull
    @Column(name = "agencia", nullable = false)
    private String agencia;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "banco", nullable = false)
    private Banco banco;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "telefone", nullable = false)
    private String telefone;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Ong nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRequisitosApadrinhamento() {
        return requisitosApadrinhamento;
    }

    public Ong requisitosApadrinhamento(String requisitosApadrinhamento) {
        this.requisitosApadrinhamento = requisitosApadrinhamento;
        return this;
    }

    public void setRequisitosApadrinhamento(String requisitosApadrinhamento) {
        this.requisitosApadrinhamento = requisitosApadrinhamento;
    }

    public String getConta() {
        return conta;
    }

    public Ong conta(String conta) {
        this.conta = conta;
        return this;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getAgencia() {
        return agencia;
    }

    public Ong agencia(String agencia) {
        this.agencia = agencia;
        return this;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public Banco getBanco() {
        return banco;
    }

    public Ong banco(Banco banco) {
        this.banco = banco;
        return this;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getEmail() {
        return email;
    }

    public Ong email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public Ong telefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ong ong = (Ong) o;
        if (ong.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ong.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ong{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", requisitosApadrinhamento='" + getRequisitosApadrinhamento() + "'" +
            ", conta='" + getConta() + "'" +
            ", agencia='" + getAgencia() + "'" +
            ", banco='" + getBanco() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefone='" + getTelefone() + "'" +
            "}";
    }
}
