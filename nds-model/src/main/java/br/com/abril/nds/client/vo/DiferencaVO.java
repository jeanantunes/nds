package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoDirecionamentoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * Value Object para lançamento e consulta de diferença.
 * 
 * @author Discover Technology
 *
 */
@Exportable
public class DiferencaVO implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6329780427551941318L;

	private Long id;
	
	@Export(label = "Data", alignment = Alignment.CENTER, exhibitionOrder = 1, fontSize = 9, widthPercent = 7)
	private String dataLancamento;
	
	@Export(label = "Código", alignment = Alignment.CENTER, exhibitionOrder = 2, fontSize = 9, widthPercent = 7)
	private String codigoProduto;
	
	@Export(label = "Produto", exhibitionOrder = 3, fontSize = 9, widthPercent = 18)
	private String descricaoProduto;
	
	@Export(label = "Edição", alignment = Alignment.CENTER, exhibitionOrder = 4, fontSize = 9, widthPercent = 6)
	private String numeroEdicao;
	
	@Export(label = "Preço Venda R$", alignment = Alignment.RIGHT, exhibitionOrder = 5, fontSize = 9, widthPercent = 5)
	private String precoVenda;
	
	private String pacotePadrao;
	
	@Export(label = "Exemplar", alignment = Alignment.RIGHT, exhibitionOrder = 9, fontSize = 9, widthPercent = 7)
	private BigInteger quantidade;
	
	@Export(label = "Tipo de Diferença", exhibitionOrder = 7, fontSize = 9, widthPercent = 11)
	private String descricaoTipoDiferenca;
	
	@Export(label = "Nota", exhibitionOrder = 8, fontSize = 9)
	private String numeroNotaFiscal;
	
	@Export(label = "Status", exhibitionOrder = 10, alignment = Alignment.CENTER, fontSize = 9, widthPercent = 5)
	private String statusAprovacao;
	
	@Export(label = "Status Integracao", exhibitionOrder = 11, alignment = Alignment.CENTER, fontSize = 9, widthPercent = 13)
	private String statusIntegracao;
	
	private String motivoAprovacao;
	
	@Export(label = "Total R$", alignment = Alignment.RIGHT, exhibitionOrder = 12, fontSize = 9)
	private String valorTotalDiferenca;
	
	private BigInteger qtdeEstoqueAtual;
	
	private BigInteger qtdeEstoque;
	
	private BigDecimal vlTotalDiferenca;
	
	private Boolean automatica;
	
	private TipoEstoque tipoEstoque;
	
	private String descricaoTipoEstoque;
	
	private boolean cadastrado;

	private String fornecedor;
	
	private boolean existemRateios;
	
	private TipoDirecionamentoDiferenca tipoDirecionamento;
	
	private TipoDiferenca tipoDiferenca;
	

	/**
	 * Construtor padrão.
	 */
	public DiferencaVO() {
		
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	

	/**
	 * @return the descricaoTipoDiferenca
	 */
	public String getDescricaoTipoDiferenca() {
		return descricaoTipoDiferenca;
	}

	/**
	 * @param descricaoTipoDiferenca the descricaoTipoDiferenca to set
	 */
	public void setDescricaoTipoDiferenca(String descricaoTipoDiferenca) {
		this.descricaoTipoDiferenca = descricaoTipoDiferenca;
	}

	/**
	 * @param tipoDiferenca the tipoDiferenca to set
	 */
	public void setTipoDiferenca(TipoDiferenca tipoDiferenca) {
		this.tipoDiferenca = tipoDiferenca;
	}

	/**
	 * @return the qtdeEstoque
	 */
	public BigInteger getQtdeEstoque() {
		return qtdeEstoque;
	}

	/**
	 * @param qtdeEstoque the qtdeEstoque to set
	 */
	public void setQtdeEstoque(BigInteger qtdeEstoque) {
		this.qtdeEstoque = qtdeEstoque;
	}

	/**
	 * @return the tipoDirecionamento
	 */
	public TipoDirecionamentoDiferenca getTipoDirecionamento() {
		return tipoDirecionamento;
	}

	/**
	 * @param tipoDirecionamento the tipoDirecionamento to set
	 */
	public void setTipoDirecionamento(TipoDirecionamentoDiferenca tipoDirecionamento) {
		this.tipoDirecionamento = tipoDirecionamento;
	}

	/**
	 * @return the vlTotalDiferenca
	 */
	public BigDecimal getVlTotalDiferenca() {
		return vlTotalDiferenca;
	}

	/**
	 * @param vlTotalDiferenca the vlTotalDiferenca to set
	 */
	public void setVlTotalDiferenca(BigDecimal vlTotalDiferenca) {
		this.vlTotalDiferenca = vlTotalDiferenca;
	}


	/**
	 * @return the dataLancamento
	 */
	public String getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	/**
	 * @return the descricaoProduto
	 */
	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	/**
	 * @param descricaoProduto the descricaoProduto to set
	 */
	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	/**
	 * @return the numeroEdicao
	 */
	public String getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the precoVenda
	 */
	public String getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(String precoVenda) {
		this.precoVenda = precoVenda;
	}

	/**
	 * @return the pacotePadrao
	 */
	public String getPacotePadrao() {
		return pacotePadrao;
	}

	/**
	 * @param pacotePadrao the pacotePadrao to set
	 */
	public void setPacotePadrao(String pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	/**
	 * @return the quantidade
	 */
	public BigInteger getQuantidade() {
		return quantidade;
	}

	/**
	 * @param quantidade the quantidade to set
	 */
	public void setQuantidade(BigInteger quantidade) {
		this.quantidade = quantidade;
	}

	/**
	 * @return the tipoDiferenca
	 */
	public TipoDiferenca getTipoDiferenca() {
		return tipoDiferenca;
	}

	/**
	 * @param tipoDiferenca the tipoDiferenca to set
	 */
	public void setTipoDiferenca(String tipoDiferenca) {
		this.descricaoTipoDiferenca = tipoDiferenca;
	}

	/**
	 * @return the numeroNotaFiscal
	 */
	public String getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	/**
	 * @param numeroNotaFiscal the numeroNotaFiscal to set
	 */
	public void setNumeroNotaFiscal(String numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	/**
	 * @return the statusAprovacao
	 */
	public String getStatusAprovacao() {
		return statusAprovacao;
	}

	/**
	 * @param statusAprovacao the statusAprovacao to set
	 */
	public void setStatusAprovacao(String statusAprovacao) {
		this.statusAprovacao = statusAprovacao;
	}

	/**
	 * @return
	 */
	public String getStatusIntegracao() {
		return statusIntegracao;
	}

	/**
	 * @param statusIntegracao
	 */
	public void setStatusIntegracao(String statusIntegracao) {
		this.statusIntegracao = statusIntegracao;
	}

	/**
	 * @return the motivoAprovacao
	 */
	public String getMotivoAprovacao() {
		return motivoAprovacao;
	}

	/**
	 * @param motivoAprovacao the motivoAprovacao to set
	 */
	public void setMotivoAprovacao(String motivoAprovacao) {
		this.motivoAprovacao = motivoAprovacao;
	}
	
	/**
	 * @return the valorDiferenca
	 */
	public String getValorTotalDiferenca() {
		return valorTotalDiferenca;
	}

	/**
	 * @param valorDiferenca the valorDiferenca to set
	 */
	public void setValorTotalDiferenca(String valorTotalDiferenca) {
		this.valorTotalDiferenca = valorTotalDiferenca;
	}

	/**
	 * @return the qtdeEstoqueAtual
	 */
	public BigInteger getQtdeEstoqueAtual() {
		return qtdeEstoqueAtual;
	}

	/**
	 * @param qtdeEstoqueAtual the qtdeEstoqueAtual to set
	 */
	public void setQtdeEstoqueAtual(BigInteger qtdeEstoqueAtual) {
		this.qtdeEstoqueAtual = qtdeEstoqueAtual;
	}
	
	/**
	 * @return the cadastrado
	 */
	public boolean isCadastrado() {
		return cadastrado;
	}

	/**
	 * @param cadastrado the cadastrado to set
	 */
	public void setCadastrado(boolean cadastrado) {
		this.cadastrado = cadastrado;
	}
	
	/**
	 * @return the automatica
	 */
	public Boolean isAutomatica() {
		return automatica;
	}

	/**
	 * @param automatica the automatica to set
	 */
	public void setAutomatica(Boolean automatica) {
		this.automatica = automatica;
	}
	

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result
				+ ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
		
		result = prime * result
				+ ((dataLancamento == null) ? 0 : dataLancamento.hashCode());
		
		result = prime * result
				+ ((tipoDirecionamento == null) ? 0 : tipoDirecionamento.hashCode());
		
		result = prime * result
				+ ((tipoDiferenca == null) ? 0 : tipoDiferenca.hashCode());

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
		
		DiferencaVO other = (DiferencaVO) obj;
		
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;

		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		
		if (dataLancamento == null) {
			if (other.dataLancamento != null)
				return false;
		} else if (!dataLancamento.equals(other.dataLancamento))
			return false;
		
		if (tipoDirecionamento == null) {
			if (other.tipoDirecionamento != null)
				return false;
		} else if (!tipoDirecionamento.equals(other.tipoDirecionamento))
			return false;
		
		if (tipoEstoque == null) {
			if (other.tipoEstoque != null)
				return false;
		} else if (!tipoEstoque.equals(other.tipoEstoque))
			return false;
		
		if (tipoDiferenca == null) {
			if (other.tipoDiferenca != null)
				return false;
		} else if (!tipoDiferenca.equals(other.tipoDiferenca))
			return false;

		return true;
	}

	/**
	 * @return the tipoEstoque
	 */
	public TipoEstoque getTipoEstoque() {
		return tipoEstoque;
	}

	/**
	 * @param tipoEstoque the tipoEstoque to set
	 */
	public void setTipoEstoque(TipoEstoque tipoEstoque) {
		this.tipoEstoque = tipoEstoque;
		this.descricaoTipoEstoque = tipoEstoque != null ? tipoEstoque.getDescricao() : "";
	}

	/**
	 * @return the descricaoTipoEstoque
	 */
	public String getDescricaoTipoEstoque() {
		return this.descricaoTipoEstoque;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;		
	}
	
	public String getFornecedor() {
		return fornecedor;
	}

	/**
	 * @return the existemRateios
	 */
	public boolean isExistemRateios() {
		return existemRateios;
	}

	/**
	 * @param existemRateios the existemRateios to set
	 */
	public void setExistemRateios(boolean existemRateios) {
		this.existemRateios = existemRateios;
	}


}
