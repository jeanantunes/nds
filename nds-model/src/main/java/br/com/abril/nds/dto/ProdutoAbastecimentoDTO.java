package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CurrencyUtil;

public class ProdutoAbastecimentoDTO implements Serializable{

	private static final long serialVersionUID = -2951289520494037916L;
	
	private String codigoProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	private Integer reparte;
	private String precoCapa;
	private String total;
	private String codigoRota;	
	private Integer codigoBox;
	private Long idProdutoEdicao;
	private Integer codigoCota;
	private String nomeCota;
	private Integer materialPromocional;
	private Integer sequenciaMatriz;
	private String totalBox;
	private Integer totalProduto;
	
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
	public void setReparte(BigInteger reparte) {
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

	public Integer getCodigoBox() {
		return codigoBox;
	}

	public void setCodigoBox(Integer codigoBox) {
		this.codigoBox = codigoBox;
	}

	public String getCodigoRota() {
		return codigoRota;
	}

	public void setCodigoRota(String codigoRota) {
		this.codigoRota = codigoRota;
	}

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}
	/**
	 * @return the materialPromocional
	 */
	public Integer getMaterialPromocional() {
		return materialPromocional;
	}

	/**
	 * @param materialPromocional the materialPromocional to set
	 */
	public void setMaterialPromocional(BigInteger materialPromocional) {
		this.materialPromocional = materialPromocional == null ? 0 : materialPromocional.intValue();
	}

	public Integer getSequenciaMatriz() {
		return sequenciaMatriz;
	}

	public void setSequenciaMatriz(Integer sequenciaMatriz) {
		this.sequenciaMatriz = sequenciaMatriz;
	}

	public Integer getCodigoCota() {
		return codigoCota;
	}

	public void setCodigoCota(Integer codigoCota) {
		this.codigoCota = codigoCota;
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
	 * @return the totalBox
	 */
	public String getTotalBox() {
		return totalBox;
	}

	/**
	 * @param totalBox the totalBox to set
	 */
	public void setTotalBox(BigDecimal totalBox) {
		this.totalBox = CurrencyUtil.formatarValor(totalBox);
	}

	/**
	 * @return the totalProduto
	 */
	public Integer getTotalProduto() {
		return totalProduto;
	}

	/**
	 * @param totalProduto the totalProduto to set
	 */
	public void setTotalProduto(Long totalProduto) {
		this.totalProduto = totalProduto.intValue();
	}
}
