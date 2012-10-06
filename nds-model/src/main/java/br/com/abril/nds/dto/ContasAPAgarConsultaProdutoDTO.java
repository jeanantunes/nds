package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class ContasAPAgarConsultaProdutoDTO implements Serializable{

	private static final long serialVersionUID = 6307198166397971383L;
	
	private String codigo;
	private String produto;
	private BigInteger edicao;
	private BigDecimal precoCapa;
	private String fornecedor;
	private String editor;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public BigInteger getEdicao() {
		return edicao;
	}
	public void setEdicao(BigInteger edicao) {
		this.edicao = edicao;
	}
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}
	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	
	
	
	

}
