package br.com.abril.nds.client.vo;

import br.com.abril.nds.dto.ContasAPagarFaltasSobrasDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ContasAPagarFaltasSobrasVO {

	@Export(label="Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label="Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String produto;
	
	@Export(label="Edição", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private String edicao;
	
	@Export(label="Preço de Capa R$", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private String precoCapa;
	
	@Export(label="Preço c/ Desc. R$", alignment=Alignment.RIGHT, exhibitionOrder = 5)
	private String precoComDesconto;
	
	@Export(label="Box", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private String box;
	
	
	@Export(label="Exemplares", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private String exemplares;
	
	@Export(label="Fornecedor", alignment=Alignment.LEFT, exhibitionOrder = 8)
	private String fornecedor;
	
	@Export(label="Valor R$", alignment=Alignment.RIGHT, exhibitionOrder = 9)
	private String valor;
	
	
	public ContasAPagarFaltasSobrasVO()
	{}
	
	
	public ContasAPagarFaltasSobrasVO(ContasAPagarFaltasSobrasDTO dto) {
		
		this.codigo = dto.getCodigo();
		this.produto = dto.getProduto();
		this.edicao = dto.getEdicao().toString();
		this.precoCapa = CurrencyUtil.formatarValor(dto.getPrecoCapa());
		this.precoComDesconto = CurrencyUtil.formatarValor(dto.getPrecoComDesconto());
		this.box = dto.getBox() == null ? null : dto.getBox().toString();
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
