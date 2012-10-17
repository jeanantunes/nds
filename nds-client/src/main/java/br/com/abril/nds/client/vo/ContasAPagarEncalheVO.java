package br.com.abril.nds.client.vo;

import br.com.abril.nds.dto.ContasAPagarEncalheDTO;
import br.com.abril.nds.util.CurrencyUtil;

public class ContasAPagarEncalheVO {

	public String codigo;
	public String produto;
	public String edicao;
	public String precoCapa;
	public String precoComDesconto;
	public String encalhe;
	public String fornecedor;
	public String valor;
	
	
	public ContasAPagarEncalheVO()
	{}
	
	
	public ContasAPagarEncalheVO(ContasAPagarEncalheDTO dto) {
	
		this.codigo = dto.getCodigo();
		this.produto = dto.getProduto();
		this.edicao = dto.getEdicao().toString();
		this.precoCapa = CurrencyUtil.formatarValor(dto.getPrecoCapa());
		this.precoComDesconto = CurrencyUtil.formatarValor(dto.getPrecoComDesconto());
		this.encalhe = dto.getEncalhe().toString();
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
	public String getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(String encalhe) {
		this.encalhe = encalhe;
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
