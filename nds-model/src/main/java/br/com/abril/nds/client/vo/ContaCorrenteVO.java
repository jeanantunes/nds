package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * Value Object para conta corrente .
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class ContaCorrenteVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 410782527293217845L;
	
	
	@Export(label = "Data")
	private Date dataConsolidado;
	
	@Export(label = "Cota")
	private Integer numeroCota;
		
	@Export(label = "Consignado", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private BigDecimal consignado;
	
	@Export(label = "Encalhe", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private BigDecimal encalhe;
	
	@Export(label = "Valor Venda Dia", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private BigDecimal valorVendaDia;
	
	@Export(label = "Vlr. Postergado R$", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private BigDecimal valorPostergado;
	
	@Export(label = "Venda Encalhe", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private BigDecimal vendaEncalhe;
	
	@Export(label = "Déb/Cred.", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private BigDecimal debitoCredito;
	
	@Export(label = "Encargos", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private BigDecimal encargos;
	
	@Export(label = "Pendente", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private BigDecimal pendente;
	
	@Export(label = "Total R$", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA)
	private BigDecimal total;
	
	@Export(label = "Situação")
	private String situacaoCadastro;
	
	@Export(label = "Legenda")
	private String legenda;
	
	/**
	 * Construtor padrão.
	 */
	public ContaCorrenteVO() {
		
	}

	/**
	 * @return the dataConsolidado
	 */
	public Date getDataConsolidado() {
		return dataConsolidado;
	}

	/**
	 * @param dataConsolidado the dataConsolidado to set
	 */
	public void setDataConsolidado(Date dataConsolidado) {
		this.dataConsolidado = dataConsolidado;
	}

	/**
	 * @return the valorPostergado
	 */
	public BigDecimal getValorPostergado() {
		return valorPostergado;
	}

	/**
	 * @param valorPostergado the valorPostergado to set
	 */
	public void setValorPostergado(BigDecimal valorPostergado) {
		this.valorPostergado =  valorPostergado != null ? valorPostergado.setScale(4, RoundingMode.HALF_UP) : valorPostergado;
	}

		/**
	 * @return the consignado
	 */
	public BigDecimal getConsignado() {
		return consignado;
	}

	/**
	 * @param consignado the consignado to set
	 */
	public void setConsignado(BigDecimal consignado) {
		this.consignado =  consignado != null ? consignado.setScale(4, RoundingMode.HALF_UP) : consignado;
	}

	/**
	 * @return the encalhe
	 */
	public BigDecimal getEncalhe() {
		return encalhe;
	}

	/**
	 * @param encalhe the encalhe to set
	 */
	public void setEncalhe(BigDecimal encalhe) {
		this.encalhe = encalhe != null ? encalhe.setScale(4, RoundingMode.HALF_UP) : encalhe;
	}

	/**
	 * @return the vendaEncalhe
	 */
	public BigDecimal getVendaEncalhe() {
		return vendaEncalhe;
	}

	/**
	 * @param vendaEncalhe the vendaEncalhe to set
	 */
	public void setVendaEncalhe(BigDecimal vendaEncalhe) {
		this.vendaEncalhe = vendaEncalhe != null ? vendaEncalhe.setScale(4, RoundingMode.HALF_UP) : vendaEncalhe;
	}

	/**
	 * @return the debitoCredito
	 */
	public BigDecimal getDebitoCredito() {
		return debitoCredito;
	}

	/**
	 * @param debitoCredito the debitoCredito to set
	 */
	public void setDebitoCredito(BigDecimal debitoCredito) {
		this.debitoCredito = debitoCredito != null ? debitoCredito.setScale(4, RoundingMode.HALF_UP) : debitoCredito;
	}

	/**
	 * @return the encargos
	 */
	public BigDecimal getEncargos() {
		return encargos;
	}

	/**
	 * @param encargos the encargos to set
	 */
	public void setEncargos(BigDecimal encargos) {
		this.encargos = encargos != null ? encargos.setScale(4, RoundingMode.HALF_UP) : encargos;
	}

	/**
	 * @return the pendente
	 */
	public BigDecimal getPendente() {
		return pendente;
	}

	/**
	 * @param pendente the pendente to set
	 */
	public void setPendente(BigDecimal pendente) {
		this.pendente = pendente != null ? pendente.setScale(2, RoundingMode.HALF_UP) : pendente;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total != null ? total.setScale(2, RoundingMode.HALF_UP) : total;
	}
	
	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	
	public BigDecimal getValorVendaDia() {
		return valorVendaDia;
	}

	public void setValorVendaDia(BigDecimal valorVendaDia) {
		this.valorVendaDia = valorVendaDia != null ? valorVendaDia.setScale(4, RoundingMode.HALF_UP) : valorVendaDia;
	}

	public String getSituacaoCadastro() {
		return situacaoCadastro;
	}

	public void setSituacaoCadastro(String situacaoCadastro) {
		this.situacaoCadastro = situacaoCadastro;
	}

	public String getLegenda() {
		return legenda;
	}

	public void setLegenda(String legenda) {
		this.legenda = legenda;
	}
}