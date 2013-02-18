package br.com.abril.nds.model.cadastro;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "COTA_BASE_COTA")
@SequenceGenerator(name="COTA_BASE_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class CotaBaseCota {
	
	@Id
	@GeneratedValue(generator = "COTA_BASE_COTA_SEQ")
	@Column(name = "ID")
	private Long id;	
	
	@ManyToOne  
    @JoinColumn(name = "COTA_BASE_ID")  
    private CotaBase cotaBase;
	
	
	@ManyToOne  
    @JoinColumn(name = "COTA_ID")  
    private Cota cota;
	
	@Column(name="ATIVO", nullable = false)
	private Boolean ativo;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DT_INICIO_VIGENCIA")
	private Date dtInicioVigencia;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DT_FIM_VIGENCIA")
	private Date dtFimVigencia;
	
	@Enumerated(EnumType.STRING)
	@Column(name="TIPO_ALTERACAO", nullable = false)
	private TipoAlteracao tipoAlteracao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CotaBase getCotaBase() {
		return cotaBase;
	}

	public void setCotaBase(CotaBase cotaBase) {
		this.cotaBase = cotaBase;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Date getDtInicioVigencia() {
		return dtInicioVigencia;
	}

	public void setDtInicioVigencia(Date dtInicioVigencia) {
		this.dtInicioVigencia = dtInicioVigencia;
	}

	public Date getDtFimVigencia() {
		return dtFimVigencia;
	}

	public void setDtFimVigencia(Date dtFimVigencia) {
		this.dtFimVigencia = dtFimVigencia;
	}

	public TipoAlteracao getTipoAlteracao() {
		return tipoAlteracao;
	}

	public void setTipoAlteracao(TipoAlteracao tipoAlteracao) {
		this.tipoAlteracao = tipoAlteracao;
	}
	
}
