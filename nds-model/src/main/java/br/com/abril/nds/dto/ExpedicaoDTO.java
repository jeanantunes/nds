package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * Objeto para transportar dados referentes a expedição de lançamentos de produtos.  
 * 
 * @author Discover Technology
 *
 */
public class ExpedicaoDTO implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idBox;
	private String codigoProduto;
	private String nomeProduto;
	private String codigoBox;
	private String nomeBox;
	private Long numeroEdicao;
	private BigDecimal precoCapa;
	private BigDecimal qntReparte;
	private BigDecimal qntDiferenca;
	private BigDecimal valorFaturado;
	private Long qntProduto;
	private String razaoSocial;
	
	public ExpedicaoDTO() {}
	
	public ExpedicaoDTO(String codigoProduto, String nomeProduto,
						Long numeroEdicao, BigDecimal precoCapa, BigDecimal qntReparte,
						BigDecimal qntDiferenca, BigDecimal valorFaturado, String razaoSocial) {
		
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.precoCapa = precoCapa;
		this.qntReparte = qntReparte;
		this.qntDiferenca = qntDiferenca;
		this.valorFaturado = valorFaturado;
		this.razaoSocial = razaoSocial;
	}
	
	public ExpedicaoDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao, BigDecimal precoCapa, BigDecimal qntReparte,
			BigDecimal qntDiferenca, BigDecimal valorFaturado) {

		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.precoCapa = precoCapa;
		this.qntReparte = qntReparte;
		this.qntDiferenca = qntDiferenca;
		this.valorFaturado = valorFaturado;

	}
	
	public ExpedicaoDTO(Long idBox,String codigoBox, String nomeBox,
			BigDecimal precoCapa, BigDecimal qntReparte,
			BigDecimal qntDiferenca, BigDecimal valorFaturado) {

		this.idBox = idBox;
		this.codigoBox = codigoBox;
		this.nomeBox = nomeBox;
		this.precoCapa = precoCapa;
		this.qntReparte = qntReparte;
		this.qntDiferenca = qntDiferenca;
		this.valorFaturado = valorFaturado;
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
	 * @return the precoCapa
	 */
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	/**
	 * @param precoCapa the precoCapa to set
	 */
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}

	/**
	 * @return the qntReparte
	 */
	public BigDecimal getQntReparte() {
		return qntReparte;
	}

	/**
	 * @param qntReparte the qntReparte to set
	 */
	public void setQntReparte(BigDecimal qntReparte) {
		this.qntReparte = qntReparte;
	}

	/**
	 * @return the qntDiferenca
	 */
	public BigDecimal getQntDiferenca() {
		return qntDiferenca;
	}

	/**
	 * @param qntDiferenca the qntDiferenca to set
	 */
	public void setQntDiferenca(BigDecimal qntDiferenca) {
		this.qntDiferenca = qntDiferenca;
	}

	/**
	 * @return the valorFaturado
	 */
	public BigDecimal getValorFaturado() {
		return valorFaturado;
	}

	/**
	 * @param valorFaturado the valorFaturado to set
	 */
	public void setValorFaturado(BigDecimal valorFaturado) {
		this.valorFaturado = valorFaturado;
	}

	/**
	 * @return the idBox
	 */
	public Long getIdBox() {
		return idBox;
	}

	/**
	 * @param idBox the idBox to set
	 */
	public void setIdBox(Long idBox) {
		this.idBox = idBox;
	}

	/**
	 * @return the codigoBox
	 */
	public String getCodigoBox() {
		return codigoBox;
	}

	/**
	 * @param codigoBox the codigoBox to set
	 */
	public void setCodigoBox(String codigoBox) {
		this.codigoBox = codigoBox;
	}

	/**
	 * @return the nomeBox
	 */
	public String getNomeBox() {
		return nomeBox;
	}

	/**
	 * @param nomeBox the nomeBox to set
	 */
	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}

	/**
	 * @return the qntProduto
	 */
	public Long getQntProduto() {
		return qntProduto;
	}

	/**
	 * @param qntProduto the qntProduto to set
	 */
	public void setQntProduto(Long qntProduto) {
		this.qntProduto = qntProduto;
	}

	/**
	 * Obtém razaoSocial
	 *
	 * @return String
	 */
	public String getRazaoSocial() {
		return razaoSocial;
	}

	/**
	 * Atribuí razaoSocial
	 * @param razaoSocial 
	 */
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	
}
