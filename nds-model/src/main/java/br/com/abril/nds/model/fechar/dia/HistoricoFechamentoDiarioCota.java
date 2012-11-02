package br.com.abril.nds.model.fechar.dia;

import java.io.Serializable;

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

@Entity
@Table(name = "HISTORICO_FECHAMENTO_DIARIO_COTA")
@SequenceGenerator(name = "HISTORICO_FECHAMENTO_DIARIO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoFechamentoDiarioCota implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "HISTORICO_FECHAMENTO_DIARIO_COTA_SEQ")
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "HISTORICO_FECHAMENTO_DIARIO_CONSOLIDADO_COTA_ID")
	private HistoricoFechamentoDiarioConsolidadoCota historicoConsolidadoCota;
	
	@Column(name="NOME_COTA")
	private String nomeCota;
	
	@Column(name="NUMERO_COTA")
	private Integer numeroCota;
	
	@Column(name="TIPO_DETALHE_COTA")
	@Enumerated(EnumType.STRING)
	private TipoSituacaoCota tipoSituacaoCota;
	
	public enum TipoSituacaoCota{
		AUSENTE_REPARTE,AUSENTE_ENCALHE,NOVAS,INATIVAS;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HistoricoFechamentoDiarioConsolidadoCota getHistoricoConsolidadoCota() {
		return historicoConsolidadoCota;
	}

	public void setHistoricoConsolidadoCota(
			HistoricoFechamentoDiarioConsolidadoCota historicoConsolidadoCota) {
		this.historicoConsolidadoCota = historicoConsolidadoCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public TipoSituacaoCota getTipoSituacaoCota() {
		return tipoSituacaoCota;
	}

	public void setTipoSituacaoCota(TipoSituacaoCota tipoSituacaoCota) {
		this.tipoSituacaoCota = tipoSituacaoCota;
	}
}
