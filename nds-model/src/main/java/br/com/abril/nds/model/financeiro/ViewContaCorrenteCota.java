package br.com.abril.nds.model.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Cota;

/**
 * @author Luis Gustavo Maschietto
 * @version 1.0
 * @created 16-mar-2012 14:00:00
 */
@Entity
@Table(name = "VIEW_CONTA_CORRENTE_COTA")
public class ViewContaCorrenteCota  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8354662679886829377L;

	@Id	
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "COTA_ID", nullable = false)
	private Long cotaId;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_CONSOLIDADO", nullable = false)
	private Date dataConsolidado;
	
	@Column(name = "NUMERO_COTA", nullable = false)
	private Integer numeroCota;
	
	@Column(name = "VALOR_POSTERGADO", nullable = true)
	private BigDecimal valorPostergado;

	@Column(name = "NUMERO_ATRASADOS", nullable = false)
	private BigDecimal numeroAtrasados;
	
	@Column(name = "CONSIGNADO", nullable = true)
	private BigDecimal consignado;
	
	@Column(name = "ENCALHE", nullable = true)
	private BigDecimal encalhe;
	
	@Column(name = "VENDA_ENCALHE", nullable = true)
	private BigDecimal vendaEncalhe;
	
	@Column(name = "DEBITO_CREDITO", nullable = true)
	private BigDecimal debitoCredito;
	
	@Column(name = "ENCARGOS", nullable = true)
	private BigDecimal encargos;
	
	@Column(name = "PENDENTE", nullable = true)
	private BigDecimal pendente;
	
	@Column(name = "TOTAL", nullable = true)
	private BigDecimal total;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DT_RAIZ_CONSOLIDADO")
	private Date dataRaizConsolidado;
	
	@ManyToOne
	private Cota cota;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataConsolidado() {
		return dataConsolidado;
	}

	public void setDataConsolidado(Date dataConsolidado) {
		this.dataConsolidado = dataConsolidado;
	}

	public BigDecimal getValorPostergado() {
		return valorPostergado;
	}

	public void setValorPostergado(BigDecimal valorPostergado) {
		this.valorPostergado = valorPostergado;
	}

	public BigDecimal getNumeroAtrasados() {
		return numeroAtrasados;
	}

	public void setNumeroAtrasados(BigDecimal numeroAtrasados) {
		this.numeroAtrasados = numeroAtrasados;
	}

	public BigDecimal getConsignado() {
		return consignado;
	}

	public void setConsignado(BigDecimal consignado) {
		this.consignado = consignado;
	}

	public BigDecimal getEncalhe() {
		return encalhe;
	}

	public void setEncalhe(BigDecimal encalhe) {
		this.encalhe = encalhe;
	}

	public BigDecimal getVendaEncalhe() {
		return vendaEncalhe;
	}

	public void setVendaEncalhe(BigDecimal vendaEncalhe) {
		this.vendaEncalhe = vendaEncalhe;
	}

	public BigDecimal getDebitoCredito() {
		return debitoCredito;
	}

	public void setDebitoCredito(BigDecimal debitoCredito) {
		this.debitoCredito = debitoCredito;
	}

	public BigDecimal getEncargos() {
		return encargos;
	}

	public void setEncargos(BigDecimal encargos) {
		this.encargos = encargos;
	}

	public BigDecimal getPendente() {
		return pendente;
	}

	public void setPendente(BigDecimal pendente) {
		this.pendente = pendente;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Long getCotaId() {
		return cotaId;
	}

	public void setCotaId(Long cotaId) {
		this.cotaId = cotaId;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the dataRaizConsolidado
	 */
	public Date getDataRaizConsolidado() {
		return dataRaizConsolidado;
	}

	/**
	 * @param dataRaizConsolidado the dataRaizConsolidado to set
	 */
	public void setDataRaizConsolidado(Date dataRaizConsolidado) {
		this.dataRaizConsolidado = dataRaizConsolidado;
	}

	
}