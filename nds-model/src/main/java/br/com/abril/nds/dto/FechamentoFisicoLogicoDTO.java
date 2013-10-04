package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;


@Exportable
public class FechamentoFisicoLogicoDTO {

	@Export(label = "Código", alignment = Alignment.LEFT, exhibitionOrder = 2, fontSize=9)
	private String codigo;
	
	@Export(label = "Produto", alignment = Alignment.LEFT, exhibitionOrder = 3, fontSize=9, widthPercent=30)
	private String produto;
	
	@Export(label = "Edição", alignment = Alignment.LEFT, exhibitionOrder = 4, fontSize=9)
	private Long edicao;
	
	private Long chamadaEncalheId;
	
	private Long produtoEdicao;
	
	private String tipo;
	
	private String recolhimento;
	
	private boolean suplementar;
	
	private Date dataRecolhimento;
	
	private BigDecimal precoCapa;
	private BigDecimal precoCapaDesconto;
	private BigInteger exemplaresDevolucao;
	private BigDecimal total;
	
	@Export(label = "Físico", alignment = Alignment.RIGHT, exhibitionOrder = 7, fontSize=9)
	private Long fisico;
	
	@Export(label = "Diferença", alignment = Alignment.RIGHT, exhibitionOrder = 8, fontSize=9)
	private Long diferenca = Long.valueOf(0);
	
	@Export(label = "Preço Capa R$", alignment = Alignment.RIGHT, exhibitionOrder = 4, fontSize=9)
	private String precoCapaFormatado;

	@Export(label = "Preço Capa Desc R$", alignment = Alignment.RIGHT, exhibitionOrder = 5, fontSize=9)
	private String precoCapaDescFormatado;
	
	@Export(label = "Exempl. Devolução", alignment = Alignment.CENTER, exhibitionOrder = 6, fontSize=9)
	private String exemplaresDevolucaoFormatado;
	
	@Export(label = "Total R$", alignment = Alignment.RIGHT, exhibitionOrder = 7, fontSize=9)
	private String totalFormatado;
	
	private String replicar = "";
	
	private Boolean fechado;
	
	@Export(label = "Estoque", alignment = Alignment.CENTER, exhibitionOrder = 8, fontSize=9)
	private String estoque;
	
	@Export(label = "Sequência", exhibitionOrder = 1, fontSize=9)
	private Integer sequencia;
	
	private Origem origem;
	
	private Long produtoDescontoLogisticaId;
	
	private Long produtoEdicaoDescontoLogisticaId;
	
	private BigDecimal desconto;
	
	private boolean chamadao; 
	
	public String getReplicar() {
		return replicar;
	}

	public String getPrecoCapaFormatado() {
		return this.precoCapaFormatado;
	}
	
	public String getExemplaresDevolucaoFormatado() {
		return this.exemplaresDevolucaoFormatado;
	}
	
	public String getTotalFormatado() {
		return this.totalFormatado;
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
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	public Long getProdutoEdicao() {
		return produtoEdicao;
	}
	public void setProdutoEdicao(Long produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa == null ? BigDecimal.ZERO : precoCapa;
		this.precoCapaFormatado = CurrencyUtil.formatarValor(this.precoCapa); 
	}
	public BigInteger getExemplaresDevolucao() {
		return exemplaresDevolucao;
	}
	public void setExemplaresDevolucao(BigInteger exemplaresDevolucao) {
		if (exemplaresDevolucao == null || exemplaresDevolucao.compareTo(BigInteger.ZERO) == -1) {
			this.exemplaresDevolucao = BigInteger.ZERO;
		}else{
			this.exemplaresDevolucao = exemplaresDevolucao;
		}
		this.exemplaresDevolucaoFormatado = String.valueOf(this.exemplaresDevolucao.intValue());
		
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
		this.totalFormatado = CurrencyUtil.formatarValor(this.total); 
	}
	public Long getFisico() {
		return fisico;
	}
	public void setFisico(Long fisico) {
		this.fisico = fisico;
	}
	public Long getDiferenca() {
		return diferenca;
	}
	public void setDiferenca(Long diferenca) {
		this.diferenca = diferenca;
	}
	public Boolean getFechado() {
		return fechado;
	}
	public void setFechado(Boolean fechado) {
		this.fechado = fechado;
	}

	
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	public String getTipo() {
		return tipo;
	}
	
	public String getRecolhimento() {
		return recolhimento;
	}

	public void setRecolhimento(String recolhimento) {
		this.recolhimento = recolhimento;
	}

	public void setEstoque(String estoque) {
		this.estoque = estoque;
	}
	
	public String getEstoque() {
		return estoque;
	}

	public BigDecimal getPrecoCapaDesconto() {
		return precoCapaDesconto;
	}

	public void setPrecoCapaDesconto(BigDecimal precoCapaDesconto) {
		this.precoCapaDesconto = precoCapaDesconto;
		
		this.precoCapaDescFormatado = (this.precoCapaDesconto!= null) 
				? CurrencyUtil.formatarValor(this.precoCapaDesconto)
				: CurrencyUtil.formatarValor(BigDecimal.ZERO);
	}

	public String getPrecoCapaDescFormatado() {
	
		return precoCapaDescFormatado;
	}

	public Integer getSequencia() {
		return sequencia;
	}

	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}

	/**
	 * @param replicar the replicar to set
	 */
	public void setReplicar(String replicar) {
		this.replicar = replicar;
	}

	/**
	 * @return the origem
	 */
	public Origem getOrigem() {
		return origem;
	}

	/**
	 * @param origem the origem to set
	 */
	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	/**
	 * @return the produtoDescontoLogisticaId
	 */
	public Long getProdutoDescontoLogisticaId() {
		return produtoDescontoLogisticaId;
	}

	/**
	 * @param produtoDescontoLogisticaId the produtoDescontoLogisticaId to set
	 */
	public void setProdutoDescontoLogisticaId(Long produtoDescontoLogisticaId) {
		this.produtoDescontoLogisticaId = produtoDescontoLogisticaId;
	}

	/**
	 * @return the produtoEdicaoDescontoLogisticaId
	 */
	public Long getProdutoEdicaoDescontoLogisticaId() {
		return produtoEdicaoDescontoLogisticaId;
	}

	/**
	 * @param produtoEdicaoDescontoLogisticaId the produtoEdicaoDescontoLogisticaId to set
	 */
	public void setProdutoEdicaoDescontoLogisticaId(
			Long produtoEdicaoDescontoLogisticaId) {
		this.produtoEdicaoDescontoLogisticaId = produtoEdicaoDescontoLogisticaId;
	}

	/**
	 * @return the desconto
	 */
	public BigDecimal getDesconto() {
		return desconto;
	}

	/**
	 * @param desconto the desconto to set
	 */
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	/**
	 * @return the chamadaEncalheId
	 */
	public Long getChamadaEncalheId() {
		return chamadaEncalheId;
	}

	/**
	 * @param chamadaEncalheId the chamadaEncalheId to set
	 */
	public void setChamadaEncalheId(Long chamadaEncalheId) {
		this.chamadaEncalheId = chamadaEncalheId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((produtoEdicao == null) ? 0 : produtoEdicao.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FechamentoFisicoLogicoDTO other = (FechamentoFisicoLogicoDTO) obj;
		if (produtoEdicao == null) {
			if (other.produtoEdicao != null)
				return false;
		} else if (!produtoEdicao.equals(other.produtoEdicao))
			return false;
		return true;
	}

	public boolean isSuplementar() {
		return suplementar;
	}

	public void setSuplementar(boolean suplementar) {
		this.suplementar = suplementar;
	}

	/**
	 * @return the chamadao
	 */
	public boolean isChamadao() {
		return chamadao;
	}

	/**
	 * @param chamadao the chamadao to set
	 */
	public void setChamadao(boolean chamadao) {
		this.chamadao = chamadao;
	}
}
