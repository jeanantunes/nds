package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class EstoqueProdutoRecolimentoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1025483945730089292L;

	@Export(label="Sequência", widthPercent=7, alignment=Alignment.CENTER)
	private Integer sequencia;
	
	@Export(label="Código", alignment=Alignment.CENTER)
	private String codigo;
	
	@Export(label="Produto", widthPercent=20)
	private String produto;
	
	@Export(label="Edição", widthPercent=7, alignment=Alignment.CENTER)
	private Long numeroEdicao;
	
	@Export(label="Preco Capa",  alignment=Alignment.CENTER)
	private BigDecimal precoCapa;
	

	@Export(label="Desc Logistica", widthPercent=7, alignment=Alignment.CENTER)
	private BigDecimal descontoLogistica;
	
	@Export(label="Lançamento", alignment=Alignment.CENTER)
	private BigInteger lancamento;
	
	@Export(label="Suplementar", alignment=Alignment.CENTER)
	private BigInteger suplementar;
	
	@Export(label="Recolhimento", alignment=Alignment.CENTER)
	private BigInteger recolhimento;
	
	@Export(label="Recolhimento PDV", alignment=Alignment.CENTER)
	private BigInteger recolhimentoPDV;
	
	@Export(label="Danificado", alignment=Alignment.CENTER)
	private BigInteger danificado;
	
	@Export(label="Total", alignment=Alignment.CENTER)
	private BigInteger total;

	public Integer getSequencia() {
		return sequencia;
	}

	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
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

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigInteger getLancamento() {
		return lancamento;
	}

	public void setLancamento(BigInteger lancamento) {
		this.lancamento = lancamento;
	}

	public BigInteger getSuplementar() {
		return suplementar;
	}

	public void setSuplementar(BigInteger suplementar) {
		this.suplementar = suplementar;
	}

	public BigInteger getRecolhimento() {
		return recolhimento;
	}

	public void setRecolhimento(BigInteger recolhimento) {
		this.recolhimento = recolhimento;
	}

	public BigInteger getRecolhimentoPDV() {
		return recolhimentoPDV;
	}

	public void setRecolhimentoPDV(BigInteger recolhimentoPDV) {
		this.recolhimentoPDV = recolhimentoPDV;
	}

	public BigInteger getDanificado() {
		return danificado;
	}

	public void setDanificado(BigInteger danificado) {
		this.danificado = danificado;
	}

	public BigInteger getTotal() {
		return total;
	}

	public void setTotal(BigInteger total) {
		this.total = total;
	}
	
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}
	public BigDecimal getDescontoLogistica() {
		return descontoLogistica;
	}

	public void setDescontoLogistica(BigDecimal descontoLogistica) {
		this.descontoLogistica = descontoLogistica;
	}


}
