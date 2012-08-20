package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0135Input extends IntegracaoDocument implements Serializable {

	private static final long serialVersionUID = -2655294342766612808L;
	
	private Integer distribuidor;
	private Date dataEmissao;
	private Long notaFiscal;
	private Integer serieNotaFiscal;
	private Date dataLancamento;
	private Integer pacotePadrao;
	private Integer qtdExemplar;
	private Integer codigoProduto;
	private String nomeProduto;
	private Integer edicao;
	private Double preco;
	private Double desconto;
	private String codigoBarras;
	private Integer codigoSiscorp;
	private String chaveAcessoNF;
	
	@Field(offset = 1, length = 7)
	public Integer getDistribuidor() {
		return distribuidor;
	}
	public void setDistribuidor(Integer distribuidor) {
		this.distribuidor = distribuidor;
	}
	
	@Field(offset = 8, length = 8)
	@FixedFormatPattern("yyyyMMdd")  
	public Date getDataEmissao() {
		return dataEmissao;
	}
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	
	@Field(offset = 16, length = 6)
	public Long getNotaFiscal() {
		return notaFiscal;
	}
	public void setNotaFiscal(Long notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
	
	@Field(offset = 22, length = 3)
	public Integer getSerieNotaFiscal() {
		return serieNotaFiscal;
	}
	public void setSerieNotaFiscal(Integer serieNotaFiscal) {
		this.serieNotaFiscal = serieNotaFiscal;
	}
	
	@Field(offset = 25, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	
	@Field(offset = 33, length = 5)
	public Integer getPacotePadrao() {
		return pacotePadrao;
	}
	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
	
	@Field(offset = 38, length = 8)
	public Integer getQtdExemplar() {
		return qtdExemplar;
	}
	public void setQtdExemplar(Integer qtdExemplar) {
		this.qtdExemplar = qtdExemplar;
	}
	
	@Field(offset = 46, length = 8)
	public Integer getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(Integer codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	@Field(offset = 54, length = 16)
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	@Field(offset = 70, length = 4)
	public Integer getEdicao() {
		return edicao;
	}
	public void setEdicao(Integer edicao) {
		this.edicao = edicao;
	}
	
	@Field(offset = 74, length = 14)
	public Double getPreco() {
		return preco;
	}
	public void setPreco(Double preco) {
		this.preco = preco;
	}
	
	@Field(offset = 88, length = 5)
	public Double getDesconto() {
		return desconto;
	}
	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}
	@Field(offset = 93, length = 18)
	public String getCodigoBarras() {
		return codigoBarras;
	}
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	
	@Field(offset = 111, length = 10)
	public Integer getCodigoSiscorp() {
		return codigoSiscorp;
	}
	public void setCodigoSiscorp(Integer codigoSiscorp) {
		this.codigoSiscorp = codigoSiscorp;
	}
	
	@Field(offset = 121, length = 44)
	public String getChaveAcessoNF() {
		return chaveAcessoNF;
	}
	public void setChaveAcessoNF(String chaveAcessoNF) {
		this.chaveAcessoNF = chaveAcessoNF;
	}
	
	
	
	

}
