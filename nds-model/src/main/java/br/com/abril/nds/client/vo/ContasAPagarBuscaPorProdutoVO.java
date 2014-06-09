package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;

public class ContasAPagarBuscaPorProdutoVO implements Serializable{
	
	private static final long serialVersionUID = -1407028163060234907L;
	
	@Export(label = "Rctl", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String rctl;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String codigo;
	
	@Export(label = "Produto", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private String produto;
	
	@Export(label = "Edição", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private String edicao;

	@Export(label = "Tipo", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private String tipo;

	@Export(label = "Reparte", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private String reparte;
	
	@Export(label = "Suplementação", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private String suplementacao;
	
	@Export(label = "Encalhe", alignment=Alignment.CENTER, exhibitionOrder = 8)
	private String editor;
	
	@Export(label = "Venda", alignment=Alignment.CENTER, exhibitionOrder = 9)
	private String venda;
	
	@Export(label = "Faltas/Sobras", alignment=Alignment.CENTER, exhibitionOrder = 10)
	private String faltasSobras;
	
	@Export(label = "Deb/Cred", alignment=Alignment.CENTER, exhibitionOrder = 11)
	private String debCred;
	
	@Export(label = "Saldo a Pager R$", alignment=Alignment.CENTER, exhibitionOrder = 12)
	private String saldoAPagar;
	
	
	public ContasAPagarBuscaPorProdutoVO(FiltroContasAPagarDTO dto){
		this.codigo = dto.getProdutoEdicaoIDs().get(0).toString();
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

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
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

	public String getDebCred() {
		return debCred;
	}

	public void setDebCred(String debCred) {
		this.debCred = debCred;
	}

	public String getSaldoAPagar() {
		return saldoAPagar;
	}

	public void setSaldoAPagar(String saldoAPagar) {
		this.saldoAPagar = saldoAPagar;
	}
	

	
	
	

}
