package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;

public class ProdutoAbastecimentoDTO implements Serializable{

	private static final long serialVersionUID = -2951289520494037916L;
	
	private String codigoProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	private Integer reparte;
	private String precoCapa;
	private String total;
	
	private String codigoBox;
	
	public ProdutoAbastecimentoDTO() {
		
	}
	
	public ProdutoAbastecimentoDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao, Integer reparte, String precoCapa, String total) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.reparte = reparte;
		this.precoCapa = precoCapa;
		this.total = total;
	}

	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}
	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	/**
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}
	/**
	 * @param nomeProduto the nomeProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	/**
	 * @return the numeroEdicao
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	/**
	 * @return the reparte
	 */
	public Integer getReparte() {
		return reparte;
	}
	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(BigDecimal reparte) {
		if(reparte!=null)
			this.reparte = reparte.intValue();
	}
	/**
	 * @return the precoCapa
	 */
	public String getPrecoCapa() {
		return precoCapa;
	}
	/**
	 * @param precoCapa the precoCapa to set
	 */
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = CurrencyUtil.formatarValor(precoCapa);
	}
	/**
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = CurrencyUtil.formatarValor(total);
	}

	public String getCodigoBox() {
		return codigoBox;
	}

	public void setCodigoBox(String codigoBox) {
		this.codigoBox = codigoBox;
	}
	
	
}
