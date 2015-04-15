package br.com.abril.nds.model.fiscal;

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
@Table(name = "PARAMETRO_EMISSAO_NOTA_FISCAL")
@SequenceGenerator(name="PARAMETRO_EMISSAO_NOTA_FISCAL_SEQ", initialValue = 1, allocationSize = 1)
public class ParametroEmissaoNotaFiscal {
	
	@Id
	@GeneratedValue(generator = "PARAMETRO_EMISSAO_NOTA_FISCAL_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "GRUPO_NOTA_FISCAL", nullable = false, unique = true)
	private GrupoNotaFiscal grupoNotaFiscal;
	
	@ManyToOne
	@JoinColumn(name = "CFOP_DENTRO_UF", nullable = false)
	private CFOP cfopDentroEstado;
	
	@ManyToOne
	@JoinColumn(name = "CFOP_FORA_UF")
	private CFOP cfopForaEstado;
	
	@Column(name = "SERIE_NF", nullable = false)
	private String serieNF;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public GrupoNotaFiscal getGrupoNotaFiscal() {
		return grupoNotaFiscal;
	}

	public void setGrupoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal) {
		this.grupoNotaFiscal = grupoNotaFiscal;
	}

	public CFOP getCfopDentroEstado() {
		return cfopDentroEstado;
	}

	public void setCfopDentroEstado(CFOP cfopDentroEstado) {
		this.cfopDentroEstado = cfopDentroEstado;
	}

	public CFOP getCfopForaEstado() {
		return cfopForaEstado;
	}

	public void setCfopForaEstado(CFOP cfopForaEstado) {
		this.cfopForaEstado = cfopForaEstado;
	}

	public String getSerieNF() {
		return serieNF;
	}

	public void setSerieNF(String serieNF) {
		this.serieNF = serieNF;
	}
	
	

}
