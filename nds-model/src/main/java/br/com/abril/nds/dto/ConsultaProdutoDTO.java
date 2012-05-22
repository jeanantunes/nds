package br.com.abril.nds.dto;

import java.io.Serializable;

public class ConsultaProdutoDTO implements Serializable {

	private static final long serialVersionUID = -8072285478313345444L;

	private Integer codigo;

	private String produtoDescricao;

	private String tipoProdutoDescricao;
	
	private String nomeEditor;
	
	private String tipoContratoFornecedor;
	
	private String situacao;
	
	private Integer peb;
	
	/**
	 * Construtor Padrão
	 */
	public ConsultaProdutoDTO() {
		
	}

	/**
	 * @return the codigo
	 */
	public Integer getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the produtoDescricao
	 */
	public String getProdutoDescricao() {
		return produtoDescricao;
	}

	/**
	 * @param produtoDescricao the produtoDescricao to set
	 */
	public void setProdutoDescricao(String produtoDescricao) {
		this.produtoDescricao = produtoDescricao;
	}

	/**
	 * @return the tipoProdutoDescricao
	 */
	public String getTipoProdutoDescricao() {
		return tipoProdutoDescricao;
	}

	/**
	 * @param tipoProdutoDescricao the tipoProdutoDescricao to set
	 */
	public void setTipoProdutoDescricao(String tipoProdutoDescricao) {
		this.tipoProdutoDescricao = tipoProdutoDescricao;
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
	 * @return the tipoContratoFornecedor
	 */
	public String getTipoContratoFornecedor() {
		return tipoContratoFornecedor;
	}

	/**
	 * @param tipoContratoFornecedor the tipoContratoFornecedor to set
	 */
	public void setTipoContratoFornecedor(String tipoContratoFornecedor) {
		this.tipoContratoFornecedor = tipoContratoFornecedor;
	}

	/**
	 * @return the situacao
	 */
	public String getSituacao() {
		return situacao;
	}

	/**
	 * @param situacao the situacao to set
	 */
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	/**
	 * @return the peb
	 */
	public Integer getPeb() {
		return peb;
	}

	/**
	 * @param peb the peb to set
	 */
	public void setPeb(Integer peb) {
		this.peb = peb;
	}
	
}
