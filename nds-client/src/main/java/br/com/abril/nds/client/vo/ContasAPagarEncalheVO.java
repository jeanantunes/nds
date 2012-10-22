package br.com.abril.nds.client.vo;

import br.com.abril.nds.dto.ContasAPagarEncalheDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ContasAPagarEncalheVO {

	@Export(label="Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	public String codigo;
	
	@Export(label="Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	public String produto;
	
	@Export(label="Edição", alignment=Alignment.CENTER, exhibitionOrder = 3)
	public String edicao;
	
	@Export(label="Preço Capa R$", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	public String precoCapa;
	
	@Export(label="Preço c/ Desc. R$", alignment=Alignment.RIGHT, exhibitionOrder = 5)
	public String precoComDesconto;
	
	@Export(label="Encalhe", alignment=Alignment.CENTER, exhibitionOrder = 6)
	public String encalhe;
	
	@Export(label="Fornecedor", alignment=Alignment.LEFT, exhibitionOrder = 7)
	public String fornecedor;
	
	@Export(label="Valor R$", alignment=Alignment.RIGHT, exhibitionOrder = 8)
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
