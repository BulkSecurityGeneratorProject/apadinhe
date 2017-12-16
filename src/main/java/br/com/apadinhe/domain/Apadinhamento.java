package br.com.apadinhe.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Apadinhamento.
 */
@Entity
@Table(name = "apadinhamento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "apadinhamento")
public class Apadinhamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "observacao")
    private String observacao;

    @OneToOne
    @JoinColumn(unique = true)
    private ProcessoApadinhamento processo;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "apadinhamento_padrinho",
               joinColumns = @JoinColumn(name="apadinhamentos_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="padrinhos_id", referencedColumnName="id"))
    private Set<User> padrinhos = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "apadinhamento_crianca",
               joinColumns = @JoinColumn(name="apadinhamentos_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="criancas_id", referencedColumnName="id"))
    private Set<Crianca> criancas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacao() {
        return observacao;
    }

    public Apadinhamento observacao(String observacao) {
        this.observacao = observacao;
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public ProcessoApadinhamento getProcesso() {
        return processo;
    }

    public Apadinhamento processo(ProcessoApadinhamento processoApadinhamento) {
        this.processo = processoApadinhamento;
        return this;
    }

    public void setProcesso(ProcessoApadinhamento processoApadinhamento) {
        this.processo = processoApadinhamento;
    }

    public Set<User> getPadrinhos() {
        return padrinhos;
    }

    public Apadinhamento padrinhos(Set<User> users) {
        this.padrinhos = users;
        return this;
    }

    public Apadinhamento addPadrinho(User user) {
        this.padrinhos.add(user);
        return this;
    }

    public Apadinhamento removePadrinho(User user) {
        this.padrinhos.remove(user);
        return this;
    }

    public void setPadrinhos(Set<User> users) {
        this.padrinhos = users;
    }

    public Set<Crianca> getCriancas() {
        return criancas;
    }

    public Apadinhamento criancas(Set<Crianca> criancas) {
        this.criancas = criancas;
        return this;
    }

    public Apadinhamento addCrianca(Crianca crianca) {
        this.criancas.add(crianca);
        return this;
    }

    public Apadinhamento removeCrianca(Crianca crianca) {
        this.criancas.remove(crianca);
        return this;
    }

    public void setCriancas(Set<Crianca> criancas) {
        this.criancas = criancas;
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
        Apadinhamento apadinhamento = (Apadinhamento) o;
        if (apadinhamento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), apadinhamento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Apadinhamento{" +
            "id=" + getId() +
            ", observacao='" + getObservacao() + "'" +
            "}";
    }
}
