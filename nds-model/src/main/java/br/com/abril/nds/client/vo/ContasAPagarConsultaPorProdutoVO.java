package br.com.abril.nds.client.vo;

import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ContasAPagarConsultaPorProdutoVO {
	
	
	private String produtoEdicaoId;
	
	@Export(label = "Rctl", alignment=Alignment.CENTER, exhibitionOrder = 1)
	private String rctl;
	
	@Export(label = "Código", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String codigo;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String produto;
	
	@Export(label = "Edição", alignment=Alignment.CENTER, exhibitionOrder = 4)
	private String edicao;
	
	@Export(label = "Tipo", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private String tipo;
	
	@Export(label = "Reparte", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private String reparte;
	
	@Export(label = "Estoque", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private String estoque;
	
	@Export(label = "Encalhe", alignment=Alignment.CENTER, exhibitionOrder = 8)
	private String encalhe;
	
	@Export(label = "Venda", alignment=Alignment.CENTER, exhibitionOrder = 9)
	private String venda;
	
	@Export(label = "Faltas/Sobras", alignment=Alignment.CENTER, exhibitionOrder = 10)
	private String faltasSobras;
	
	@Export(label = "Deb/Cred.", alignment=Alignment.CENTER, exhibitionOrder = 11)
	private String debitosCreditos;
	
	@Export(label = "Saldo a Pagar R$", alignment=Alignment.RIGHT, exhibitionOrder = 12)
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
		this.setEstoque(dto.getEstoque().toString());
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
	public String getEstoque() {
		return estoque;
	}
	public void setEstoque(String estoque) {
		this.estoque = estoque;
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
