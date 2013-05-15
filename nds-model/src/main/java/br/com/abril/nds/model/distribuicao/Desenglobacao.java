package br.com.abril.nds.model.distribuicao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "DESENGLOBACAO")
@SequenceGenerator(name = "DESENGLOBACAO_SEQ", initialValue = 1, allocationSize = 1)
public class Desenglobacao implements Serializable {

    private static final long serialVersionUID = -670097663318412728L;

    @Id
    @GeneratedValue(generator = "DESENGLOBACAO_SEQ")
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "COTA_ID_DESENGLOBADA")
    private Cota cotaDesenglobada;

    @ManyToOne(optional = false)
    @JoinColumn(name = "TIPO_PDV_ID")
    private TipoPontoPDV tipoPDV;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USUARIO_ID")
    private Usuario responsavel;

    @ManyToOne
    @JoinColumn(name = "COTA_ID_ENGLOBADA")
    private Cota cotaEnglobada;

    @Column(name = "PORCENTAGEM_COTA_ENGLOBADA")
    private Float porcentagemCota;

    @Column (name = "DATA_ALTERACAO")
    private Date dataAlteracao;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Cota getCotaDesenglobada() {
	return cotaDesenglobada;
    }

    public void setCotaDesenglobada(Cota cotaDesenglobada) {
	this.cotaDesenglobada = cotaDesenglobada;
    }

    public TipoPontoPDV getTipoPDV() {
	return tipoPDV;
    }

    public void setTipoPDV(TipoPontoPDV tipoPDV) {
	this.tipoPDV = tipoPDV;
    }

    public Usuario getResponsavel() {
	return responsavel;
    }

    public void setResponsavel(Usuario responsavel) {
	this.responsavel = responsavel;
    }

    public Cota getCotaEnglobada() {
	return cotaEnglobada;
    }

    public void setCotaEnglobada(Cota cotaEnglobada) {
	this.cotaEnglobada = cotaEnglobada;
    }
    public Float getPorcentagemCota() {
        return porcentagemCota;
    }

    public void setPorcentagemCota(Float porcentagemCota) {
        this.porcentagemCota = porcentagemCota;
    }

    public Date getDataAlteracao() {
	return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
	this.dataAlteracao = dataAlteracao;
    }
}
