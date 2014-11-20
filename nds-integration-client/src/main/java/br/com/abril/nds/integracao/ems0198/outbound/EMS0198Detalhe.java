package br.com.abril.nds.integracao.ems0198.outbound;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0198Detalhe {

	private String detalhes;
	
	@Field(offset = 1, length = 200)
	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}
	
	
//	private String registro;
//	
//	private String codigoCota;
//	
//	private String codigoProduto;
//	
//	private String edicao;
//	
//	private String nomePublicacao;
//	
//	private String codigoDeBarras;
//	
//	private String precoCusto;
//	
//	private String precoVenda;
//	
//	private String desconto;
//	
//	private String quantidade;
//	
//	private String dataEncalhe;
//	
//	private String diaChamada;
//	
//	
//	/**
//	 * 
//	 * FIXME: O tamanho esta "erroneamente" fixado em 150 posições porque este
//	 * a API FixedFormat4J gera apenas arquivos do tipo posicional (e 
//	 * este arquivo é do tipo que utiliza delimitadores).
//	 * O tamanho de 150 posições deve ser suficiente para este tipo de linha.
//	 * 
//	 * @return
//	 */	
//	@Field(offset = 1, length = 150)
//	public String getTipoRegistro() {
//		this.registro = "2" 
//				+ "|" + this.codigoCota 
//				+ "|" + this.codigoProduto
//				+ "|" + this.edicao
//				+ "|" + this.nomePublicacao 
//				+ "|" + this.codigoDeBarras 
//				+ "|" + this.precoCusto 
//				+ "|" + this.precoVenda 
//				+ "|" + this.desconto 
//				+ "|" + this.quantidade
//				+ "|" + this.dataEncalhe 
//				+ "|" + this.diaChamada;
//							
//		return registro;
//	}
//
//	public String getCodigoCota() {
//		return codigoCota;
//	}
//
//	public void setCodigoCota(String codigoCota) {
//		this.codigoCota = codigoCota;
//	}
//
//	public String getCodigoProduto() {
//		return codigoProduto;
//	}
//
//	public void setCodigoProduto(String codigoProduto) {
//		this.codigoProduto = codigoProduto;
//	}
//
//	public String getEdicao() {
//		return edicao;
//	}
//
//	public void setEdicao(String edicao) {
//		this.edicao = edicao;
//	}
//
//	public String getNomePublicacao() {
//		return nomePublicacao;
//	}
//
//	public void setNomePublicacao(String nomePublicacao) {
//		this.nomePublicacao = nomePublicacao;
//	}
//
//	public String getCodigoDeBarras() {
//		return codigoDeBarras;
//	}
//
//	public void setCodigoDeBarras(String codigoDeBarras) {
//		this.codigoDeBarras = codigoDeBarras;
//	}
//
//	public String getPrecoCusto() {
//		return precoCusto;
//	}
//
//	public void setPrecoCusto(String precoCusto) {
//		this.precoCusto = precoCusto;
//	}
//
//	public String getPrecoVenda() {
//		return precoVenda;
//	}
//
//	public void setPrecoVenda(String precoVenda) {
//		this.precoVenda = precoVenda;
//	}
//
//	public String getDesconto() {
//		return desconto;
//	}
//
//	public void setDesconto(String desconto) {
//		this.desconto = desconto;
//	}
//
//	public String getQuantidade() {
//		return quantidade;
//	}
//
//	public void setQuantidade(String quantidade) {
//		this.quantidade = quantidade;
//	}
//
//	public String getDataEncalhe() {
//		return dataEncalhe;
//	}
//
//	public void setDataEncalhe(String dataEncalhe) {
//		this.dataEncalhe = dataEncalhe;
//	}
//
//	public String getDiaChamada() {
//		return diaChamada;
//	}
//
//	public void setDiaChamada(String diaChamada) {
//		this.diaChamada = diaChamada;
//	}
		
}
