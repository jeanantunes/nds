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

	
	@Field(offset = 29, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	
	@Field(offset = 37, length = 5)
	public Integer getPacotePadrao() {
		return pacotePadrao;
	}
	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}
	
	@Field(offset = 42, length = 8)
	public Integer getQtdExemplar() {
		return qtdExemplar;
	}
	public void setQtdExemplar(Integer qtdExemplar) {
		this.qtdExemplar = qtdExemplar;
	}
	
	@Field(offset = 44, length = 8)
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	@Field(offset = 58, length = 16)
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	@Field(offset = 74, length = 4)
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	
	@Field(offset = 78, length = 14)
	public Double getPreco() {
		return preco;
	}
	public void setPreco(Double preco) {
		this.preco = preco;
	}
	
	@Field(offset = 92, length = 7)
	public Double getDesconto() {
		return desconto;
	}
	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}
	@Field(offset = 99, length = 18)
	public String getCodigoBarras() {
		return codigoBarras;
	}
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	
	@Field(offset = 117, length = 8)
	public Long getCodigoSiscorp() {
		return codigoSiscorp;
	}
	public void setCodigoSiscorp(Long codigoSiscorp) {
		this.codigoSiscorp = codigoSiscorp;
	}		
}