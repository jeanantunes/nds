package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Discover Technology
 *
 */
public class DetalheProdutoLancamentoVO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3924519267530987655L;

	private Long idProdutoEdicao;
	
	private String nomeProduto;
    
	private String codigoProduto;

	private BigDecimal precoCapa;
	
	private BigDecimal precoComDesconto;

	private String fornecedor;

	private Long codigoEditor;

	private String nomeEditor;
	
	private String chamadaCapa;

    private String possuiBrinde;

  	private Integer pacotePadrao;
	
	/**
	 * Construtor padrão.
	 */
	public DetalheProdutoLancamentoVO() {
		
	}

	/**
	 * Construtor
	 * @param precoCapa
	 * @param precoComDesconto
	 * @param nomeProduto
	 * @param fornecedor
	 * @param codigoEditor
	 * @param nomeEditor
	 * @param chamadaCapa
	 * @param possuiBrinde
	 * @param pacotePadrao
	 */
	 public DetalheProdutoLancamentoVO(Long idProdutoEdicao, String nomeProduto,
			String codigoProduto, BigDecimal precoCapa,
			BigDecimal precoComDesconto, String fornecedor, Long codigoEditor,
			String nomeEditor, String chamadaCapa, String possuiBrinde,
			Integer pacotePadrao) {
		super();
		this.idProdutoEdicao = idProdutoEdicao;
		this.nomeProduto = nomeProduto;
		this.codigoProduto = codigoProduto;
		this.precoCapa = precoCapa;
		this.precoComDesconto = precoComDesconto;
		this.fornecedor = fornecedor;
		this.codigoEditor = codigoEditor;
		this.nomeEditor = nomeEditor;
		this.chamadaCapa = chamadaCapa;
		this.possuiBrinde = possuiBrinde;
		this.pacotePadrao = pacotePadrao;
	}

	/**
	 * @return the idProdutoEdicao
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
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
	 * @return the precoComDesconto
	 */
	public BigDecimal getPrecoComDesconto() {
		return precoComDesconto;
	}

	/**
	 * @param precoComDesconto the precoComDesconto to set
	 */
	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
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
	 * @return the fornecedor
	 */
	public String getFornecedor() {
		return fornecedor;
	}

	/**
	 * @param fornecedor the fornecedor to set
	 */
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	/**
	 * @return the codigoEditor
	 */
	public Long getCodigoEditor() {
		return codigoEditor;
	}

	/**
	 * @param codigoEditor the codigoEditor to set
	 */
	public void setCodigoEditor(Long codigoEditor) {
		this.codigoEditor = codigoEditor;
	}

	/**
	 * @return the nomeEditor
	 */
	public String getNomeEditor() {
		return nomeEditor;
	}

	/**
	 * @param nomeEditor the nomeEditor to set
	 */
	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	/**
	 * @return the chamadaCapa
	 */
	public String getChamadaCapa() {
		return chamadaCapa;
	}

	/**
	 * @param chamadaCapa the chamadaCapa to set
	 */
	public void setChamadaCapa(String chamadaCapa) {
		this.chamadaCapa = chamadaCapa;
	}

	/**
	 * @return the possuiBrinde
	 */
	public String getPossuiBrinde() {
		return possuiBrinde;
	}

	/**
	 * @param possuiBrinde the possuiBrinde to set
	 */
	public void setPossuiBrinde(String possuiBrinde) {
		this.possuiBrinde = possuiBrinde;
	}

	/**
	 * @return the pacotePadrao
	 */
	public Integer getPacotePadrao() {
		return pacotePadrao;
	}

	/**
	 * @param pacotePadrao the pacotePadrao to set
	 */
	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
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

}
