package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * DTO que representa os detalhes de diferenças. 
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class RateioDiferencaCotaDTO implements Serializable {

	private static final long serialVersionUID = -232160362921954478L;

	@Export(label="Data", alignment=Alignment.CENTER, exhibitionOrder=1)
	private Date data;

	@Export(label="Cota", alignment=Alignment.CENTER, exhibitionOrder=2)
	private Integer numeroCota;

	@Export(label="Nome", exhibitionOrder=3, widthPercent=30)
	private String nomeCota;

	@Export(label="Box", alignment=Alignment.CENTER, exhibitionOrder=4, widthPercent=4)
	private Integer codigoBox;

	@Export(label="Exemplares", alignment=Alignment.CENTER, exhibitionOrder=5)
	private BigInteger exemplares;

	@Export(label="Preço Desc. R$", alignment=Alignment.CENTER, exhibitionOrder=6)
	private String precoDescontoFormatado;
	
	@Export(label="Total Aprovadas R$", alignment=Alignment.CENTER, exhibitionOrder=7, widthPercent=10)
	private String totalAprovadasFormatado;
	
	@Export(label="Total Rejeitadas R$", alignment=Alignment.CENTER, exhibitionOrder=8, widthPercent=10)
	private String totalRejeitadasFormatado;
	
	@Export(label="Total R$", alignment=Alignment.CENTER, exhibitionOrder=9)
	private String valorTotalFormatado;

	private BigDecimal precoDesconto;

	private BigDecimal totalAprovadas;

	private BigDecimal totalRejeitadas;

	private BigDecimal valorTotal;

	/**
	 * @return the data
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
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
	 * @return the codigoBox
	 */
	public Integer getCodigoBox() {
		return codigoBox;
	}

	/**
	 * @param codigoBox the codigoBox to set
	 */
	public void setCodigoBox(Integer codigoBox) {
		this.codigoBox = codigoBox;
	}

	/**
	 * @return the exemplares
	 */
	public BigInteger getExemplares() {
		return exemplares;
	}

	/**
	 * @param exemplares the exemplares to set
	 */
	public void setExemplares(BigInteger exemplares) {
		this.exemplares = exemplares;
	}

	/**
	 * @return the precoDesconto
	 */
	public BigDecimal getPrecoDesconto() {
		return precoDesconto;
	}

	/**
	 * @param precoDesconto the precoDesconto to set
	 */
	public void setPrecoDesconto(BigDecimal precoDesconto) {

		this.precoDesconto = precoDesconto;
		
		this.precoDescontoFormatado = CurrencyUtil.formatarValor(precoDesconto);
	}

	/**
	 * @return the totalAprovadas
	 */
	public BigDecimal getTotalAprovadas() {
		return totalAprovadas;
	}

	/**
	 * @param totalAprovadas the totalAprovadas to set
	 */
	public void setTotalAprovadas(BigDecimal totalAprovadas) {
		
		this.totalAprovadas = totalAprovadas;
		
		this.totalAprovadasFormatado = CurrencyUtil.formatarValor(totalAprovadas);
	}

	/**
	 * @return the totalRejeitadas
	 */
	public BigDecimal getTotalRejeitadas() {
		return totalRejeitadas;
	}

	/**
	 * @param totalRejeitadas the totalRejeitadas to set
	 */
	public void setTotalRejeitadas(BigDecimal totalRejeitadas) {
		
		this.totalRejeitadas = totalRejeitadas;
		
		this.totalRejeitadasFormatado = CurrencyUtil.formatarValor(totalRejeitadas);
	}

	/**
	 * @return the valorTotal
	 */
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(BigDecimal valorTotal) {

		this.valorTotal = valorTotal;

		this.valorTotalFormatado = CurrencyUtil.formatarValor(valorTotal);
	}

	/**
	 * @return the totalAprovadasFormatado
	 */
	public String getTotalAprovadasFormatado() {
		return totalAprovadasFormatado;
	}

	/**
	 * @return the totalRejeitadasFormatado
	 */
	public String getTotalRejeitadasFormatado() {
		return totalRejeitadasFormatado;
	}

	/**
	 * @return the precoDescontoFormatado
	 */
	public String getPrecoDescontoFormatado() {
		return precoDescontoFormatado;
	}

	/**
	 * @return the valorTotalFormatado
	 */
	public String getValorTotalFormatado() {
		return valorTotalFormatado;
	}
}
