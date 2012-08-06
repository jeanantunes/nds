package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

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
	
	private Date dataLancamento;
	private Long idBox;
	private String codigoProduto;
	private String nomeProduto;
	private String codigoBox;
	private String nomeBox;
	private Long numeroEdicao;
	private BigDecimal precoCapa;
	private BigInteger qntReparte;
	private BigInteger qntDiferenca;
	private BigDecimal valorFaturado;
	private Long qntProduto;
	private String razaoSocial;
	
	public ExpedicaoDTO() {}
	
	public ExpedicaoDTO(String codigoProduto, String nomeProduto,
						Long numeroEdicao, BigDecimal precoCapa, BigInteger qntReparte,
						BigInteger qntDiferenca, BigDecimal valorFaturado, String razaoSocial) {
		
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
			Long numeroEdicao, BigDecimal precoCapa, BigInteger qntReparte,
			BigInteger qntDiferenca, BigDecimal valorFaturado) {

		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.precoCapa = precoCapa;
		this.qntReparte = qntReparte;
		this.qntDiferenca = qntDiferenca;
		this.valorFaturado = valorFaturado;

	}
	
	public ExpedicaoDTO(Date dataLancamento, Long idBox, String codigoBox, String nomeBox,
			BigDecimal precoCapa, BigInteger qntReparte,
			BigInteger qntDiferenca, BigDecimal valorFaturado) {

		
		this.dataLancamento = dataLancamento;
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
	 * Obtém qntReparte
	 *
	 * @return BigInteger
	 */
	public BigInteger getQntReparte() {
		return qntReparte;
	}

	/**
	 * Atribuí qntReparte
	 * @param qntReparte 
	 */
	public void setQntReparte(BigInteger qntReparte) {
		this.qntReparte = qntReparte;
	}

	/**
	 * Obtém qntDiferenca
	 *
	 * @return BigInteger
	 */
	public BigInteger getQntDiferenca() {
		return qntDiferenca;
	}

	/**
	 * Atribuí qntDiferenca
	 * @param qntDiferenca 
	 */
	public void setQntDiferenca(BigInteger qntDiferenca) {
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

	/**
	 * Obtém dataLancamento
	 *
	 * @return Date
	 */
	public Date getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * Atribuí dataLancamento
	 * @param dataLancamento 
	 */
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	
}
