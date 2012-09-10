package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigInteger;

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
	
	@Export(label = "Data", alignment = Alignment.CENTER, exhibitionOrder = 1)
	private String dataLancamento;
	
	@Export(label = "Código", alignment = Alignment.CENTER, exhibitionOrder = 2)
	private String codigoProduto;
	
	@Export(label = "Produto", exhibitionOrder = 3)
	private String descricaoProduto;
	
	@Export(label = "Edição", alignment = Alignment.CENTER, exhibitionOrder = 4)
	private String numeroEdicao;
	
	@Export(label = "Preço Venda R$", alignment = Alignment.RIGHT, exhibitionOrder = 5)
	private String precoVenda;
	
	private String pacotePadrao;
	
	@Export(label = "Exemplar", alignment = Alignment.RIGHT, exhibitionOrder = 9)
	private BigInteger quantidade;
	
	@Export(label = "Tipo de Diferença", exhibitionOrder = 7)
	private String tipoDiferenca;
	
	@Export(label = "Nota", exhibitionOrder = 8)
	private String numeroNotaFiscal;
	
	@Export(label = "Status", exhibitionOrder = 10, alignment = Alignment.CENTER)
	private String statusAprovacao;
	
	private String motivoAprovacao;
	
	@Export(label = "Total R$", alignment = Alignment.RIGHT, exhibitionOrder = 11)
	private String valorTotalDiferenca;
	
	private BigInteger qtdeEstoqueAtual;
	
	private Boolean automatica;
	
	private String tipoEstoque;
	
	private boolean cadastrado;

	private String fornecedor;
	
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
	public String getTipoDiferenca() {
		return tipoDiferenca;
	}

	/**
	 * @param tipoDiferenca the tipoDiferenca to set
	 */
	public void setTipoDiferenca(String tipoDiferenca) {
		this.tipoDiferenca = tipoDiferenca;
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
				+ ((automatica == null) ? 0 : automatica.hashCode());
		result = prime * result + (cadastrado ? 1231 : 1237);
		result = prime * result
				+ ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		result = prime * result
				+ ((dataLancamento == null) ? 0 : dataLancamento.hashCode());
		result = prime
				* result
				+ ((descricaoProduto == null) ? 0 : descricaoProduto.hashCode());
		result = prime * result
				+ ((fornecedor == null) ? 0 : fornecedor.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((motivoAprovacao == null) ? 0 : motivoAprovacao.hashCode());
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
		result = prime
				* result
				+ ((numeroNotaFiscal == null) ? 0 : numeroNotaFiscal.hashCode());
		result = prime * result
				+ ((pacotePadrao == null) ? 0 : pacotePadrao.hashCode());
		result = prime * result
				+ ((precoVenda == null) ? 0 : precoVenda.hashCode());
		result = prime
				* result
				+ ((qtdeEstoqueAtual == null) ? 0 : qtdeEstoqueAtual.hashCode());
		result = prime * result
				+ ((quantidade == null) ? 0 : quantidade.hashCode());
		result = prime * result
				+ ((statusAprovacao == null) ? 0 : statusAprovacao.hashCode());
		result = prime * result
				+ ((tipoDiferenca == null) ? 0 : tipoDiferenca.hashCode());
		result = prime * result
				+ ((tipoEstoque == null) ? 0 : tipoEstoque.hashCode());
		result = prime
				* result
				+ ((valorTotalDiferenca == null) ? 0 : valorTotalDiferenca
						.hashCode());
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
		if (automatica == null) {
			if (other.automatica != null)
				return false;
		} else if (!automatica.equals(other.automatica))
			return false;
		if (cadastrado != other.cadastrado)
			return false;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		if (dataLancamento == null) {
			if (other.dataLancamento != null)
				return false;
		} else if (!dataLancamento.equals(other.dataLancamento))
			return false;
		if (descricaoProduto == null) {
			if (other.descricaoProduto != null)
				return false;
		} else if (!descricaoProduto.equals(other.descricaoProduto))
			return false;
		if (fornecedor == null) {
			if (other.fornecedor != null)
				return false;
		} else if (!fornecedor.equals(other.fornecedor))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (motivoAprovacao == null) {
			if (other.motivoAprovacao != null)
				return false;
		} else if (!motivoAprovacao.equals(other.motivoAprovacao))
			return false;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		if (numeroNotaFiscal == null) {
			if (other.numeroNotaFiscal != null)
				return false;
		} else if (!numeroNotaFiscal.equals(other.numeroNotaFiscal))
			return false;
		if (pacotePadrao == null) {
			if (other.pacotePadrao != null)
				return false;
		} else if (!pacotePadrao.equals(other.pacotePadrao))
			return false;
		if (precoVenda == null) {
			if (other.precoVenda != null)
				return false;
		} else if (!precoVenda.equals(other.precoVenda))
			return false;
		if (qtdeEstoqueAtual == null) {
			if (other.qtdeEstoqueAtual != null)
				return false;
		} else if (!qtdeEstoqueAtual.equals(other.qtdeEstoqueAtual))
			return false;
		if (quantidade == null) {
			if (other.quantidade != null)
				return false;
		} else if (!quantidade.equals(other.quantidade))
			return false;
		if (statusAprovacao == null) {
			if (other.statusAprovacao != null)
				return false;
		} else if (!statusAprovacao.equals(other.statusAprovacao))
			return false;
		if (tipoDiferenca == null) {
			if (other.tipoDiferenca != null)
				return false;
		} else if (!tipoDiferenca.equals(other.tipoDiferenca))
			return false;
		if (tipoEstoque == null) {
			if (other.tipoEstoque != null)
				return false;
		} else if (!tipoEstoque.equals(other.tipoEstoque))
			return false;
		if (valorTotalDiferenca == null) {
			if (other.valorTotalDiferenca != null)
				return false;
		} else if (!valorTotalDiferenca.equals(other.valorTotalDiferenca))
			return false;
		return true;
	}

	/**
	 * @return the tipoEstoque
	 */
	public String getTipoEstoque() {
		return tipoEstoque;
	}

	/**
	 * @param tipoEstoque the tipoEstoque to set
	 */
	public void setTipoEstoque(String tipoEstoque) {
		this.tipoEstoque = tipoEstoque;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;		
	}
	
	public String getFornecedor() {
		return fornecedor;
	}

}
