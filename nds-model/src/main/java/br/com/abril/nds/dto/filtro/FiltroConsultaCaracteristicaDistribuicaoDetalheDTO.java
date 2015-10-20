package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

public class FiltroConsultaCaracteristicaDistribuicaoDetalheDTO extends FiltroDTO implements Serializable{

	private static final long serialVersionUID = -6219213708879289967L;
	
	private String codigoProduto;
	private String classificacaoProduto;
	private String segmento;
	private Boolean brinde;
	private String nomeProduto;
	private String nomeEditor;
	private String chamadaCapa;
	private String faixaPrecoDe;
	private String faixaPrecoAte;
	private boolean opcaoFiltroPublicacao;
	private Long idProduto;
	
	private OrdemColuna ordemColuna;
	
	public enum OrdemColuna{
		
		CODIGO("codigoProduto"),
		PRODUTO("nomeProduto"),
		EDITOR("nomeEditor"),
		EDICAO("numeroEdicao"),
		PRECO_CAPA("precoCapa"),
		CLASSIFICACAO("classificacao"),
		SEGMENTO("segmento"),
		DT_LANCAMENTO("dataLancamentoString"),
		DT_RECOLHIMENTO("dataRecolhimentoString"),
		REPARTE("reparteString"),		
		VENDA("vendaString"),
		CHAMADA_CAPA("chamadaCapa");
		
		private String descricao;
		
		private OrdemColuna(String descricao) {
			this.descricao = descricao;
		}
		
		public String getDescricao(){
			return this.descricao;
		}
		
		@Override
		public String toString() {
			
			return this.descricao;
		}
	}
	
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
	}	public Boolean getBrinde() {
		return brinde;
	}
	public void setBrinde(Boolean brinde) {
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
	public boolean getOpcaoFiltroPublicacao() {
		return opcaoFiltroPublicacao;
	}
	public void setOpcaoFiltroPublicacao(boolean opcaoFiltroPublicacao) {
		this.opcaoFiltroPublicacao = opcaoFiltroPublicacao;
	}
	public OrdemColuna getOrdemColuna() {
		return ordemColuna;
	}
	public void setOrdemColuna(OrdemColuna ordemColuna) {
		this.ordemColuna = ordemColuna;
	}
	public Long getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}
}
