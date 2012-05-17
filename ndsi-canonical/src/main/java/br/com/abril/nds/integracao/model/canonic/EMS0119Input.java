package br.com.abril.nds.integracao.model.canonic;

import java.math.BigDecimal;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0119Input {
	private String codigoDaPublicacao;
	private Integer edicao;
	private String nomeDaPublicacao;
	private String numeroDeEdicoes;
	private String periodicidade;
	private Long tipoDePublicacao;
	private String statusDaPublicacao;
	private Long codigoDoEditor;
	private String cobrancaAntecipada;
	private String condTransmiteHistograma;
	private int pacotePadrao;
	private String condPublicacaoEspecial;
	private String condProdutoAVista;
	private String contextoFornecedorProduto;
	private String codigoFornecedorPublic;
	private BigDecimal desconto;
	private String origemDoProduto;
	private String cromos;
	private String nomeComercial;
	
	@Field(offset = 1, length = 5)
	public String getCodigoDaPublicacao() {
		return codigoDaPublicacao;
	}
	public void setCodigoDaPublicacao(String codigoDaPublicacao) {
		this.codigoDaPublicacao = codigoDaPublicacao;
	}
	
	@Field(offset = 6, length = 3)
	public Integer getEdicao() {
		return edicao;
	}
	public void setEdicao(Integer edicao) {
		this.edicao = edicao;
	}
	
	@Field(offset = 9, length = 20)
	public String getNomeDaPublicacao() {
		return nomeDaPublicacao;
	}
	public void setNomeDaPublicacao(String nomeDaPublicacao) {
		this.nomeDaPublicacao = nomeDaPublicacao;
	}
	
	
	public String getNumeroDeEdicoes() {
		return numeroDeEdicoes;
	}
	public void setNumeroDeEdicoes(String numeroDeEdicoes) {
		this.numeroDeEdicoes = numeroDeEdicoes;
	}
	
	@Field(offset = 31, length = 3)
	public String getPeriodicidade() {
		return periodicidade;
	}
	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	@Field(offset = 34, length = 2)
	public Long getTipoDePublicacao() {
		return tipoDePublicacao;
	}
	public void setTipoDePublicacao(Long tipoDePublicacao) {
		this.tipoDePublicacao = tipoDePublicacao;
	}
	
	@Field(offset = 36, length = 1)
	public String getStatusDaPublicacao() {
		return statusDaPublicacao;
	}
	public void setStatusDaPublicacao(String statusDaPublicacao) {
		this.statusDaPublicacao = statusDaPublicacao;
	}
	
	@Field(offset = 37, length = 7)
	public Long getCodigoDoEditor() {
		return codigoDoEditor;
	}
	public void setCodigoDoEditor(Long codigoDoEditor) {
		this.codigoDoEditor = codigoDoEditor;
	}
	
	
	public String getCobrancaAntecipada() {
		return cobrancaAntecipada;
	}
	public void setCobrancaAntecipada(String cobrancaAntecipada) {
		this.cobrancaAntecipada = cobrancaAntecipada;
	}
	
	
	public String getCondTransmiteHistograma() {
		return condTransmiteHistograma;
	}
	public void setCondTransmiteHistograma(String condTransmiteHistograma) {
		this.condTransmiteHistograma = condTransmiteHistograma;
	}
	
	@Field(offset = 51, length = 5)
	public int getPacotePadrao() {
		return pacotePadrao;
	}
	public void setPacotePadrao(int pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
	
	
	public String getCondPublicacaoEspecial() {
		return condPublicacaoEspecial;
	}
	public void setCondPublicacaoEspecial(String condPublicacaoEspecial) {
		this.condPublicacaoEspecial = condPublicacaoEspecial;
	}
	
	
	public String getCondProdutoAVista() {
		return condProdutoAVista;
	}
	public void setCondProdutoAVista(String condProdutoAVista) {
		this.condProdutoAVista = condProdutoAVista;
	}
	
	
	public String getContextoFornecedorProduto() {
		return contextoFornecedorProduto;
	}
	public void setContextoFornecedorProduto(String contextoFornecedorProduto) {
		this.contextoFornecedorProduto = contextoFornecedorProduto;
	}
	
	
	public String getCodigoFornecedorPublic() {
		return codigoFornecedorPublic;
	}
	public void setCodigoFornecedorPublic(String codigoFornecedorPublic) {
		this.codigoFornecedorPublic = codigoFornecedorPublic;
	}
	
	@Field(offset = 66, length = 9)
	public BigDecimal getDesconto() {
		return desconto;
	}
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	
	
	public String getOrigemDoProduto() {
		return origemDoProduto;
	}
	public void setOrigemDoProduto(String origemDoProduto) {
		this.origemDoProduto = origemDoProduto;
	}
	
	
	public String getCromos() {
		return cromos;
	}
	public void setCromos(String cromos) {
		this.cromos = cromos;
	}
	
	@Field(offset = 77, length = 46)
	public String getNomeComercial() {
		return nomeComercial;
	}
	public void setNomeComercial(String nomeComercial) {
		this.nomeComercial = nomeComercial;
	}
	
	
	
	
}