package br.com.abril.nds.model.financeiro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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
@Table(name = "CONSOLIDADO_FINANCEIRO_COTA")
@SequenceGenerator(name="CONSOLIDADO_SEQ", initialValue = 1, allocationSize = 1)
public class ConsolidadoFinanceiroCota {
	
	@Id
	@GeneratedValue(generator = "CONSOLIDADO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_CONSOLIDADO", nullable = false)
	private Date dataConsolidado;
	
	@Column(name = "VALOR_POSTERGADO")
	private BigDecimal valorPostergado;
	
	@Column(name = "CONSIGNADO")
	private BigDecimal consignado;

	@Column(name = "ENCALHE")
	private BigDecimal encalhe;
	
	@Column(name = "VENDA_ENCALHE")
	private BigDecimal vendaEncalhe;
	
	@Column(name = "DEBITO_CREDITO")
	private BigDecimal debitoCredito;
	
	@Column(name = "ENCARGOS")
	private BigDecimal encargos;
	
	@Column(name = "PENDENTE")
	private BigDecimal pendente;
	
	@Column(name = "TOTAL", nullable = false)
	private BigDecimal total;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@OneToMany
	@JoinTable(name = "CONSOLIDADO_MVTO_FINANCEIRO_COTA", joinColumns = {@JoinColumn(name = "CONSOLIDADO_FINANCEIRO_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "MVTO_FINANCEIRO_COTA_ID")})
	private List<MovimentoFinanceiroCota> movimentos = new ArrayList<MovimentoFinanceiroCota>();

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
	
	public List<MovimentoFinanceiroCota> getMovimentos() {
		return movimentos;
	}
	
	public void setMovimentos(List<MovimentoFinanceiroCota> movimentos) {
		this.movimentos = movimentos;
	}
	
}