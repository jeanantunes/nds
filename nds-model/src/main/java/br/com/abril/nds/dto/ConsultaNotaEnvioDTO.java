package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaNotaEnvioDTO implements Serializable {

	private static final long serialVersionUID = -4249511518029888493L;

	
	private Long idCota;
	@Export(label="Cota", alignment=Alignment.LEFT)
	private Integer numeroCota;
	@Export(label="Nome", alignment=Alignment.LEFT)
	private String nomeCota;
	@Export(label="Total Exemplares", alignment=Alignment.CENTER)
	private Long exemplares;
	
	@Export(label="Total R$", alignment=Alignment.RIGHT)
	private BigDecimal total;
	
	@Export(label="Total Desconto R$", alignment=Alignment.RIGHT)
	private BigDecimal totalDesconto;
	
	private boolean notaImpressa;
	
	private boolean cotaSuspensa;

	/**
	 * @return the idCota
	 */
	public Long getIdCota() {
		return idCota;
	}

	/**
	 * @param idCota the idCota to set
	 */
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nomeCota
	 */
	public String getNomeCota() {
		return nomeCota;
	}

	/**
	 * @param nomeCota the nomeCota to set
	 */
	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	/**
	 * @return the exemplares
	 */
	public Long getExemplares() {
		return exemplares;
	}

	/**
	 * @param exemplares the exemplares to set
	 */
	public void setExemplares(Long exemplares) {
		this.exemplares = exemplares;
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

	/**
	 * @return the totalDesconto
	 */
	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}

	/**
	 * @param totalDesconto the totalDesconto to set
	 */
	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	/**
	 * @return the notaImpressa
	 */
	public boolean isNotaImpressa() {
		return notaImpressa;
	}

	/**
	 * @param notaImpressa the notaImpressa to set
	 */
	public void setNotaImpressa(boolean notaImpressa) {
		this.notaImpressa = notaImpressa;
	}

	public boolean isCotaSuspensa() {
		return cotaSuspensa;
	}

	public void setCotaSuspensa(boolean cotaSuspensa) {
		this.cotaSuspensa = cotaSuspensa;
	}
	
}
