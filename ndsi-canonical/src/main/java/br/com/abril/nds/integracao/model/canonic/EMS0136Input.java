package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;


/**
 * Importação de arquivos do tipo "<i>Parciais de Produto (Envio PRODN)</i>."
 */
@Record
public class EMS0136Input extends IntegracaoDocument implements Serializable {

	/** */
	private static final long serialVersionUID = 1004062042976059921L;
	
	
	/** Código do Distribuidor. */
	private String codigoDistribuidor;
	
	/** Código do Produto. */
	private String codigoProduto;
	
	/** Número da Edição que esta na capa da publicação. */
	private Long edicaoCapa;
	
	/** Preço da publicação. */
	private BigDecimal preco;
	
	/** Número do Período. */
	private Integer numeroPeriodo;
	
	/** Valor do Desconto. */
	private BigDecimal valorDesconto;
	
	/** Data de Lançamento da publicação. */
	private Date dataLancamento;
	
	/** Data de Recolhimento da publicação. */
	private Date dataRecolhimento;
	
	/** Tipo de Recolhimento. */
	private String tipoRecolhimento;

	
	@Field(offset = 1, length = 7)
	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	@Field(offset = 8, length = 8)
	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	@Field(offset = 16, length = 4)
	public Long getEdicaoCapa() {
		return edicaoCapa;
	}

	public void setEdicaoCapa(Long edicaoCapa) {
		this.edicaoCapa = edicaoCapa;
	}

	@Field(offset = 20, length = 14)
	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	@Field(offset = 34, length = 2)
	public Integer getNumeroPeriodo() {
		return numeroPeriodo;
	}

	public void setNumeroPeriodo(Integer numeroPeriodo) {
		this.numeroPeriodo = numeroPeriodo;
	}

	@Field(offset = 36, length = 5)
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	@Field(offset = 41, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	@Field(offset = 49, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	@Field(offset = 57, length = 1)
	public String getTipoRecolhimento() {
		return tipoRecolhimento;
	}

	public void setTipoRecolhimento(String tipoRecolhimento) {
		this.tipoRecolhimento = tipoRecolhimento;
	}
	
}
