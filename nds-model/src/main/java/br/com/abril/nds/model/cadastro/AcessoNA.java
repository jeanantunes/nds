package br.com.abril.nds.model.cadastro;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="ACESSO_NA")
@SequenceGenerator(name="ACESSO_NA_SEQ", allocationSize = 1)
public class AcessoNA implements Serializable {

    @Id
    @GeneratedValue(generator = "ACESSO_NA_SEQ")
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="COTA_ID")
    private Cota cota;

    @Column(name = "USUARIO_INCLUSAO", length = 100)
    private String usuarioInclusao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_INCLUSAO")
    private Date dataInclusao;

    @Column(name = "USUARIO_ALTERACAO", length = 100)
    private String usuarioAlteracao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_ALTERACAO")
    private Date dataAlteracao;

    @Column(name = "ACESSO_ATIVO", nullable = false)
    private boolean acessoAtivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cota getCota() {
        return cota;
    }

    public void setCota(Cota cota) {
        this.cota = cota;
    }

    public String getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(String usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    public Date getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public String getUsuarioAlteracao() {
        return usuarioAlteracao;
    }

    public void setUsuarioAlteracao(String usuarioAlteracao) {
        this.usuarioAlteracao = usuarioAlteracao;
    }

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public boolean isAcessoAtivo() {
        return acessoAtivo;
    }

    public void setAcessoAtivo(boolean acessoAtivo) {
        this.acessoAtivo = acessoAtivo;
    }
}
