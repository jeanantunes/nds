package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Footer;
import br.com.abril.nds.util.export.FooterType;

@Exportable
public class CotaExemplaresDTO implements Serializable {
	
	private static final long serialVersionUID = -5054002962389418662L;
	
	public CotaExemplaresDTO() {
		
	}
	
	public CotaExemplaresDTO(Long idCota, Integer numeroCota, String nomeCota, 
			BigInteger exemplares, BigDecimal total, BigDecimal totalDesconto, 
			boolean inativo, SituacaoCadastro situacaoCadastro) {
		super();
		this.idCota = idCota;
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.exemplares = exemplares;
		this.total = total;
		this.totalDesconto = totalDesconto;
		this.inativo = inativo;
		this.situacaoCadastro = situacaoCadastro;
	}

	private Long idCota;
	
	@Export(label="Cota", alignment=Alignment.LEFT)
	private Integer numeroCota;
	
	@Export(label="Nome", alignment=Alignment.LEFT)
	private String nomeCota;
	
	@Export(label="Total Exemplares", alignment=Alignment.CENTER)
	@Footer(type = FooterType.SUM,columnType = ColumnType.INTEGER)
	private BigInteger exemplares;
	
	@Export(label="Total R$", alignment=Alignment.RIGHT, columnType = ColumnType.MOEDA_QUATRO_CASAS)
	@Footer(type = FooterType.SUM, columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private BigDecimal total;
	
	@Export(label="Total Desconto R$", alignment=Alignment.RIGHT, columnType = ColumnType.MOEDA_QUATRO_CASAS)
	@Footer(type = FooterType.SUM,columnType = ColumnType.MOEDA_QUATRO_CASAS)
	private BigDecimal totalDesconto;
	
	private SituacaoCadastro situacaoCadastro;
	
	private boolean inativo;
	
	private boolean contribuinteICMS;
	
	private boolean contribuinteICMSExigeNFe;
	
	private boolean exigeNotaFiscalEletronica;
	
	private boolean notaFiscalConsolidada;

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
	
	/**
	 * @return the exemplares
	 */
	public void setExemplares(BigInteger exemplares) {
		this.exemplares = exemplares != null ? exemplares.abs() : exemplares;
	}
	
	/**
	 * @param exemplares the exemplares to set
	 */
	public BigInteger getExemplares() {
		return exemplares;
	}

	public SituacaoCadastro getSituacaoCadastro() {
		return situacaoCadastro;
	}

	public void setSituacaoCadastro(SituacaoCadastro situacaoCadastro) {
		this.situacaoCadastro = situacaoCadastro;
	}

	public Boolean isContribuinteICMS() {
		return contribuinteICMS;
	}

	public void setContribuinteICMS(boolean contribuinteICMS) {
		this.contribuinteICMS = contribuinteICMS;
	}

	public boolean isContribuinteICMSExigeNFe() {
		return contribuinteICMSExigeNFe;
	}

	public void setContribuinteICMSExigeNFe(boolean contribuinteICMSExigeNFe) {
		this.contribuinteICMSExigeNFe = contribuinteICMSExigeNFe;
	}

	public Boolean isExigeNotaFiscalEletronica() {
		return exigeNotaFiscalEletronica;
	}

	public void setExigeNotaFiscalEletronica(Boolean exigeNotaFiscalEletronica) {
		this.exigeNotaFiscalEletronica = exigeNotaFiscalEletronica;
	}

	public Boolean getNotaFiscalConsolidada() {
		return notaFiscalConsolidada;
	}

	public void setNotaFiscalConsolidada(Boolean notaFiscalConsolidada) {
		this.notaFiscalConsolidada = notaFiscalConsolidada;
	}

	public void setExigeNotaFiscalEletronica(boolean exigeNotaFiscalEletronica) {
		this.exigeNotaFiscalEletronica = exigeNotaFiscalEletronica;
	}

	public void setNotaFiscalConsolidada(boolean notaFiscalConsolidada) {
		this.notaFiscalConsolidada = notaFiscalConsolidada;
	}
	
}