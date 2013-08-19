package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CotaExemplaresDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5054002962389418662L;
	
	
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
	
	private boolean inativo;

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
	 * @return the inativo
	 */
	public boolean isInativo() {
		return inativo;
	}

	/**
	 * @param inativo the inativo to set
	 */
	public void setInativo(boolean inativo) {
		this.inativo = inativo;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
	}
}