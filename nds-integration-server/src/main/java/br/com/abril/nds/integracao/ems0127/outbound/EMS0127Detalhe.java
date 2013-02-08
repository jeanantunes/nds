package br.com.abril.nds.integracao.ems0127.outbound;

import java.math.BigDecimal;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0127Detalhe {
	
	private String tipoRegistro;
	private String codDistribuidor;
	private String codProduto;
	private Long edicao;
	private BigDecimal quantidadeEncalhe;
	private BigDecimal quantidadeVenda;
	private Date diaRecolhimento;
	private String flagFechamento;
	private String tipoProduto;
	private Date dataLancamento;
	private Date dataRecolhimento;
	
	
	/**
	 * Método construtor que recebe o código do produto e o dia de recolhimento.
	 * 
	 * @param codProduto
	 * @param diaRecolhimento
	 */
	public EMS0127Detalhe(String codProduto, Date diaRecolhimento) {
		this.codProduto = codProduto;
		this.diaRecolhimento = diaRecolhimento;
	}
	
	
	@Field(offset = 1, length = 2, paddingChar = '2')
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	
	@Field(offset = 2, length = 7)
	public String getCodDistribuidor() {
		return codDistribuidor;
	}
	
	@Field(offset = 9, length = 8)
	public String getCodProduto() {
		return codProduto;
	}
	
	@Field(offset = 17, length = 4)
	public Long getEdicao() {
		return edicao;
	}
	
	@Field(offset = 21, length = 7)
	@FixedFormatDecimal(decimals = 2, useDecimalDelimiter = true)
	public BigDecimal getQuantidadeEncalhe() {
		return quantidadeEncalhe;
	}
	
	@Field(offset = 28, length = 7)
	@FixedFormatDecimal(decimals = 2, useDecimalDelimiter = true)
	public BigDecimal getQuantidadeVenda() {
		return quantidadeVenda;
	}
	
	@Field(offset = 35, length = 1)
	@FixedFormatPattern("d")
	public Date getDiaRecolhimento() {
		return diaRecolhimento;
	}
	
	@Field(offset = 36, length = 1)
	public String getFlagFechamento() {
		return flagFechamento;
	}

	@Field(offset = 37, length = 1)
	public String getTipoProduto() {
		return tipoProduto;
	}
	
	@Field(offset = 38, length = 8)
	@FixedFormatPattern("ddMMyyyy")
	public Date getDataLancamento() {
		return dataLancamento;
	}
	
	@Field(offset = 46, length = 8)
	@FixedFormatPattern("ddMMyyyy")
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public void setCodDistribuidor(String codDistribuidor) {
		this.codDistribuidor = codDistribuidor;
	}

	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}

	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}

	public void setQuantidadeEncalhe(BigDecimal quantidadeEncalhe) {
		this.quantidadeEncalhe = quantidadeEncalhe;
	}

	public void setQuantidadeVenda(BigDecimal quantidadeVenda) {
		this.quantidadeVenda = quantidadeVenda;
	}

	public void setDiaRecolhimento(Date diaRecolhimento) {
		this.diaRecolhimento = diaRecolhimento;
	}

	public void setFlagFechamento(String flagFechamento) {
		this.flagFechamento = flagFechamento;
	}

	public void setTipoProduto(String tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}		
}
	