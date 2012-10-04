package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
	
	@Export(label = "Data")
	private Date dataConsolidado;
	
	@Export(label = "Vlr. Postergado", alignment = Alignment.RIGHT)
	private BigDecimal valorPostergado;
		
	@Export(label = "Consignado", alignment = Alignment.RIGHT)
	private BigDecimal consignado;
	
	@Export(label = "Encalhe", alignment = Alignment.RIGHT)
	private BigDecimal encalhe;
	
	@Export(label = "Venda Encalhe", alignment = Alignment.RIGHT)
	private BigDecimal vendaEncalhe;
	
	@Export(label = "Déb/Cred.", alignment = Alignment.RIGHT)
	private BigDecimal debitoCredito;
	
	@Export(label = "Encargos", alignment = Alignment.RIGHT)
	private BigDecimal encargos;
	
	@Export(label = "Pendente", alignment = Alignment.RIGHT)
	private BigDecimal pendente;
	
	@Export(label = "Total R$", alignment = Alignment.RIGHT)
	private BigDecimal total;
	
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
		this.valorPostergado = valorPostergado;
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
		this.consignado = consignado;
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
		this.encalhe = encalhe;
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
		this.vendaEncalhe = vendaEncalhe;
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
		this.debitoCredito = debitoCredito;
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
		this.encargos = encargos;
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
		this.pendente = pendente;
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
		this.total = total;
	}

}
