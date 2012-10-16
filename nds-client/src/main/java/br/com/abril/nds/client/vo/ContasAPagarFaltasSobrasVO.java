package br.com.abril.nds.client.vo;

import br.com.abril.nds.dto.ContasAPagarFaltasSobrasDTO;
import br.com.abril.nds.util.CurrencyUtil;

public class ContasAPagarFaltasSobrasVO {

	private String codigo;
	private String produto;
	private String edicao;
	private String precoCapa;
	private String precoComDesconto;
	private String box;
	private String exemplares;
	private String fornecedor;
	private String valor;
	
	
	public ContasAPagarFaltasSobrasVO()
	{}
	
	
	public ContasAPagarFaltasSobrasVO(ContasAPagarFaltasSobrasDTO dto) {
		
		this.codigo = dto.getCodigo();
		this.produto = dto.getProduto();
		this.edicao = dto.getEdicao().toString();
		this.precoCapa = CurrencyUtil.formatarValor(dto.getPrecoCapa());
		this.precoComDesconto = CurrencyUtil.formatarValor(dto.getPrecoComDesconto());
		this.box = dto.getBox().toString();
		this.exemplares = dto.getExemplares().toString();
		this.fornecedor = dto.getFornecedor();
		this.valor = CurrencyUtil.formatarValor(dto.getValor());
	}
	
	
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
	public String getEdicao() {
		return edicao;
	}
	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}
	public String getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(String precoCapa) {
		this.precoCapa = precoCapa;
	}
	public String getPrecoComDesconto() {
		return precoComDesconto;
	}
	public void setPrecoComDesconto(String precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}
	public String getBox() {
		return box;
	}
	public void setBox(String box) {
		this.box = box;
	}
	public String getExemplares() {
		return exemplares;
	}
	public void setExemplares(String exemplares) {
		this.exemplares = exemplares;
	}
	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
}
