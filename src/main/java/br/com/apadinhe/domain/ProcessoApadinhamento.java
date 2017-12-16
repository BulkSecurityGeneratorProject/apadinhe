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

import br.com.apadinhe.domain.enumeration.ProcessoApadinhamentoSituacao;

/**
 * A ProcessoApadinhamento.
 */
@Entity
@Table(name = "processo_apadinhamento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "processoapadinhamento")
public class ProcessoApadinhamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private ProcessoApadinhamentoSituacao situacao;

    @NotNull
    @Column(name = "texto", nullable = false)
    private String texto;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "processo_apadinhamento_padrinho",
               joinColumns = @JoinColumn(name="processo_apadinhamentos_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="padrinhos_id", referencedColumnName="id"))
    private Set<User> padrinhos = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "processo_apadinhamento_crianca",
               joinColumns = @JoinColumn(name="processo_apadinhamentos_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="criancas_id", referencedColumnName="id"))
    private Set<Crianca> criancas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessoApadinhamentoSituacao getSituacao() {
        return situacao;
    }

    public ProcessoApadinhamento situacao(ProcessoApadinhamentoSituacao situacao) {
        this.situacao = situacao;
        return this;
    }

    public void setSituacao(ProcessoApadinhamentoSituacao situacao) {
        this.situacao = situacao;
    }

    public String getTexto() {
        return texto;
    }

    public ProcessoApadinhamento texto(String texto) {
        this.texto = texto;
        return this;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Set<User> getPadrinhos() {
        return padrinhos;
    }

    public ProcessoApadinhamento padrinhos(Set<User> users) {
        this.padrinhos = users;
        return this;
    }

    public ProcessoApadinhamento addPadrinho(User user) {
        this.padrinhos.add(user);
        return this;
    }

    public ProcessoApadinhamento removePadrinho(User user) {
        this.padrinhos.remove(user);
        return this;
    }

    public void setPadrinhos(Set<User> users) {
        this.padrinhos = users;
    }

    public Set<Crianca> getCriancas() {
        return criancas;
    }

    public ProcessoApadinhamento criancas(Set<Crianca> criancas) {
        this.criancas = criancas;
        return this;
    }

    public ProcessoApadinhamento addCrianca(Crianca crianca) {
        this.criancas.add(crianca);
        return this;
    }

    public ProcessoApadinhamento removeCrianca(Crianca crianca) {
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
        ProcessoApadinhamento processoApadinhamento = (ProcessoApadinhamento) o;
        if (processoApadinhamento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), processoApadinhamento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProcessoApadinhamento{" +
            "id=" + getId() +
            ", situacao='" + getSituacao() + "'" +
            ", texto='" + getTexto() + "'" +
            "}";
    }
}
