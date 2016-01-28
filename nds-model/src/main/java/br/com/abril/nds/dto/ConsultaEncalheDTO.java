package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ConsultaEncalheDTO {

	private Date dataDoRecolhimentoDistribuidor;
	
	private Date dataMovimento;
	
	private Long idProdutoEdicao;
	
	
	private Long idBox;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private String nomeCota;
	
	private String nomeBox;

	private Long numeroEdicao;
	
	private BigDecimal precoVenda;
	
	private BigDecimal precoComDesconto;
	
	private BigDecimal reparte;
	
	private BigDecimal encalhe;
	
	private String fornecedor;
	
	private BigDecimal valor;
	
	private BigDecimal valorComDesconto;
	
	private Long recolhimento;
	
	private Long idFornecedor;
	
	private Long idCota;
	
	boolean indPossuiObservacaoConferenciaEncalhe;
	
	public ConsultaEncalheDTO() {}

	/**
	 * Obtém dataDoRecolhimentoDistribuidor
	 *
	 * @return Date
	 */
	public Date getDataDoRecolhimentoDistribuidor() {
		return dataDoRecolhimentoDistribuidor;
	}

	/**
	 * Atribuí dataDoRecolhimentoDistribuidor
	 * @param dataDoRecolhimentoDistribuidor 
	 */
	public void setDataDoRecolhimentoDistribuidor(
			Date dataDoRecolhimentoDistribuidor) {
		this.dataDoRecolhimentoDistribuidor = dataDoRecolhimentoDistribuidor;
	}

	/**
	 * Obtém dataMovimento
	 *
	 * @return Date
	 */
	public Date getDataMovimento() {
		return dataMovimento;
	}

	/**
	 * Atribuí dataMovimento
	 * @param dataMovimento 
	 */
	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	/**
	 * Obtém idProdutoEdicao
	 *
	 * @return Long
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * Atribuí idProdutoEdicao
	 * @param idProdutoEdicao 
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	/**
	 * Obtém codigoProduto
	 *
	 * @return String
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * Atribuí codigoProduto
	 * @param codigoProduto 
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	/**
	 * Obtém nomeProduto
	 *
	 * @return String
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * Atribuí nomeProduto
	 * @param nomeProduto 
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	/**
	 * Obtém numeroEdicao
	 *
	 * @return Long
	 */
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * Atribuí numeroEdicao
	 * @param numeroEdicao 
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * Obtém precoVenda
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * Atribuí precoVenda
	 * @param precoVenda 
	 */
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	/**
	 * Obtém precoComDesconto
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}

	/**
	 * Atribuí precoComDesconto
	 * @param precoComDesconto 
	 */
	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}

	/**
	 * Obtém reparte
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getReparte() {
		return reparte;
	}

	/**
	 * Atribuí reparte
	 * @param reparte 
	 */
	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}

	/**
	 * Obtém encalhe
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getEncalhe() {
		return encalhe;
	}

	/**
	 * Atribuí encalhe
	 * @param encalhe 
	 */
	public void setEncalhe(BigDecimal encalhe) {
		this.encalhe = encalhe;
	}

	/**
	 * Obtém fornecedor
	 *
	 * @return String
	 */
	public String getFornecedor() {
		return fornecedor;
	}

	/**
	 * Atribuí fornecedor
	 * @param fornecedor 
	 */
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	/**
	 * Obtém recolhimento
	 *
	 * @return Long
	 */
	public Long getRecolhimento() {
		return recolhimento;
	}

	/**
	 * Atribuí recolhimento
	 * @param recolhimento 
	 */
	public void setRecolhimento(Long recolhimento) {
		this.recolhimento = recolhimento;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the valorComDesconto
	 */
	public BigDecimal getValorComDesconto() {
		return valorComDesconto;
	}

	/**
	 * @param valorComDesconto the valorComDesconto to set
	 */
	public void setValorComDesconto(BigDecimal valorComDesconto) {
		this.valorComDesconto = valorComDesconto;
	}

	/**
	 * @return the idFornecedor
	 */
	public Long getIdFornecedor() {
		return idFornecedor;
	}

	/**
	 * @param idFornecedor the idFornecedor to set
	 */
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	/**
	 * @return the idCota
	 */
	public Long getIdCota() {
		return idCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}
	

	public String getNomeBox() {
		return nomeBox;
	}

	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}
	

	public Long getIdBox() {
		return idBox;
	}

	public void setIdBox(Long idBox) {
		this.idBox = idBox;
	}

	
	/**
	 * @param idCota the idCota to set
	 */
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public void setIndPossuiObservacaoConferenciaEncalhe(
			boolean indPossuiObservacaoConferenciaEncalhe) {
		this.indPossuiObservacaoConferenciaEncalhe = indPossuiObservacaoConferenciaEncalhe;
	}

	public boolean getIndPossuiObservacaoConferenciaEncalhe() {
		return this.indPossuiObservacaoConferenciaEncalhe;
	}
	
}