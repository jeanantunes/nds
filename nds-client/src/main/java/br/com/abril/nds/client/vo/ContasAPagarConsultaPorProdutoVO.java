package br.com.abril.nds.client.vo;

import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

public class ContasAPagarConsultaPorProdutoVO {
	
	private String produtoEdicaoId;
	private String rctl;
	private String codigo;
	private String produto;
	private String edicao;
	private String tipo;
	private String reparte;
	private String suplementacao;
	private String encalhe;
	private String venda;
	private String faltasSobras;
	private String debitosCreditos;
	private String saldoAPagar;
	private String fornecedor;
	private String dataLcto;
	private String dataFinal;
	
	public ContasAPagarConsultaPorProdutoVO() 
	{}
	
	public ContasAPagarConsultaPorProdutoVO(ContasApagarConsultaPorProdutoDTO dto) {
		
		this.setProdutoEdicaoId(dto.getProdutoEdicaoId().toString());
		this.setRctl(DateUtil.formatarDataPTBR(dto.getRctl()));
		this.setCodigo(dto.getCodigo());
		this.setProduto(dto.getProduto());
		this.setEdicao(dto.getEdicao().toString());
		this.setTipo(dto.isTipo() ? "P" : "N");
		this.setReparte(dto.getReparte().toString());
		this.setSuplementacao(dto.getSuplementacao().toString());
		this.setEncalhe(dto.getEncalhe().toString());
		this.setVenda(dto.getVenda().toString());
		this.setFaltasSobras(dto.getFaltasSobras().toString());
		this.setDebitosCreditos(dto.getDebitosCreditos().toString());
		this.setSaldoAPagar(CurrencyUtil.formatarValor(dto.getSaldoAPagar()));
		this.setFornecedor(dto.getFornecedor());
		this.setDataLcto(DateUtil.formatarDataPTBR(dto.getDataLcto()));
		this.setDataFinal(DateUtil.formatarDataPTBR(dto.getDataFinal()));
	}
	
	public String getProdutoEdicaoId() {
		return produtoEdicaoId;
	}
	public void setProdutoEdicaoId(String produtoEdicaoId) {
		this.produtoEdicaoId = produtoEdicaoId;
	}
	public String getRctl() {
		return rctl;
	}
	public void setRctl(String rctl) {
		this.rctl = rctl;
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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getReparte() {
		return reparte;
	}
	public void setReparte(String reparte) {
		this.reparte = reparte;
	}
	public String getSuplementacao() {
		return suplementacao;
	}
	public void setSuplementacao(String suplementacao) {
		this.suplementacao = suplementacao;
	}
	public String getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(String encalhe) {
		this.encalhe = encalhe;
	}
	public String getVenda() {
		return venda;
	}
	public void setVenda(String venda) {
		this.venda = venda;
	}
	public String getFaltasSobras() {
		return faltasSobras;
	}
	public void setFaltasSobras(String faltasSobras) {
		this.faltasSobras = faltasSobras;
	}
	public String getDebitosCreditos() {
		return debitosCreditos;
	}
	public void setDebitosCreditos(String debitosCreditos) {
		this.debitosCreditos = debitosCreditos;
	}
	public String getSaldoAPagar() {
		return saldoAPagar;
	}
	public void setSaldoAPagar(String saldoAPagar) {
		this.saldoAPagar = saldoAPagar;
	}
	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	public String getDataLcto() {
		return dataLcto;
	}
	public void setDataLcto(String dataLcto) {
		this.dataLcto = dataLcto;
	}
	public String getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}
}
