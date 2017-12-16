package br.com.apadinhe.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Visita.
 */
@Entity
@Table(name = "visita")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "visita")
public class Visita implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "data", nullable = false)
    private ZonedDateTime data;

    @ManyToOne(optional = false)
    @NotNull
    private Crianca crianca;

    @ManyToOne(optional = false)
    @NotNull
    private User padrinho;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public Visita data(ZonedDateTime data) {
        this.data = data;
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public Crianca getCrianca() {
        return crianca;
    }

    public Visita crianca(Crianca crianca) {
        this.crianca = crianca;
        return this;
    }

    public void setCrianca(Crianca crianca) {
        this.crianca = crianca;
    }

    public User getPadrinho() {
        return padrinho;
    }

    public Visita padrinho(User user) {
        this.padrinho = user;
        return this;
    }

    public void setPadrinho(User user) {
        this.padrinho = user;
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
        Visita visita = (Visita) o;
        if (visita.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), visita.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Visita{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            "}";
    }
}
