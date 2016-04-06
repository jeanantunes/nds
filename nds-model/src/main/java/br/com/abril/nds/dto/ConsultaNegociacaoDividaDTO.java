package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaNegociacaoDividaDTO implements Serializable {
	
	private static final long serialVersionUID = 8190481445740297657L;

	@Export(label = "Numero Cota")
	private Integer numeroCota;
	
	@Export(label = "Nome Cota")
	private String nomeCota;
	
	@Export(label = "Status Cota")
	private String statusCota;
	
	@Export(label = "Divida Inicial")
	private BigDecimal dividaInicial;
	
	@Export(label = "Encargos")
	private BigDecimal valorEncargos;
	
	@Export(label = "Valor Dívida R$")
	private BigDecimal dividaNegociada;
	
	@Export(label = "Data Negociação")
	private Date dataNegociacao;
	
	@Export(label = "Data Vencimento")
	private Date dataVencimento;
	
	@Export(label = "Parcela")
	private String parcela;
	
	@Export(label = "Valor Parcela")
	private BigDecimal valorParcela;
	
	@Export(label = "Situacao Parcela")
	private String situacaoParcela;
	
	private Long idCobranca;
	
	private Long idNegociacao;
	
	private Integer countParcelas;

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public String getStatusCota() {
		return statusCota;
	}

	public void setStatusCota(String statusCota) {
		this.statusCota = statusCota;
	}

	public BigDecimal getDividaInicial() {
		return dividaInicial;
	}

	public void setDividaInicial(BigDecimal dividaInicial) {
		this.dividaInicial = dividaInicial;
	}

	public BigDecimal getValorEncargos() {
		return valorEncargos;
	}

	public void setValorEncargos(BigDecimal valorEncargos) {
		this.valorEncargos = valorEncargos;
	}

	public BigDecimal getDividaNegociada() {
		return dividaNegociada;
	}

	public void setDividaNegociada(BigDecimal dividaNegociada) {
		this.dividaNegociada = dividaNegociada;
	}

	public Date getDataNegociacao() {
		return dataNegociacao;
	}

	public void setDataNegociacao(Date dataNegociacao) {
		this.dataNegociacao = dataNegociacao;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getParcela() {
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public BigDecimal getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(BigDecimal valorParcela) {
		this.valorParcela = valorParcela;
	}

	public String getSituacaoParcela() {
		return situacaoParcela;
	}

	public void setSituacaoParcela(String situacaoParcela) {
		this.situacaoParcela = situacaoParcela;
	}

	public Long getIdCobranca() {
		return idCobranca;
	}

	public void setIdCobranca(Long idCobranca) {
		this.idCobranca = idCobranca;
	}

	public Long getIdNegociacao() {
		return idNegociacao;
	}

	public void setIdNegociacao(Long idNegociacao) {
		this.idNegociacao = idNegociacao;
	}

	public Integer getCountParcelas() {
		return countParcelas;
	}

	public void setCountParcelas(Integer countParcelas) {
		this.countParcelas = countParcelas;
	}
	
}
