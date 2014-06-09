package br.com.abril.nds.dto.filtro;

public class FiltroConsultaCaracteristicaDistribuicaoSimplesDTO extends FiltroDTO{

	private String codigoProduto;
	private String classificacaoProduto;
	private String segmento;
	private String brinde;
	private String nomeProduto;
	private String nomeEditor;
	private String chamadaCapa;
	private String faixaPrecoDe;
	private String faixaPrecoAte;
	private boolean opcaoFiltroPublicacao;
	
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getClassificacaoProduto() {
		return classificacaoProduto;
	}
	public void setClassificacaoProduto(String classificacaoProduto) {
		this.classificacaoProduto = classificacaoProduto;
	}
	public String getSegmento() {
		return segmento;
	}
	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}
	public String getBrinde() {
		return brinde;
	}
	public void setBrinde(String brinde) {
		this.brinde = brinde;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public String getNomeEditor() {
		return nomeEditor;
	}
	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}
	public String getChamadaCapa() {
		return chamadaCapa;
	}
	public void setChamadaCapa(String chamadaCapa) {
		this.chamadaCapa = chamadaCapa;
	}
	public String getFaixaPrecoDe() {
		return faixaPrecoDe;
	}
	public void setFaixaPrecoDe(String faixaPrecoDe) {
		this.faixaPrecoDe = faixaPrecoDe;
	}
	public String getFaixaPrecoAte() {
		return faixaPrecoAte;
	}
	public void setFaixaPrecoAte(String faixaPrecoAte) {
		this.faixaPrecoAte = faixaPrecoAte;
	}
	public boolean isOpcaoFiltroPublicacao() {
		return opcaoFiltroPublicacao;
	}
	public void setOpcaoFiltroPublicacao(boolean opcaoFiltroPublicacao) {
		this.opcaoFiltroPublicacao = opcaoFiltroPublicacao;
	}
	
	
	
}
