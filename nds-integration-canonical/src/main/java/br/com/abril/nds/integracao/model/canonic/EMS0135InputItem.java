package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0135InputItem extends IntegracaoDocumentDetail implements Serializable {

	private static final long serialVersionUID = 7027560778379484156L;
	
	private Date dataLancamento;
	
	private Integer pacotePadrao;
	
	private Integer qtdExemplar;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long edicao;
	
	private Double preco;
	
	private Double desconto;
	
	private String codigoBarras;
	
	private Long codigoSiscorp;

	
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
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
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
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	
	@Field(offset = 74, length = 14)
	public Double getPreco() {
		return preco;
	}
	public void setPreco(Double preco) {
		this.preco = preco;
	}
	
	@Field(offset = 88, length = 7)
	public Double getDesconto() {
		return desconto;
	}
	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}
	@Field(offset = 95, length = 18)
	public String getCodigoBarras() {
		return codigoBarras;
	}
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	
	@Field(offset = 113, length = 10)
	public Long getCodigoSiscorp() {
		return codigoSiscorp;
	}
	public void setCodigoSiscorp(Long codigoSiscorp) {
		this.codigoSiscorp = codigoSiscorp;
	}
		
}


