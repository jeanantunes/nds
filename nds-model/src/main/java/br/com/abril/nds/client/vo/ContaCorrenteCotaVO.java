package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;

import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * Value Object para conta corrente da cota.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class ContaCorrenteCotaVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 410782527293217845L;
	
	private Long id;
	
	private Long cotaId;
	
	private Integer numeroCota;
	
	private Date dataRaiz;
	
	private String statusDivida;
	
	private Date dataPendente;
	
	private String nomeBox;
	
	private boolean cobrado;
	
	private boolean inadimplente;
	
	private BigInteger numeroAcumulo;
	
	@Export(label = "Data")
	private Date dataConsolidado;
		
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
	
	@Export(label = "Vlr. Pago R$", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA)
	private BigDecimal valorPago;
	
	@Export(label = "Saldo R$", alignment = Alignment.RIGHT, columnType = ColumnType.MOEDA)
	private BigDecimal saldo;
	
	private String formaCobranca;
	
	
	private boolean detalharDebitoCredito;
	
	/**
	 * Construtor padrão.
	 */
	public ContaCorrenteCotaVO() {
		
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
		this.valorPostergado = 
				valorPostergado != null ? 
						valorPostergado.setScale(4, RoundingMode.HALF_UP) : valorPostergado;
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
		this.consignado = 
				consignado != null ?
						consignado.setScale(4, RoundingMode.HALF_UP) : consignado;
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
		this.encalhe = 
				encalhe != null ? 
						encalhe.setScale(4, RoundingMode.HALF_UP) : encalhe;
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
		this.vendaEncalhe = 
				vendaEncalhe != null ? 
						vendaEncalhe.setScale(4, RoundingMode.HALF_UP) : vendaEncalhe;
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
		this.debitoCredito = 
				debitoCredito != null ? 
						debitoCredito.setScale(4, RoundingMode.HALF_UP) : debitoCredito;
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
		this.encargos = 
				encargos != null ? 
						encargos.setScale(4, RoundingMode.HALF_UP) : encargos;
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
		this.pendente = 
				pendente != null ? 
						pendente.setScale(2, RoundingMode.HALF_UP) : pendente;
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
		this.total = 
				total != null ? 
						total.setScale(2, RoundingMode.HALF_UP) : total;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getDataRaiz() {
		return dataRaiz;
	}

	public void setDataRaiz(Date dataRaiz) {
		this.dataRaiz = dataRaiz;
	}

	/**
	 * @return the dataPendente
	 */
	public Date getDataPendente() {
		return dataPendente;
	}

	/**
	 * @param dataPendente the dataPendente to set
	 */
	public void setDataPendente(Date dataPendente) {
		this.dataPendente = dataPendente;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = 
				valorPago != null ? 
						valorPago.setScale(4, RoundingMode.HALF_UP) : valorPago;
	}

	/**
	 * @return the numeroAcumulo
	 */
	public BigInteger getNumeroAcumulo() {
		return numeroAcumulo;
	}

	/**
	 * @param numeroAcumulo the numeroAcumulo to set
	 */
	public void setNumeroAcumulo(BigInteger numeroAcumulo) {
		this.numeroAcumulo = numeroAcumulo;
	}

	public boolean getCobrado() {
		return cobrado;
	}

	public void setCobrado(boolean cobrado) {
		this.cobrado = cobrado;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		
		if(saldo == null) {
			saldo = BigDecimal.ZERO;
		}
		
		this.saldo = saldo.setScale(4, RoundingMode.HALF_UP);
	}

	public BigDecimal getValorVendaDia() {
		return valorVendaDia;
	}

	public void setValorVendaDia(BigDecimal valorVendaDia) {
		this.valorVendaDia = 
				valorVendaDia != null ? 
						valorVendaDia.setScale(4, RoundingMode.HALF_UP) : valorVendaDia;
	}

	public String getNomeBox() {
		return nomeBox;
	}

	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}

	/**
	 * @return the inadimplente
	 */
	public boolean isInadimplente() {
		return inadimplente;
	}

	/**
	 * @param inadimplente the inadimplente to set
	 */
	public void setInadimplente(boolean inadimplente) {
		this.inadimplente = inadimplente;
	}

	public String getStatusDivida() {
		return statusDivida;
	}

	public void setStatusDivida(String statusDivida) {
		this.statusDivida = statusDivida;
	}

	public boolean isDetalharDebitoCredito() {
		return detalharDebitoCredito;
	}

	public void setDetalharDebitoCredito(boolean detalharDebitoCredito) {
		this.detalharDebitoCredito = detalharDebitoCredito;
	}

	public String getFormaCobranca() {
		return formaCobranca;
	}

	public void setFormaCobranca(String formaCobranca) {
		this.formaCobranca = formaCobranca;
	}
}