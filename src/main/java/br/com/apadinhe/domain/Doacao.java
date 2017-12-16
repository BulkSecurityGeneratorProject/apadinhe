package br.com.apadinhe.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Doacao.
 */
@Entity
@Table(name = "doacao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "doacao")
public class Doacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comprovante")
    private String comprovante;

    @NotNull
    @Column(name = "valor", nullable = false)
    private Double valor;

    @ManyToOne(optional = false)
    @NotNull
    private Ong ong;

    @ManyToOne(optional = false)
    @NotNull
    private User doador;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComprovante() {
        return comprovante;
    }

    public Doacao comprovante(String comprovante) {
        this.comprovante = comprovante;
        return this;
    }

    public void setComprovante(String comprovante) {
        this.comprovante = comprovante;
    }

    public Double getValor() {
        return valor;
    }

    public Doacao valor(Double valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Ong getOng() {
        return ong;
    }

    public Doacao ong(Ong ong) {
        this.ong = ong;
        return this;
    }

    public void setOng(Ong ong) {
        this.ong = ong;
    }

    public User getDoador() {
        return doador;
    }

    public Doacao doador(User user) {
        this.doador = user;
        return this;
    }

    public void setDoador(User user) {
        this.doador = user;
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
        Doacao doacao = (Doacao) o;
        if (doacao.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), doacao.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Doacao{" +
            "id=" + getId() +
            ", comprovante='" + getComprovante() + "'" +
            ", valor='" + getValor() + "'" +
            "}";
    }
}
