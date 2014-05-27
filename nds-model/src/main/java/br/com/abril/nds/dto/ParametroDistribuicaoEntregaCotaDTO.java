package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.ModalidadeCobranca;
import br.com.abril.nds.model.cadastro.PeriodicidadeCobranca;

public class ParametroDistribuicaoEntregaCotaDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long idCota; 
	private Date inicioCarencia; 
	private Date fimCarencia;
	private BaseCalculo baseCalculo; 
	private Integer diaCobranca;
	private DiaSemana diaSemana; 
	private ModalidadeCobranca modalidadeCobranca; 
	private BigDecimal percentualFaturamento;
	private PeriodicidadeCobranca periodicidade;
	private BigDecimal taxaFixa;
	private boolean porEntrega;
	private DescricaoTipoEntrega tipoEntrega;

	public ParametroDistribuicaoEntregaCotaDTO() {}

	
	public boolean isPorEntrega() {
		return porEntrega;
	}


	public void setPorEntrega(boolean porEntrega) {
		this.porEntrega = porEntrega;
	}


	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Date getInicioCarencia() {
		return inicioCarencia;
	}

	public void setInicioCarencia(Date inicioCarencia) {
		this.inicioCarencia = inicioCarencia;
	}

	public Date getFimCarencia() {
		return fimCarencia;
	}

	public void setFimCarencia(Date fimCarencia) {
		this.fimCarencia = fimCarencia;
	}

	public BaseCalculo getBaseCalculo() {
		return baseCalculo;
	}

	public void setBaseCalculo(BaseCalculo baseCalculo) {
		this.baseCalculo = baseCalculo;
	}

	public Integer getDiaCobranca() {
		return diaCobranca;
	}

	public void setDiaCobranca(Integer diaCobranca) {
		this.diaCobranca = diaCobranca;
	}

	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public ModalidadeCobranca getModalidadeCobranca() {
		return modalidadeCobranca;
	}

	public void setModalidadeCobranca(ModalidadeCobranca modalidadeCobranca) {
		this.modalidadeCobranca = modalidadeCobranca;
	}

	public BigDecimal getPercentualFaturamento() {
		return percentualFaturamento;
	}

	public void setPercentualFaturamento(BigDecimal percentualFaturamento) {
		this.percentualFaturamento = percentualFaturamento;
	}

	public PeriodicidadeCobranca getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(PeriodicidadeCobranca periodicidade) {
		this.periodicidade = periodicidade;
	}

	public BigDecimal getTaxaFixa() {
		return taxaFixa;
	}

	public void setTaxaFixa(BigDecimal taxaFixa) {
		this.taxaFixa = taxaFixa;
	}


	public DescricaoTipoEntrega getTipoEntrega() {
		return tipoEntrega;
	}


	public void setTipoEntrega(DescricaoTipoEntrega tipoEntrega) {
		this.tipoEntrega = tipoEntrega;
	}
}
